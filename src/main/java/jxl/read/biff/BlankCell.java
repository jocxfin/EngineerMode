package jxl.read.biff;

import jxl.CellType;
import jxl.biff.FormattingRecords;

public class BlankCell extends CellValue {
    BlankCell(Record t, FormattingRecords fr, SheetImpl si) {
        super(t, fr, si);
    }

    public String getContents() {
        return "";
    }

    public CellType getType() {
        return CellType.EMPTY;
    }
}
