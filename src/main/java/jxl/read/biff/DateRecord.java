package jxl.read.biff;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jxl.CellFeatures;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.biff.FormattingRecords;
import jxl.common.Logger;
import jxl.format.CellFormat;

class DateRecord implements DateCell, CellFeaturesAccessor {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");
    private static Logger logger = Logger.getLogger(DateRecord.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private CellFormat cellFormat;
    private int column;
    private Date date;
    private CellFeatures features;
    private DateFormat format = this.formattingRecords.getDateFormat(this.xfIndex);
    private FormattingRecords formattingRecords;
    private boolean initialized = false;
    private int row;
    private SheetImpl sheet;
    private boolean time;
    private int xfIndex;

    public DateRecord(NumberCell num, int xfi, FormattingRecords fr, boolean nf, SheetImpl si) {
        int offsetDays;
        this.row = num.getRow();
        this.column = num.getColumn();
        this.xfIndex = xfi;
        this.formattingRecords = fr;
        this.sheet = si;
        double numValue = num.getValue();
        if (Math.abs(numValue) < 1.0d) {
            if (this.format == null) {
                this.format = timeFormat;
            }
            this.time = true;
        } else {
            if (this.format == null) {
                this.format = dateFormat;
            }
            this.time = false;
        }
        if (!(nf || this.time || numValue >= 61.0d)) {
            numValue += 1.0d;
        }
        this.format.setTimeZone(gmtZone);
        if (nf) {
            offsetDays = 24107;
        } else {
            offsetDays = 25569;
        }
        this.date = new Date(Math.round(86400.0d * (numValue - ((double) offsetDays))) * 1000);
    }

    public final int getRow() {
        return this.row;
    }

    public final int getColumn() {
        return this.column;
    }

    public Date getDate() {
        return this.date;
    }

    public String getContents() {
        return this.format.format(this.date);
    }

    public CellType getType() {
        return CellType.DATE;
    }

    public boolean isTime() {
        return this.time;
    }

    public CellFormat getCellFormat() {
        if (!this.initialized) {
            this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
            this.initialized = true;
        }
        return this.cellFormat;
    }

    protected final SheetImpl getSheet() {
        return this.sheet;
    }

    public CellFeatures getCellFeatures() {
        return this.features;
    }

    public void setCellFeatures(CellFeatures cf) {
        this.features = cf;
    }
}
