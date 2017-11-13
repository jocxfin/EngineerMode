package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class AutoFilterRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(AutoFilterRecord.class);
    private byte[] data = getRecord().getData();

    public AutoFilterRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return this.data;
    }
}
