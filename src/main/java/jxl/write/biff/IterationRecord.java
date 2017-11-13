package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class IterationRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean iterate;

    public IterationRecord(boolean it) {
        super(Type.ITERATION);
        this.iterate = it;
        if (this.iterate) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
