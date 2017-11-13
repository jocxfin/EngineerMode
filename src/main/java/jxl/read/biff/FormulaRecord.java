package jxl.read.biff;

import jxl.CellType;
import jxl.WorkbookSettings;
import jxl.biff.DoubleHelper;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Assert;
import jxl.common.Logger;

class FormulaRecord extends CellValue {
    public static final IgnoreSharedFormula ignoreSharedFormula = new IgnoreSharedFormula();
    private static Logger logger = Logger.getLogger(FormulaRecord.class);
    private CellValue formula;
    private boolean shared = false;

    private static class IgnoreSharedFormula {
        private IgnoreSharedFormula() {
        }
    }

    public FormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        if ((IntegerHelper.getInt(data[14], data[15]) & 8) == 0) {
            if (data[6] == (byte) 0 && data[12] == (byte) -1 && data[13] == (byte) -1) {
                this.formula = new StringFormulaRecord(t, excelFile, fr, es, nt, si, ws);
            } else if (data[6] == (byte) 1 && data[12] == (byte) -1 && data[13] == (byte) -1) {
                this.formula = new BooleanFormulaRecord(t, fr, es, nt, si);
            } else if (data[6] == (byte) 2 && data[12] == (byte) -1 && data[13] == (byte) -1) {
                this.formula = new ErrorFormulaRecord(t, fr, es, nt, si);
            } else if (data[6] == (byte) 3 && data[12] == (byte) -1 && data[13] == (byte) -1) {
                this.formula = new StringFormulaRecord(t, fr, es, nt, si);
            } else {
                this.formula = new NumberFormulaRecord(t, fr, es, nt, si);
            }
            return;
        }
        this.shared = true;
        if (data[6] == (byte) 0 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new SharedStringFormulaRecord(t, excelFile, fr, es, nt, si, ws);
        } else if (data[6] == (byte) 3 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new SharedStringFormulaRecord(t, excelFile, fr, es, nt, si, SharedStringFormulaRecord.EMPTY_STRING);
        } else if (data[6] == (byte) 2 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new SharedErrorFormulaRecord(t, excelFile, data[8], fr, es, nt, si);
        } else if (data[6] == (byte) 1 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            boolean value;
            if (data[8] != (byte) 1) {
                value = false;
            } else {
                value = true;
            }
            this.formula = new SharedBooleanFormulaRecord(t, excelFile, value, fr, es, nt, si);
        } else {
            SharedNumberFormulaRecord snfr = new SharedNumberFormulaRecord(t, excelFile, DoubleHelper.getIEEEDouble(data, 6), fr, es, nt, si);
            snfr.setNumberFormat(fr.getNumberFormat(getXFIndex()));
            this.formula = snfr;
        }
    }

    public FormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, IgnoreSharedFormula i, SheetImpl si, WorkbookSettings ws) {
        super(t, fr, si);
        byte[] data = getRecord().getData();
        if (data[6] == (byte) 0 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new StringFormulaRecord(t, excelFile, fr, es, nt, si, ws);
        } else if (data[6] == (byte) 1 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new BooleanFormulaRecord(t, fr, es, nt, si);
        } else if (data[6] == (byte) 2 && data[12] == (byte) -1 && data[13] == (byte) -1) {
            this.formula = new ErrorFormulaRecord(t, fr, es, nt, si);
        } else {
            this.formula = new NumberFormulaRecord(t, fr, es, nt, si);
        }
    }

    public String getContents() {
        Assert.verify(false);
        return "";
    }

    public CellType getType() {
        Assert.verify(false);
        return CellType.EMPTY;
    }

    final CellValue getFormula() {
        return this.formula;
    }

    final boolean isShared() {
        return this.shared;
    }
}
