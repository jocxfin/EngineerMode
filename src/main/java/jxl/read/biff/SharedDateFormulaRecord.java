package jxl.read.biff;

import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.DateFormulaCell;
import jxl.biff.DoubleHelper;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;

public class SharedDateFormulaRecord extends BaseSharedFormulaRecord implements DateCell, FormulaData, DateFormulaCell {
    private DateRecord dateRecord;
    private double value;

    public SharedDateFormulaRecord(SharedNumberFormulaRecord nfr, FormattingRecords fr, boolean nf, SheetImpl si, int pos) {
        super(nfr.getRecord(), fr, nfr.getExternalSheet(), nfr.getNameTable(), si, pos);
        this.dateRecord = new DateRecord(nfr, nfr.getXFIndex(), fr, nf, si);
        this.value = nfr.getValue();
    }

    public String getContents() {
        return this.dateRecord.getContents();
    }

    public CellType getType() {
        return CellType.DATE_FORMULA;
    }

    public byte[] getFormulaData() throws FormulaException {
        if (getSheet().getWorkbookBof().isBiff8()) {
            FormulaParser fp = new FormulaParser(getTokens(), (Cell) this, getExternalSheet(), getNameTable(), getSheet().getWorkbook().getSettings());
            fp.parse();
            byte[] rpnTokens = fp.getBytes();
            byte[] data = new byte[(rpnTokens.length + 22)];
            IntegerHelper.getTwoBytes(getRow(), data, 0);
            IntegerHelper.getTwoBytes(getColumn(), data, 2);
            IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
            DoubleHelper.getIEEEBytes(this.value, data, 6);
            System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
            IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
            byte[] d = new byte[(data.length - 6)];
            System.arraycopy(data, 6, d, 0, data.length - 6);
            return d;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }

    public Date getDate() {
        return this.dateRecord.getDate();
    }

    public boolean isTime() {
        return this.dateRecord.isTime();
    }
}
