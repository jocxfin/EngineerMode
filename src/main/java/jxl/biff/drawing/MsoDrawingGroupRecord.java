package jxl.biff.drawing;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.read.biff.Record;

public class MsoDrawingGroupRecord extends WritableRecordData {
    private byte[] data;

    public MsoDrawingGroupRecord(Record t) {
        super(t);
        this.data = t.getData();
    }

    MsoDrawingGroupRecord(byte[] d) {
        super(Type.MSODRAWINGGROUP);
        this.data = d;
    }

    public byte[] getData() {
        return this.data;
    }
}
