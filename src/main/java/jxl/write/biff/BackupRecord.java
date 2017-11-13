package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class BackupRecord extends WritableRecordData {
    private boolean backup;
    private byte[] data = new byte[2];

    public BackupRecord(boolean bu) {
        super(Type.BACKUP);
        this.backup = bu;
        if (this.backup) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
