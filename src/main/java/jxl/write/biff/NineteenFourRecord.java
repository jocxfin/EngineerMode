package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class NineteenFourRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean nineteenFourDate;

    public NineteenFourRecord(boolean oldDate) {
        super(Type.NINETEENFOUR);
        this.nineteenFourDate = oldDate;
        if (this.nineteenFourDate) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
