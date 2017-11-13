package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class HideobjRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private int hidemode;

    public HideobjRecord(int newHideMode) {
        super(Type.HIDEOBJ);
        this.hidemode = newHideMode;
        IntegerHelper.getTwoBytes(this.hidemode, this.data, 0);
    }

    public byte[] getData() {
        return this.data;
    }
}
