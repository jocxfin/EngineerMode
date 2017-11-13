package jxl.read.biff;

import jxl.CellType;
import jxl.ErrorCell;
import jxl.ErrorFormulaCell;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaErrorCode;
import jxl.biff.formula.FormulaException;
import jxl.common.Assert;

class ErrorFormulaRecord extends CellValue implements ErrorCell, FormulaData, ErrorFormulaCell {
    private byte[] data = getRecord().getData();
    private FormulaErrorCode error;
    private int errorCode;
    private ExternalSheet externalSheet;
    private WorkbookMethods nameTable;

    public ErrorFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        boolean z;
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        if (this.data[6] != (byte) 2) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        this.errorCode = this.data[8];
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getContents() {
        if (this.error == null) {
            this.error = FormulaErrorCode.getErrorCode(this.errorCode);
        }
        return this.error == FormulaErrorCode.UNKNOWN ? "ERROR " + this.errorCode : this.error.getDescription();
    }

    public CellType getType() {
        return CellType.FORMULA_ERROR;
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
