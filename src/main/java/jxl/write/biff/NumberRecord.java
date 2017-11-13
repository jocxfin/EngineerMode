package jxl.write.biff;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jxl.CellType;
import jxl.NumberCell;
import jxl.biff.DoubleHelper;
import jxl.biff.Type;
import jxl.biff.XFRecord;

public abstract class NumberRecord extends CellValue {
    private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
    private NumberFormat format;
    private double value;

    protected NumberRecord(int c, int r, double val) {
        super(Type.NUMBER, c, r);
        this.value = val;
    }

    protected NumberRecord(NumberCell nc) {
        super(Type.NUMBER, nc);
        this.value = nc.getValue();
    }

    public CellType getType() {
        return CellType.NUMBER;
    }

    public byte[] getData() {
        byte[] celldata = super.getData();
        byte[] data = new byte[(celldata.length + 8)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        DoubleHelper.getIEEEBytes(this.value, data, celldata.length);
        return data;
    }

    public String getContents() {
        if (this.format == null) {
            this.format = ((XFRecord) getCellFormat()).getNumberFormat();
            if (this.format == null) {
                this.format = defaultFormat;
            }
        }
        return this.format.format(this.value);
    }

    public double getValue() {
        return this.value;
    }
}
