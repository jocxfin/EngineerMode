package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class VerticalCentreRecord extends WritableRecordData {
    private boolean centre;
    private byte[] data = new byte[2];

    public VerticalCentreRecord(boolean ce) {
        super(Type.VCENTER);
        this.centre = ce;
        if (this.centre) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
