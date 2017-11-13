package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class InterfaceHeaderRecord extends WritableRecordData {
    public InterfaceHeaderRecord() {
        super(Type.INTERFACEHDR);
    }

    public byte[] getData() {
        return new byte[]{(byte) -80, (byte) 4};
    }
}
