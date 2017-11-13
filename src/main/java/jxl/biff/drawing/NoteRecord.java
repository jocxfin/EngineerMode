package jxl.biff.drawing;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;
import jxl.read.biff.Record;

public class NoteRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(NoteRecord.class);
    private int column;
    private byte[] data;
    private int objectId;
    private int row;

    public NoteRecord(Record t) {
        super(t);
        this.data = getRecord().getData();
        this.row = IntegerHelper.getInt(this.data[0], this.data[1]);
        this.column = IntegerHelper.getInt(this.data[2], this.data[3]);
        this.objectId = IntegerHelper.getInt(this.data[6], this.data[7]);
    }

    public NoteRecord(int c, int r, int id) {
        super(Type.NOTE);
        this.row = r;
        this.column = c;
        this.objectId = id;
    }

    public byte[] getData() {
        if (this.data != null) {
            return this.data;
        }
        String author = "";
        this.data = new byte[((author.length() + 8) + 4)];
        IntegerHelper.getTwoBytes(this.row, this.data, 0);
        IntegerHelper.getTwoBytes(this.column, this.data, 2);
        IntegerHelper.getTwoBytes(this.objectId, this.data, 6);
        IntegerHelper.getTwoBytes(author.length(), this.data, 8);
        return this.data;
    }

    int getRow() {
        return this.row;
    }

    int getColumn() {
        return this.column;
    }

    public int getObjectId() {
        return this.objectId;
    }
}
