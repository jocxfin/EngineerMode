package jxl.read.biff;

import jxl.biff.DoubleHelper;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.Type;
import jxl.common.Logger;

public class SetupRecord extends RecordData {
    private static Logger logger = Logger.getLogger(SetupRecord.class);
    private int copies = IntegerHelper.getInt(this.data[32], this.data[33]);
    private byte[] data;
    private int fitHeight = IntegerHelper.getInt(this.data[8], this.data[9]);
    private int fitWidth = IntegerHelper.getInt(this.data[6], this.data[7]);
    private double footerMargin = DoubleHelper.getIEEEDouble(this.data, 24);
    private double headerMargin = DoubleHelper.getIEEEDouble(this.data, 16);
    private int horizontalPrintResolution = IntegerHelper.getInt(this.data[12], this.data[13]);
    private boolean initialized;
    private boolean pageOrder;
    private int pageStart = IntegerHelper.getInt(this.data[4], this.data[5]);
    private int paperSize = IntegerHelper.getInt(this.data[0], this.data[1]);
    private boolean portraitOrientation;
    private int scaleFactor = IntegerHelper.getInt(this.data[2], this.data[3]);
    private int verticalPrintResolution = IntegerHelper.getInt(this.data[14], this.data[15]);

    SetupRecord(Record t) {
        boolean z;
        boolean z2 = false;
        super(Type.SETUP);
        this.data = t.getData();
        int grbit = IntegerHelper.getInt(this.data[10], this.data[11]);
        if ((grbit & 1) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.pageOrder = z;
        if ((grbit & 2) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.portraitOrientation = z;
        if ((grbit & 4) == 0) {
            z2 = true;
        }
        this.initialized = z2;
    }

    public boolean isPortrait() {
        return this.portraitOrientation;
    }

    public boolean isRightDown() {
        return this.pageOrder;
    }

    public double getHeaderMargin() {
        return this.headerMargin;
    }

    public double getFooterMargin() {
        return this.footerMargin;
    }

    public int getPaperSize() {
        return this.paperSize;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }

    public int getPageStart() {
        return this.pageStart;
    }

    public int getFitWidth() {
        return this.fitWidth;
    }

    public int getFitHeight() {
        return this.fitHeight;
    }

    public int getHorizontalPrintResolution() {
        return this.horizontalPrintResolution;
    }

    public int getVerticalPrintResolution() {
        return this.verticalPrintResolution;
    }

    public int getCopies() {
        return this.copies;
    }

    public boolean getInitialized() {
        return this.initialized;
    }
}
