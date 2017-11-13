package jxl.read.biff;

import jxl.CellType;
import jxl.LabelCell;
import jxl.WorkbookSettings;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;

class LabelRecord extends CellValue implements LabelCell {
    public static Biff7 biff7 = new Biff7();
    private int length;
    private String string;

    private static class Biff7 {
        private Biff7() {
        }
    }

    public LabelRecord(Record t, FormattingRecords fr, SheetImpl si, WorkbookSettings ws) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        this.length = IntegerHelper.getInt(data[6], data[7]);
        if (data[8] != (byte) 0) {
            this.string = StringHelper.getUnicodeString(data, this.length, 9);
        } else {
            this.string = StringHelper.getString(data, this.length, 9, ws);
        }
    }

    public LabelRecord(Record t, FormattingRecords fr, SheetImpl si, WorkbookSettings ws, Biff7 dummy) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        this.length = IntegerHelper.getInt(data[6], data[7]);
        this.string = StringHelper.getString(data, this.length, 8, ws);
    }

    public String getString() {
        return this.string;
    }

    public String getContents() {
        return this.string;
    }

    public CellType getType() {
        return CellType.LABEL;
    }
}
