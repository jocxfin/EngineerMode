package jxl.biff.drawing;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;
import jxl.read.biff.Record;

public class MsoDrawingRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(MsoDrawingRecord.class);
    private byte[] data;
    private boolean first;

    public MsoDrawingRecord(Record t) {
        super(t);
        this.data = getRecord().getData();
        this.first = false;
    }

    public MsoDrawingRecord(byte[] d) {
        super(Type.MSODRAWING);
        this.data = d;
        this.first = false;
    }

    public byte[] getData() {
        return this.data;
    }

    public Record getRecord() {
        return super.getRecord();
    }

    public void setFirst() {
        this.first = true;
    }

    public boolean isFirst() {
        return this.first;
    }
}
