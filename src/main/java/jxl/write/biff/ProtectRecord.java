package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ProtectRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean protection;

    public ProtectRecord(boolean prot) {
        super(Type.PROTECT);
        this.protection = prot;
        if (this.protection) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
