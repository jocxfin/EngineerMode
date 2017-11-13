package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class GridSetRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean gridSet;

    public GridSetRecord(boolean gs) {
        super(Type.GRIDSET);
        this.gridSet = gs;
        if (this.gridSet) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
