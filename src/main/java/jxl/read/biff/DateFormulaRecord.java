package jxl.read.biff;

import jxl.CellType;
import jxl.DateCell;
import jxl.DateFormulaCell;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;

class DateFormulaRecord extends DateRecord implements DateCell, FormulaData, DateFormulaCell {
    private byte[] data;
    private ExternalSheet externalSheet;
    private WorkbookMethods nameTable;

    public DateFormulaRecord(NumberFormulaRecord t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, boolean nf, SheetImpl si) throws FormulaException {
        super(t, t.getXFIndex(), fr, nf, si);
        this.externalSheet = es;
        this.nameTable = nt;
        this.data = t.getFormulaData();
    }

    public CellType getType() {
        return CellType.DATE_FORMULA;
    }

    public byte[] getFormulaData() throws FormulaException {
        if (getSheet().getWorkbookBof().isBiff8()) {
            return this.data;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }
}
