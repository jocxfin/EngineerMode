package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class PrecisionRecord extends WritableRecordData {
    private boolean asDisplayed;
    private byte[] data = new byte[2];

    public PrecisionRecord(boolean disp) {
        super(Type.PRECISION);
        this.asDisplayed = disp;
        if (!this.asDisplayed) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
