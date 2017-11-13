package jxl.read.biff;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jxl.CellType;
import jxl.NumberCell;
import jxl.biff.DoubleHelper;
import jxl.biff.FormattingRecords;
import jxl.common.Logger;

class NumberRecord extends CellValue implements NumberCell {
    private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
    private static Logger logger = Logger.getLogger(NumberRecord.class);
    private NumberFormat format;
    private double value = DoubleHelper.getIEEEDouble(getRecord().getData(), 6);

    public NumberRecord(Record t, FormattingRecords fr, SheetImpl si) {
        super(t, fr, si);
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
