package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;
import jxl.common.Logger;

public class SupbookRecord extends RecordData {
    public static final Type ADDIN = new Type();
    public static final Type EXTERNAL = new Type();
    public static final Type INTERNAL = new Type();
    public static final Type LINK = new Type();
    public static final Type UNKNOWN = new Type();
    private static Logger logger = Logger.getLogger(SupbookRecord.class);
    private String fileName;
    private int numSheets;
    private String[] sheetNames;
    private Type type;

    private static class Type {
        private Type() {
        }
    }

    SupbookRecord(Record t, WorkbookSettings ws) {
        super(t);
        byte[] data = getRecord().getData();
        if (data.length != 4) {
            if (data[0] == (byte) 0 && data[1] == (byte) 0) {
                this.type = LINK;
            } else {
                this.type = EXTERNAL;
            }
        } else if (data[2] == (byte) 1 && data[3] == (byte) 4) {
            this.type = INTERNAL;
        } else if (data[2] == (byte) 1 && data[3] == (byte) 58) {
            this.type = ADDIN;
        } else {
            this.type = UNKNOWN;
        }
        if (this.type == INTERNAL) {
            this.numSheets = IntegerHelper.getInt(data[0], data[1]);
        }
        if (this.type == EXTERNAL) {
            readExternal(data, ws);
        }
    }

    private void readExternal(byte[] data, WorkbookSettings ws) {
        int pos;
        this.numSheets = IntegerHelper.getInt(data[0], data[1]);
        int ln = IntegerHelper.getInt(data[2], data[3]) - 1;
        if (data[4] != (byte) 0) {
            if (IntegerHelper.getInt(data[5], data[6]) != 0) {
                this.fileName = getUnicodeEncodedFilename(data, ln, 7);
                pos = (ln * 2) + 7;
            } else {
                this.fileName = StringHelper.getUnicodeString(data, ln, 7);
                pos = (ln * 2) + 7;
            }
        } else if (data[5] != 0) {
            this.fileName = getEncodedFilename(data, ln, 6);
            pos = ln + 6;
        } else {
            this.fileName = StringHelper.getString(data, ln, 6, ws);
            pos = ln + 6;
        }
        this.sheetNames = new String[this.numSheets];
        for (int i = 0; i < this.sheetNames.length; i++) {
            ln = IntegerHelper.getInt(data[pos], data[pos + 1]);
            if (data[pos + 2] == (byte) 0) {
                this.sheetNames[i] = StringHelper.getString(data, ln, pos + 3, ws);
                pos += ln + 3;
            } else if (data[pos + 2] == (byte) 1) {
                this.sheetNames[i] = StringHelper.getUnicodeString(data, ln, pos + 3);
                pos += (ln * 2) + 3;
            }
        }
    }

    public Type getType() {
        return this.type;
    }

    public int getNumberOfSheets() {
        return this.numSheets;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getSheetName(int i) {
        return this.sheetNames[i];
    }

    private String getEncodedFilename(byte[] data, int ln, int pos) {
        StringBuffer buf = new StringBuffer();
        int endpos = pos + ln;
        while (pos < endpos) {
            char c = (char) data[pos];
            if (c == '\u0001') {
                pos++;
                buf.append((char) data[pos]);
                buf.append(":\\\\");
            } else if (c == '\u0002') {
                buf.append('\\');
            } else if (c == '\u0003') {
                buf.append('\\');
            } else if (c != '\u0004') {
                buf.append(c);
            } else {
                buf.append("..\\");
            }
            pos++;
        }
        return buf.toString();
    }

    private String getUnicodeEncodedFilename(byte[] data, int ln, int pos) {
        StringBuffer buf = new StringBuffer();
        int endpos = pos + (ln * 2);
        while (pos < endpos) {
            char c = (char) IntegerHelper.getInt(data[pos], data[pos + 1]);
            if (c == '\u0001') {
                pos += 2;
                buf.append((char) IntegerHelper.getInt(data[pos], data[pos + 1]));
                buf.append(":\\\\");
            } else if (c == '\u0002') {
                buf.append('\\');
            } else if (c == '\u0003') {
                buf.append('\\');
            } else if (c != '\u0004') {
                buf.append(c);
            } else {
                buf.append("..\\");
            }
            pos += 2;
        }
        return buf.toString();
    }
}
