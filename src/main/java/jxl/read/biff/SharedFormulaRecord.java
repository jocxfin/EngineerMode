package jxl.read.biff;

import com.android.engineeringmode.functions.Light;

import java.text.NumberFormat;
import java.util.ArrayList;

import jxl.Cell;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Logger;

class SharedFormulaRecord {
    private static Logger logger = Logger.getLogger(SharedFormulaRecord.class);
    private int firstCol;
    private int firstRow;
    private ArrayList formulas = new ArrayList();
    private int lastCol;
    private int lastRow;
    private SheetImpl sheet;
    private BaseSharedFormulaRecord templateFormula;
    private byte[] tokens;

    public SharedFormulaRecord(Record t, BaseSharedFormulaRecord fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        this.sheet = si;
        byte[] data = t.getData();
        this.firstRow = IntegerHelper.getInt(data[0], data[1]);
        this.lastRow = IntegerHelper.getInt(data[2], data[3]);
        this.firstCol = data[4] & Light.MAIN_KEY_MAX;
        this.lastCol = data[5] & Light.MAIN_KEY_MAX;
        this.templateFormula = fr;
        this.tokens = new byte[(data.length - 10)];
        System.arraycopy(data, 10, this.tokens, 0, this.tokens.length);
    }

    public boolean add(BaseSharedFormulaRecord fr) {
        int r = fr.getRow();
        if (r < this.firstRow || r > this.lastRow) {
            return false;
        }
        int c = fr.getColumn();
        if (c < this.firstCol || c > this.lastCol) {
            return false;
        }
        this.formulas.add(fr);
        return true;
    }

    Cell[] getFormulas(FormattingRecords fr, boolean nf) {
        Cell[] sfs = new Cell[(this.formulas.size() + 1)];
        if (this.templateFormula != null) {
            SharedNumberFormulaRecord snfr;
            this.templateFormula.setTokens(this.tokens);
            if (this.templateFormula.getType() == CellType.NUMBER_FORMULA) {
                snfr = this.templateFormula;
                NumberFormat templateNumberFormat = snfr.getNumberFormat();
                if (fr.isDate(this.templateFormula.getXFIndex())) {
                    this.templateFormula = new SharedDateFormulaRecord(snfr, fr, nf, this.sheet, snfr.getFilePos());
                    this.templateFormula.setTokens(snfr.getTokens());
                }
            }
            sfs[0] = this.templateFormula;
            for (int i = 0; i < this.formulas.size(); i++) {
                BaseSharedFormulaRecord f = (BaseSharedFormulaRecord) this.formulas.get(i);
                if (f.getType() == CellType.NUMBER_FORMULA) {
                    snfr = (SharedNumberFormulaRecord) f;
                    if (fr.isDate(f.getXFIndex())) {
                        f = new SharedDateFormulaRecord(snfr, fr, nf, this.sheet, snfr.getFilePos());
                    }
                }
                f.setTokens(this.tokens);
                sfs[i + 1] = f;
            }
            return sfs;
        }
        logger.warn("Shared formula template formula is null");
        return new Cell[0];
    }

    BaseSharedFormulaRecord getTemplateFormula() {
        return this.templateFormula;
    }
}
