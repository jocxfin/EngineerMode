package jxl.biff.formula;

import jxl.JXLException;

public class FormulaException extends JXLException {
    public static final FormulaMessage BIFF8_SUPPORTED = new FormulaMessage("Only biff8 formulas are supported");
    static final FormulaMessage CELL_NAME_NOT_FOUND = new FormulaMessage("Could not find named cell");
    static final FormulaMessage INCORRECT_ARGUMENTS = new FormulaMessage("Incorrect arguments supplied to function");
    static final FormulaMessage LEXICAL_ERROR = new FormulaMessage("Lexical error:  ");
    static final FormulaMessage SHEET_REF_NOT_FOUND = new FormulaMessage("Could not find sheet");
    static final FormulaMessage UNRECOGNIZED_FUNCTION = new FormulaMessage("Unrecognized function");
    static final FormulaMessage UNRECOGNIZED_TOKEN = new FormulaMessage("Unrecognized token");

    private static class FormulaMessage {
        private String message;

        FormulaMessage(String m) {
            this.message = m;
        }
    }

    public FormulaException(FormulaMessage m) {
        super(m.message);
    }

    public FormulaException(FormulaMessage m, int val) {
        super(m.message + " " + val);
    }

    public FormulaException(FormulaMessage m, String val) {
        super(m.message + " " + val);
    }
}
