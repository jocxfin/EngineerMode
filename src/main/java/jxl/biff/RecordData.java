package jxl.biff;

import jxl.read.biff.Record;

public abstract class RecordData {
    private int code;
    private Record record;

    protected RecordData(Record r) {
        this.record = r;
        this.code = r.getCode();
    }

    protected RecordData(Type t) {
        this.code = t.value;
    }

    protected Record getRecord() {
        return this.record;
    }

    protected final int getCode() {
        return this.code;
    }
}
