package jxl.write.biff;

import jxl.biff.DoubleHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

abstract class MarginRecord extends WritableRecordData {
    private double margin;

    public MarginRecord(Type t, double v) {
        super(t);
        this.margin = v;
    }

    public byte[] getData() {
        byte[] data = new byte[8];
        DoubleHelper.getIEEEBytes(this.margin, data, 0);
        return data;
    }
}
