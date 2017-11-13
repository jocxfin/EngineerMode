package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class AutoFilterInfoRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(AutoFilterInfoRecord.class);
    private byte[] data = getRecord().getData();

    public AutoFilterInfoRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return this.data;
    }
}
