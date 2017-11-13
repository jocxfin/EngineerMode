package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class PrintHeadersRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean printHeaders;

    public PrintHeadersRecord(boolean ph) {
        super(Type.PRINTHEADERS);
        this.printHeaders = ph;
        if (this.printHeaders) {
            this.data[0] = (byte) 1;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
