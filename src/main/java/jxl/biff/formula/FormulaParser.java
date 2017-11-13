package jxl.biff.formula;

import jxl.Cell;
import jxl.WorkbookSettings;
import jxl.biff.WorkbookMethods;
import jxl.common.Assert;
import jxl.common.Logger;

public class FormulaParser {
    private static final Logger logger = Logger.getLogger(FormulaParser.class);
    private Parser parser;

    public FormulaParser(byte[] tokens, Cell rt, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws) throws FormulaException {
        boolean z = false;
        if (es.getWorkbookBof() == null || es.getWorkbookBof().isBiff8()) {
            if (nt != null) {
                z = true;
            }
            Assert.verify(z);
            this.parser = new TokenFormulaParser(tokens, rt, es, nt, ws, ParseContext.DEFAULT);
            return;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }

    public FormulaParser(byte[] tokens, Cell rt, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc) throws FormulaException {
        boolean z = false;
        if (es.getWorkbookBof() == null || es.getWorkbookBof().isBiff8()) {
            if (nt != null) {
                z = true;
            }
            Assert.verify(z);
            this.parser = new TokenFormulaParser(tokens, rt, es, nt, ws, pc);
            return;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }

    public FormulaParser(String form, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws) {
        this.parser = new StringFormulaParser(form, es, nt, ws, ParseContext.DEFAULT);
    }

    public FormulaParser(String form, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc) {
        this.parser = new StringFormulaParser(form, es, nt, ws, pc);
    }

    public void parse() throws FormulaException {
        this.parser.parse();
    }

    public String getFormula() throws FormulaException {
        return this.parser.getFormula();
    }

    public byte[] getBytes() {
        return this.parser.getBytes();
    }
}
