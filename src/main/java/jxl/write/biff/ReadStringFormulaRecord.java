package jxl.write.biff;

import jxl.StringFormulaCell;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;
import jxl.common.Assert;
import jxl.common.Logger;

class ReadStringFormulaRecord extends ReadFormulaRecord implements StringFormulaCell {
    private static Logger logger = Logger.getLogger(ReadFormulaRecord.class);

    public ReadStringFormulaRecord(FormulaData f) {
        super(f);
    }

    public String getString() {
        return ((StringFormulaCell) getReadFormula()).getString();
    }

    protected byte[] handleFormulaException() {
        byte[] celldata = super.getCellData();
        WritableWorkbookImpl w = getSheet().getWorkbook();
        FormulaParser parser = new FormulaParser("\"" + getContents() + "\"", w, w, w.getSettings());
        try {
            parser.parse();
        } catch (FormulaException e2) {
            logger.warn(e2.getMessage());
            parser = new FormulaParser("\"ERROR\"", w, w, w.getSettings());
            try {
                parser.parse();
            } catch (FormulaException e) {
                Assert.verify(false);
            }
        }
        byte[] formulaBytes = parser.getBytes();
        byte[] expressiondata = new byte[(formulaBytes.length + 16)];
        IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
        System.arraycopy(formulaBytes, 0, expressiondata, 16, formulaBytes.length);
        expressiondata[8] = (byte) ((byte) (expressiondata[8] | 2));
        byte[] data = new byte[(celldata.length + expressiondata.length)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        System.arraycopy(expressiondata, 0, data, celldata.length, expressiondata.length);
        data[6] = (byte) 0;
        data[12] = (byte) -1;
        data[13] = (byte) -1;
        return data;
    }
}
