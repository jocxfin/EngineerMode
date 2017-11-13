package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Prot4RevRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean protection;

    public Prot4RevRecord(boolean prot) {
        super(Type.PROT4REV);
        this.protection = prot;
        if (this.protection) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
