package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class FunctionGroupCountRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private int numFunctionGroups = 14;

    public FunctionGroupCountRecord() {
        super(Type.FNGROUPCOUNT);
        IntegerHelper.getTwoBytes(this.numFunctionGroups, this.data, 0);
    }

    public byte[] getData() {
        return this.data;
    }
}
