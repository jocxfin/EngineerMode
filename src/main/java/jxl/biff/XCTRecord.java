package jxl.biff;

import jxl.read.biff.Record;

public class XCTRecord extends WritableRecordData {
    public XCTRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return getRecord().getData();
    }
}
