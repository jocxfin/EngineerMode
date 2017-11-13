package jxl.write.biff;

import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;
import jxl.common.Assert;
import jxl.common.Logger;

class ReadFormulaRecord extends CellValue implements FormulaData {
    private static Logger logger = Logger.getLogger(ReadFormulaRecord.class);
    private FormulaData formula;
    private FormulaParser parser;

    protected ReadFormulaRecord(FormulaData f) {
        super(Type.FORMULA, f);
        this.formula = f;
    }

    protected final byte[] getCellData() {
        return super.getData();
    }

    protected byte[] handleFormulaException() {
        byte[] celldata = super.getData();
        WritableWorkbookImpl w = getSheet().getWorkbook();
        this.parser = new FormulaParser(getContents(), w, w, w.getSettings());
        try {
            this.parser.parse();
        } catch (FormulaException e2) {
            logger.warn(e2.getMessage());
            this.parser = new FormulaParser("\"ERROR\"", w, w, w.getSettings());
            try {
                this.parser.parse();
            } catch (FormulaException e) {
                Assert.verify(false);
            }
        }
        byte[] formulaBytes = this.parser.getBytes();
        byte[] expressiondata = new byte[(formulaBytes.length + 16)];
        IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
        System.arraycopy(formulaBytes, 0, expressiondata, 16, formulaBytes.length);
        expressiondata[8] = (byte) ((byte) (expressiondata[8] | 2));
        byte[] data = new byte[(celldata.length + expressiondata.length)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        System.arraycopy(expressiondata, 0, data, celldata.length, expressiondata.length);
        return data;
    }

    public byte[] getData() {
        byte[] celldata = super.getData();
        try {
            byte[] expressiondata;
            if (this.parser != null) {
                byte[] formulaBytes = this.parser.getBytes();
                expressiondata = new byte[(formulaBytes.length + 16)];
                IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
                System.arraycopy(formulaBytes, 0, expressiondata, 16, formulaBytes.length);
            } else {
                expressiondata = this.formula.getFormulaData();
            }
            expressiondata[8] = (byte) ((byte) (expressiondata[8] | 2));
            byte[] data = new byte[(celldata.length + expressiondata.length)];
            System.arraycopy(celldata, 0, data, 0, celldata.length);
            System.arraycopy(expressiondata, 0, data, celldata.length, expressiondata.length);
            return data;
        } catch (FormulaException e) {
            logger.warn(CellReferenceHelper.getCellReference(getColumn(), getRow()) + " " + e.getMessage());
            return handleFormulaException();
        }
    }

    public CellType getType() {
        return this.formula.getType();
    }

    public String getContents() {
        return this.formula.getContents();
    }

    public byte[] getFormulaData() throws FormulaException {
        byte[] d = this.formula.getFormulaData();
        byte[] data = new byte[d.length];
        System.arraycopy(d, 0, data, 0, d.length);
        data[8] = (byte) ((byte) (data[8] | 2));
        return data;
    }

    void setCellDetails(FormattingRecords fr, SharedStrings ss, WritableSheetImpl s) {
        super.setCellDetails(fr, ss, s);
        s.getWorkbook().addRCIRCell(this);
    }

    protected FormulaData getReadFormula() {
        return this.formula;
    }
}
