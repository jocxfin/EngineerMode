package jxl.biff.formula;

interface Parser {
    byte[] getBytes();

    String getFormula();

    void parse() throws FormulaException;
}
