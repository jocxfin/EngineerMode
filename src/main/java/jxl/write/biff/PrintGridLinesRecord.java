package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class PrintGridLinesRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean printGridLines;

    public PrintGridLinesRecord(boolean pgl) {
        super(Type.PRINTGRIDLINES);
        this.printGridLines = pgl;
        if (this.printGridLines) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
