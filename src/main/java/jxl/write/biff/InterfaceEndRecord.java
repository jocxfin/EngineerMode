package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class InterfaceEndRecord extends WritableRecordData {
    public InterfaceEndRecord() {
        super(Type.INTERFACEEND);
    }

    public byte[] getData() {
        return new byte[0];
    }
}
