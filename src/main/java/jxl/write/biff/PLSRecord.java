package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class PLSRecord extends WritableRecordData {
    private byte[] data;

    public PLSRecord(jxl.read.biff.PLSRecord hr) {
        super(Type.PLS);
        this.data = hr.getData();
    }

    public byte[] getData() {
        return this.data;
    }
}
