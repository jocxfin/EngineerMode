package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class DefaultColumnWidth extends WritableRecordData {
    private byte[] data = new byte[2];
    private int width;

    public DefaultColumnWidth(int w) {
        super(Type.DEFCOLWIDTH);
        this.width = w;
        IntegerHelper.getTwoBytes(this.width, this.data, 0);
    }

    protected byte[] getData() {
        return this.data;
    }
}
