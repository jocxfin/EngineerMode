package jxl.write.biff;

import jxl.BooleanFormulaCell;
import jxl.biff.FormulaData;

class ReadBooleanFormulaRecord extends ReadFormulaRecord implements BooleanFormulaCell {
    public ReadBooleanFormulaRecord(FormulaData f) {
        super(f);
    }

    public boolean getValue() {
        return ((BooleanFormulaCell) getReadFormula()).getValue();
    }
}
