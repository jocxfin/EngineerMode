package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class ConditionalFormatRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(ConditionalFormatRecord.class);
    private byte[] data = getRecord().getData();

    public ConditionalFormatRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return this.data;
    }
}
