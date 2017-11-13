package jxl.read.biff;

import jxl.BooleanCell;
import jxl.BooleanFormulaCell;
import jxl.Cell;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;
import jxl.common.Logger;

public class SharedBooleanFormulaRecord extends BaseSharedFormulaRecord implements BooleanCell, FormulaData, BooleanFormulaCell {
    private static Logger logger = Logger.getLogger(SharedBooleanFormulaRecord.class);
    private boolean value;

    public SharedBooleanFormulaRecord(Record t, File excelFile, boolean v, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        super(t, fr, es, nt, si, excelFile.getPos());
        this.value = v;
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
            int i;
            FormulaParser fp = new FormulaParser(getTokens(), (Cell) this, getExternalSheet(), getNameTable(), getSheet().getWorkbook().getSettings());
            fp.parse();
            byte[] rpnTokens = fp.getBytes();
            byte[] data = new byte[(rpnTokens.length + 22)];
            IntegerHelper.getTwoBytes(getRow(), data, 0);
            IntegerHelper.getTwoBytes(getColumn(), data, 2);
            IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
            data[6] = (byte) 1;
            if (!this.value) {
                i = 0;
            } else {
                i = 1;
            }
            data[8] = (byte) ((byte) i);
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
