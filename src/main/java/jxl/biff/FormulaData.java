package jxl.biff;

import jxl.Cell;
import jxl.biff.formula.FormulaException;

public interface FormulaData extends Cell {
    byte[] getFormulaData() throws FormulaException;
}
