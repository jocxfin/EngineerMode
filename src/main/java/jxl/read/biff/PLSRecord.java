package jxl.read.biff;

import jxl.biff.RecordData;

public class PLSRecord extends RecordData {
    public PLSRecord(Record r) {
        super(r);
    }

    public byte[] getData() {
        return getRecord().getData();
    }
}
