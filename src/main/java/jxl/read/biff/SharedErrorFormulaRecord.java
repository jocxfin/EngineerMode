package jxl.read.biff;

import jxl.Cell;
import jxl.CellType;
import jxl.ErrorCell;
import jxl.ErrorFormulaCell;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaErrorCode;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;
import jxl.common.Logger;

public class SharedErrorFormulaRecord extends BaseSharedFormulaRecord implements ErrorCell, FormulaData, ErrorFormulaCell {
    private static Logger logger = Logger.getLogger(SharedErrorFormulaRecord.class);
    private FormulaErrorCode error;
    private int errorCode;

    public SharedErrorFormulaRecord(Record t, File excelFile, int ec, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        super(t, fr, es, nt, si, excelFile.getPos());
        this.errorCode = ec;
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
            FormulaParser fp = new FormulaParser(getTokens(), (Cell) this, getExternalSheet(), getNameTable(), getSheet().getWorkbook().getSettings());
            fp.parse();
            byte[] rpnTokens = fp.getBytes();
            byte[] data = new byte[(rpnTokens.length + 22)];
            IntegerHelper.getTwoBytes(getRow(), data, 0);
            IntegerHelper.getTwoBytes(getColumn(), data, 2);
            IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
            data[6] = (byte) 2;
            data[8] = (byte) ((byte) this.errorCode);
            data[12] = (byte) -1;
            data[13] = (byte) -1;
            System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
            IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
            byte[] d = new byte[(data.length - 6)];
            System.arraycopy(data, 6, d, 0, data.length - 6);
            return d;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }
}
