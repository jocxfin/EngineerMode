package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class SaveRecalcRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean recalc;

    public SaveRecalcRecord(boolean r) {
        super(Type.SAVERECALC);
        this.recalc = r;
        if (this.recalc) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
