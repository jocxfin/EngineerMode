package jxl.biff.formula;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import jxl.common.Logger;

public class FunctionNames {
    private static Logger logger = Logger.getLogger(FunctionNames.class);
    private HashMap functions;
    private HashMap names;

    public FunctionNames(Locale l) {
        ResourceBundle rb = ResourceBundle.getBundle("functions", l);
        Function[] allfunctions = Function.getFunctions();
        this.names = new HashMap(allfunctions.length);
        this.functions = new HashMap(allfunctions.length);
        for (Function f : allfunctions) {
            String propname = f.getPropertyName();
            String n = propname.length() == 0 ? null : rb.getString(propname);
            if (n != null) {
                this.names.put(f, n);
                this.functions.put(n, f);
            }
        }
    }

    Function getFunction(String s) {
        return (Function) this.functions.get(s);
    }

    String getName(Function f) {
        return (String) this.names.get(f);
    }
}
