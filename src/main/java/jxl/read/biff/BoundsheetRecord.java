package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;

class BoundsheetRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private int length;
    private String name;
    private int offset;
    private byte typeFlag;
    private byte visibilityFlag;

    private static class Biff7 {
        private Biff7() {
        }
    }

    public BoundsheetRecord(Record t, WorkbookSettings s) {
        super(t);
        byte[] data = getRecord().getData();
        this.offset = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
        this.typeFlag = (byte) data[5];
        this.visibilityFlag = (byte) data[4];
        this.length = data[6];
        if (data[7] != (byte) 0) {
            byte[] bytes = new byte[(this.length * 2)];
            System.arraycopy(data, 8, bytes, 0, this.length * 2);
            this.name = StringHelper.getUnicodeString(bytes, this.length, 0);
            return;
        }
        bytes = new byte[this.length];
        System.arraycopy(data, 8, bytes, 0, this.length);
        this.name = StringHelper.getString(bytes, this.length, 0, s);
    }

    public BoundsheetRecord(Record t, Biff7 biff7) {
        super(t);
        byte[] data = getRecord().getData();
        this.offset = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
        this.typeFlag = (byte) data[5];
        this.visibilityFlag = (byte) data[4];
        this.length = data[6];
        byte[] bytes = new byte[this.length];
        System.arraycopy(data, 7, bytes, 0, this.length);
        this.name = new String(bytes);
    }

    public String getName() {
        return this.name;
    }

    public boolean isHidden() {
        return this.visibilityFlag != (byte) 0;
    }

    public boolean isSheet() {
        return this.typeFlag == (byte) 0;
    }

    public boolean isChart() {
        return this.typeFlag == (byte) 2;
    }
}
