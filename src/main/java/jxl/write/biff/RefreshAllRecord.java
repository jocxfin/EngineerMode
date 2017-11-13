package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class RefreshAllRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean refreshall;

    public RefreshAllRecord(boolean refresh) {
        super(Type.REFRESHALL);
        this.refreshall = refresh;
        if (this.refreshall) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
