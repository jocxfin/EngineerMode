package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class GuttersRecord extends WritableRecordData {
    private int colGutter;
    private byte[] data;
    private int maxColumnOutline;
    private int maxRowOutline;
    private int rowGutter;

    public GuttersRecord() {
        super(Type.GUTS);
    }

    public byte[] getData() {
        this.data = new byte[8];
        IntegerHelper.getTwoBytes(this.rowGutter, this.data, 0);
        IntegerHelper.getTwoBytes(this.colGutter, this.data, 2);
        IntegerHelper.getTwoBytes(this.maxRowOutline, this.data, 4);
        IntegerHelper.getTwoBytes(this.maxColumnOutline, this.data, 6);
        return this.data;
    }

    public void setMaxRowOutline(int value) {
        this.maxRowOutline = value;
        this.rowGutter = (value * 14) + 1;
    }

    public void setMaxColumnOutline(int value) {
        this.maxColumnOutline = value;
        this.colGutter = (value * 14) + 1;
    }
}
