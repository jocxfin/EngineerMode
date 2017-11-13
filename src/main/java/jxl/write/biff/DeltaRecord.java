package jxl.write.biff;

import jxl.biff.DoubleHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class DeltaRecord extends WritableRecordData {
    private byte[] data = new byte[8];
    private double iterationValue;

    public DeltaRecord(double itval) {
        super(Type.DELTA);
        this.iterationValue = itval;
    }

    public byte[] getData() {
        DoubleHelper.getIEEEBytes(this.iterationValue, this.data, 0);
        return this.data;
    }
}
