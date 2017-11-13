package jxl.read.biff;

import java.util.ArrayList;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.common.Logger;

public final class Record {
    private static final Logger logger = Logger.getLogger(Record.class);
    private int code;
    private ArrayList continueRecords;
    private byte[] data;
    private int dataPos;
    private File file;
    private int length;
    private Type type = Type.getType(this.code);

    Record(byte[] d, int offset, File f) {
        this.code = IntegerHelper.getInt(d[offset], d[offset + 1]);
        this.length = IntegerHelper.getInt(d[offset + 2], d[offset + 3]);
        this.file = f;
        this.file.skip(4);
        this.dataPos = f.getPos();
        this.file.skip(this.length);
    }

    public Type getType() {
        return this.type;
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getData() {
        if (this.data == null) {
            this.data = this.file.read(this.dataPos, this.length);
        }
        if (this.continueRecords != null) {
            int i;
            int size = 0;
            byte[][] contData = new byte[this.continueRecords.size()][];
            for (i = 0; i < this.continueRecords.size(); i++) {
                contData[i] = ((Record) this.continueRecords.get(i)).getData();
                size += contData[i].length;
            }
            byte[] d3 = new byte[(this.data.length + size)];
            System.arraycopy(this.data, 0, d3, 0, this.data.length);
            int pos = this.data.length;
            for (byte[] d2 : contData) {
                System.arraycopy(d2, 0, d3, pos, d2.length);
                pos += d2.length;
            }
            this.data = d3;
        }
        return this.data;
    }

    public int getCode() {
        return this.code;
    }

    void setType(Type t) {
        this.type = t;
    }

    public void addContinueRecord(Record d) {
        if (this.continueRecords == null) {
            this.continueRecords = new ArrayList();
        }
        this.continueRecords.add(d);
    }
}
