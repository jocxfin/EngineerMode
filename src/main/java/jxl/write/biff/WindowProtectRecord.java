package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class WindowProtectRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean protection;

    public WindowProtectRecord(boolean prot) {
        super(Type.WINDOWPROTECT);
        this.protection = prot;
        if (this.protection) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
