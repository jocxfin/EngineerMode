package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class HorizontalCentreRecord extends WritableRecordData {
    private boolean centre;
    private byte[] data = new byte[2];

    public HorizontalCentreRecord(boolean ce) {
        super(Type.HCENTER);
        this.centre = ce;
        if (this.centre) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
