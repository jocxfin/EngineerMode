package jxl.read.biff;

import jxl.BooleanCell;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.common.Assert;

class BooleanRecord extends CellValue implements BooleanCell {
    private boolean error = false;
    private boolean value = false;

    public BooleanRecord(Record t, FormattingRecords fr, SheetImpl si) {
        boolean z = false;
        super(t, fr, si);
        byte[] data = getRecord().getData();
        this.error = data[7] == (byte) 1;
        if (!this.error) {
            if (data[6] == (byte) 1) {
                z = true;
            }
            this.value = z;
        }
    }

    public boolean isError() {
        return this.error;
    }

    public boolean getValue() {
        return this.value;
    }

    public String getContents() {
        boolean z = false;
        if (!isError()) {
            z = true;
        }
        Assert.verify(z);
        return new Boolean(this.value).toString();
    }

    public CellType getType() {
        return CellType.BOOLEAN;
    }

    public Record getRecord() {
        return super.getRecord();
    }
}
