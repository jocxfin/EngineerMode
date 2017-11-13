package jxl.read.biff;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jxl.CellType;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.biff.DoubleHelper;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;
import jxl.common.Logger;

class NumberFormulaRecord extends CellValue implements NumberCell, FormulaData, NumberFormulaCell {
    private static final DecimalFormat defaultFormat = new DecimalFormat("#.###");
    private static Logger logger = Logger.getLogger(NumberFormulaRecord.class);
    private byte[] data = getRecord().getData();
    private ExternalSheet externalSheet;
    private NumberFormat format;
    private WorkbookMethods nameTable;
    private double value;

    public NumberFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        this.format = fr.getNumberFormat(getXFIndex());
        if (this.format == null) {
            this.format = defaultFormat;
        }
        this.value = DoubleHelper.getIEEEDouble(this.data, 6);
    }

    public double getValue() {
        return this.value;
    }

    public String getContents() {
        return Double.isNaN(this.value) ? "" : this.format.format(this.value);
    }

    public CellType getType() {
        return CellType.NUMBER_FORMULA;
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
