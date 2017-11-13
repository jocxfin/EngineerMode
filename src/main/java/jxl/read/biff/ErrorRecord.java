package jxl.read.biff;

import jxl.CellType;
import jxl.ErrorCell;
import jxl.biff.FormattingRecords;

class ErrorRecord extends CellValue implements ErrorCell {
    private int errorCode = getRecord().getData()[6];

    public ErrorRecord(Record t, FormattingRecords fr, SheetImpl si) {
        super(t, fr, si);
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getContents() {
        return "ERROR " + this.errorCode;
    }

    public CellType getType() {
        return CellType.ERROR;
    }
}
