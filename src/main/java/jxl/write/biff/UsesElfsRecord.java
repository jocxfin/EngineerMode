package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class UsesElfsRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean usesElfs = true;

    public UsesElfsRecord() {
        super(Type.USESELFS);
        if (this.usesElfs) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
