package jxl.biff;

import jxl.read.biff.Record;

public class ContinueRecord extends WritableRecordData {
    private byte[] data;

    public ContinueRecord(Record t) {
        super(t);
        this.data = t.getData();
    }

    public ContinueRecord(byte[] d) {
        super(Type.CONTINUE);
        this.data = d;
    }

    public byte[] getData() {
        return this.data;
    }

    public Record getRecord() {
        return super.getRecord();
    }
}
