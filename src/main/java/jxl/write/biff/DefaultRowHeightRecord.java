package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class DefaultRowHeightRecord extends WritableRecordData {
    private boolean changed;
    private byte[] data = new byte[4];
    private int rowHeight;

    public DefaultRowHeightRecord(int h, boolean ch) {
        super(Type.DEFAULTROWHEIGHT);
        this.rowHeight = h;
        this.changed = ch;
    }

    public byte[] getData() {
        if (this.changed) {
            byte[] bArr = this.data;
            bArr[0] = (byte) ((byte) (bArr[0] | 1));
        }
        IntegerHelper.getTwoBytes(this.rowHeight, this.data, 2);
        return this.data;
    }
}
