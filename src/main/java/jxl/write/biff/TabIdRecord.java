package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class TabIdRecord extends WritableRecordData {
    private byte[] data;

    public TabIdRecord(int sheets) {
        super(Type.TABID);
        this.data = new byte[(sheets * 2)];
        for (int i = 0; i < sheets; i++) {
            IntegerHelper.getTwoBytes(i + 1, this.data, i * 2);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
