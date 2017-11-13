package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Prot4RevPassRecord extends WritableRecordData {
    private byte[] data = new byte[2];

    public Prot4RevPassRecord() {
        super(Type.PROT4REVPASS);
    }

    public byte[] getData() {
        return this.data;
    }
}
