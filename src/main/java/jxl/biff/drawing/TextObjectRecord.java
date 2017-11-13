package jxl.biff.drawing;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;
import jxl.read.biff.Record;

public class TextObjectRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(TextObjectRecord.class);
    private byte[] data;
    private int textLength;

    TextObjectRecord(String t) {
        super(Type.TXO);
        this.textLength = t.length();
    }

    public TextObjectRecord(Record t) {
        super(t);
        this.data = getRecord().getData();
        this.textLength = IntegerHelper.getInt(this.data[10], this.data[11]);
    }

    public int getTextLength() {
        return this.textLength;
    }

    public byte[] getData() {
        if (this.data != null) {
            return this.data;
        }
        this.data = new byte[18];
        IntegerHelper.getTwoBytes((2 | 16) | 512, this.data, 0);
        IntegerHelper.getTwoBytes(this.textLength, this.data, 10);
        IntegerHelper.getTwoBytes(16, this.data, 12);
        return this.data;
    }
}
