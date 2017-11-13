package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class SCLRecord extends WritableRecordData {
    private int zoomFactor;

    public SCLRecord(int zf) {
        super(Type.SCL);
        this.zoomFactor = zf;
    }

    public byte[] getData() {
        byte[] data = new byte[4];
        IntegerHelper.getTwoBytes(this.zoomFactor, data, 0);
        IntegerHelper.getTwoBytes(100, data, 2);
        return data;
    }
}
