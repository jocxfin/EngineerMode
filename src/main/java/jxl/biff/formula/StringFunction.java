package jxl.biff.formula;

import jxl.WorkbookSettings;
import jxl.common.Logger;

class StringFunction extends StringParseItem {
    private static Logger logger = Logger.getLogger(StringFunction.class);
    private Function function;
    private String functionString;

    StringFunction(String s) {
        this.functionString = s.substring(0, s.length() - 1);
    }

    Function getFunction(WorkbookSettings ws) {
        if (this.function == null) {
            this.function = Function.getFunction(this.functionString, ws);
        }
        return this.function;
    }
}
