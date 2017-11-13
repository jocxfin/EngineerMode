package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ObjectProtectRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean protection;

    public ObjectProtectRecord(boolean prot) {
        super(Type.OBJPROTECT);
        this.protection = prot;
        if (this.protection) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
