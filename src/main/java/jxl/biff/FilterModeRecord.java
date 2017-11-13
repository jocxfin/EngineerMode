package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class FilterModeRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(FilterModeRecord.class);
    private byte[] data = getRecord().getData();

    public FilterModeRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return this.data;
    }
}
