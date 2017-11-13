package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ButtonPropertySetRecord extends WritableRecordData {
    private byte[] data;

    public ButtonPropertySetRecord(jxl.read.biff.ButtonPropertySetRecord bps) {
        super(Type.BUTTONPROPERTYSET);
        this.data = bps.getData();
    }

    public byte[] getData() {
        return this.data;
    }
}
