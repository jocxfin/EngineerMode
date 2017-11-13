package jxl.read.biff;

import jxl.BooleanCell;
import jxl.BooleanFormulaCell;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;
import jxl.common.Assert;

class BooleanFormulaRecord extends CellValue implements BooleanCell, FormulaData, BooleanFormulaCell {
    private byte[] data = getRecord().getData();
    private ExternalSheet externalSheet;
    private WorkbookMethods nameTable;
    private boolean value = false;

    public BooleanFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        boolean z;
        boolean z2 = false;
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        if (this.data[6] == (byte) 2) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        if (this.data[8] == (byte) 1) {
            z2 = true;
        }
        this.value = z2;
    }

    public boolean getValue() {
        return this.value;
    }

    public String getContents() {
        return new Boolean(this.value).toString();
    }

    public CellType getType() {
        return CellType.BOOLEAN_FORMULA;
    }

    public byte[] getFormulaData() throws FormulaException {
        if (getSheet().getWorkbookBof().isBiff8()) {
            byte[] d = new byte[(this.data.length - 6)];
            System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
            return d;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }
}
