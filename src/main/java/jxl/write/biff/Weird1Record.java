package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Weird1Record extends WritableRecordData {
    public Weird1Record() {
        super(Type.WEIRD1);
    }

    public byte[] getData() {
        byte[] data = new byte[6];
        data[2] = (byte) 55;
        return data;
    }
}
