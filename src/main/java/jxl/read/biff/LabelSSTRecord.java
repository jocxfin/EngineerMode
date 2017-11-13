package jxl.read.biff;

import jxl.CellType;
import jxl.LabelCell;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;

class LabelSSTRecord extends CellValue implements LabelCell {
    private int index;
    private String string;

    public LabelSSTRecord(Record t, SSTRecord stringTable, FormattingRecords fr, SheetImpl si) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        this.index = IntegerHelper.getInt(data[6], data[7], data[8], data[9]);
        this.string = stringTable.getString(this.index);
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
