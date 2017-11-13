package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class RefModeRecord extends WritableRecordData {
    public RefModeRecord() {
        super(Type.REFMODE);
    }

    public byte[] getData() {
        byte[] data = new byte[2];
        data[0] = (byte) 1;
        return data;
    }
}
