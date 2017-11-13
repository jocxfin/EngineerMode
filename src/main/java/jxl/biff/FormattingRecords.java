package jxl.biff;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class FormattingRecords {
    private static Logger logger = Logger.getLogger(FormattingRecords.class);
    private Fonts fonts;
    private HashMap formats = new HashMap(10);
    private ArrayList formatsList = new ArrayList(10);
    private int nextCustomIndexNumber;
    private PaletteRecord palette;
    private ArrayList xfRecords = new ArrayList(10);

    public FormattingRecords(Fonts f) {
        this.fonts = f;
        this.nextCustomIndexNumber = 164;
    }

    public final void addStyle(XFRecord xf) throws NumFormatRecordsException {
        if (!xf.isInitialized()) {
            xf.initialize(this.xfRecords.size(), this, this.fonts);
            this.xfRecords.add(xf);
        } else if (xf.getXFIndex() >= this.xfRecords.size()) {
            this.xfRecords.add(xf);
        }
    }

    public final void addFormat(DisplayFormat fr) throws NumFormatRecordsException {
        if (fr.isInitialized() && fr.getFormatIndex() >= 441) {
            logger.warn("Format index exceeds Excel maximum - assigning custom number");
            fr.initialize(this.nextCustomIndexNumber);
            this.nextCustomIndexNumber++;
        }
        if (!fr.isInitialized()) {
            fr.initialize(this.nextCustomIndexNumber);
            this.nextCustomIndexNumber++;
        }
        if (this.nextCustomIndexNumber <= 441) {
            if (fr.getFormatIndex() >= this.nextCustomIndexNumber) {
                this.nextCustomIndexNumber = fr.getFormatIndex() + 1;
            }
            if (!fr.isBuiltIn()) {
                this.formatsList.add(fr);
                this.formats.put(new Integer(fr.getFormatIndex()), fr);
                return;
            }
            return;
        }
        this.nextCustomIndexNumber = 441;
        throw new NumFormatRecordsException();
    }

    public final boolean isDate(int pos) {
        boolean z = false;
        XFRecord xfr = (XFRecord) this.xfRecords.get(pos);
        if (xfr.isDate()) {
            return true;
        }
        FormatRecord fr = (FormatRecord) this.formats.get(new Integer(xfr.getFormatRecord()));
        if (fr != null) {
            z = fr.isDate();
        }
        return z;
    }

    public final DateFormat getDateFormat(int pos) {
        DateFormat dateFormat = null;
        XFRecord xfr = (XFRecord) this.xfRecords.get(pos);
        if (xfr.isDate()) {
            return xfr.getDateFormat();
        }
        FormatRecord fr = (FormatRecord) this.formats.get(new Integer(xfr.getFormatRecord()));
        if (fr == null) {
            return null;
        }
        if (fr.isDate()) {
            dateFormat = fr.getDateFormat();
        }
        return dateFormat;
    }

    public final NumberFormat getNumberFormat(int pos) {
        NumberFormat numberFormat = null;
        XFRecord xfr = (XFRecord) this.xfRecords.get(pos);
        if (xfr.isNumber()) {
            return xfr.getNumberFormat();
        }
        FormatRecord fr = (FormatRecord) this.formats.get(new Integer(xfr.getFormatRecord()));
        if (fr == null) {
            return null;
        }
        if (fr.isNumber()) {
            numberFormat = fr.getNumberFormat();
        }
        return numberFormat;
    }

    FormatRecord getFormatRecord(int index) {
        return (FormatRecord) this.formats.get(new Integer(index));
    }

    public void write(File outputFile) throws IOException {
        Iterator i = this.formatsList.iterator();
        while (i.hasNext()) {
            outputFile.write((FormatRecord) i.next());
        }
        i = this.xfRecords.iterator();
        while (i.hasNext()) {
            outputFile.write((XFRecord) i.next());
        }
        outputFile.write(new BuiltInStyle(16, 3));
        outputFile.write(new BuiltInStyle(17, 6));
        outputFile.write(new BuiltInStyle(18, 4));
        outputFile.write(new BuiltInStyle(19, 7));
        outputFile.write(new BuiltInStyle(0, 0));
        outputFile.write(new BuiltInStyle(20, 5));
    }

    protected final Fonts getFonts() {
        return this.fonts;
    }

    public final XFRecord getXFRecord(int index) {
        return (XFRecord) this.xfRecords.get(index);
    }

    public IndexMapping rationalizeFonts() {
        return this.fonts.rationalize();
    }

    public IndexMapping rationalize(IndexMapping fontMapping, IndexMapping formatMapping) {
        int i;
        Iterator it = this.xfRecords.iterator();
        while (it.hasNext()) {
            XFRecord xfr = (XFRecord) it.next();
            if (xfr.getFormatRecord() >= 164) {
                xfr.setFormatIndex(formatMapping.getNewIndex(xfr.getFormatRecord()));
            }
            xfr.setFontIndex(fontMapping.getNewIndex(xfr.getFontIndex()));
        }
        ArrayList newrecords = new ArrayList(21);
        IndexMapping mapping = new IndexMapping(this.xfRecords.size());
        int numremoved = 0;
        int numXFRecords = Math.min(21, this.xfRecords.size());
        for (i = 0; i < numXFRecords; i++) {
            newrecords.add(this.xfRecords.get(i));
            mapping.setMapping(i, i);
        }
        if (numXFRecords >= 21) {
            for (i = 21; i < this.xfRecords.size(); i++) {
                XFRecord xf = (XFRecord) this.xfRecords.get(i);
                boolean duplicate = false;
                it = newrecords.iterator();
                while (it.hasNext() && !duplicate) {
                    XFRecord xf2 = (XFRecord) it.next();
                    if (xf2.equals(xf)) {
                        duplicate = true;
                        mapping.setMapping(i, mapping.getNewIndex(xf2.getXFIndex()));
                        numremoved++;
                    }
                }
                if (!duplicate) {
                    newrecords.add(xf);
                    mapping.setMapping(i, i - numremoved);
                }
            }
            Iterator i2 = this.xfRecords.iterator();
            while (i2.hasNext()) {
                ((XFRecord) i2.next()).rationalize(mapping);
            }
            this.xfRecords = newrecords;
            return mapping;
        }
        logger.warn("There are less than the expected minimum number of XF records");
        return mapping;
    }

    public IndexMapping rationalizeDisplayFormats() {
        ArrayList newformats = new ArrayList();
        int numremoved = 0;
        IndexMapping mapping = new IndexMapping(this.nextCustomIndexNumber);
        Iterator i = this.formatsList.iterator();
        while (i.hasNext()) {
            boolean z;
            DisplayFormat df = (DisplayFormat) i.next();
            if (df.isBuiltIn()) {
                z = false;
            } else {
                z = true;
            }
            Assert.verify(z);
            Iterator i2 = newformats.iterator();
            boolean duplicate = false;
            while (i2.hasNext() && !duplicate) {
                DisplayFormat df2 = (DisplayFormat) i2.next();
                if (df2.equals(df)) {
                    duplicate = true;
                    mapping.setMapping(df.getFormatIndex(), mapping.getNewIndex(df2.getFormatIndex()));
                    numremoved++;
                }
            }
            if (!duplicate) {
                newformats.add(df);
                if (df.getFormatIndex() - numremoved > 441) {
                    logger.warn("Too many number formats - using default format.");
                }
                mapping.setMapping(df.getFormatIndex(), df.getFormatIndex() - numremoved);
            }
        }
        this.formatsList = newformats;
        i = this.formatsList.iterator();
        while (i.hasNext()) {
            df = (DisplayFormat) i.next();
            df.initialize(mapping.getNewIndex(df.getFormatIndex()));
        }
        return mapping;
    }

    public PaletteRecord getPalette() {
        return this.palette;
    }

    public void setPalette(PaletteRecord pr) {
        this.palette = pr;
    }
}
