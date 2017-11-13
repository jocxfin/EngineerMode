package jxl.read.biff;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jxl.CellType;
import jxl.NumberCell;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class RKRecord extends CellValue implements NumberCell {
    private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
    private static Logger logger = Logger.getLogger(RKRecord.class);
    private NumberFormat format;
    private double value;

    public RKRecord(Record t, FormattingRecords fr, SheetImpl si) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        this.value = RKHelper.getDouble(IntegerHelper.getInt(data[6], data[7], data[8], data[9]));
        this.format = fr.getNumberFormat(getXFIndex());
        if (this.format == null) {
            this.format = defaultFormat;
        }
    }

    public double getValue() {
        return this.value;
    }

    public String getContents() {
        return this.format.format(this.value);
    }

    public CellType getType() {
        return CellType.NUMBER;
    }
}
