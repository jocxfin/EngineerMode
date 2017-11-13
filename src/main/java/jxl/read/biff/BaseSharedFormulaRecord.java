package jxl.read.biff;

import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;

public abstract class BaseSharedFormulaRecord extends CellValue implements FormulaData {
    private ExternalSheet externalSheet;
    private int filePos;
    private WorkbookMethods nameTable;
    private byte[] tokens;

    public BaseSharedFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, int pos) {
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        this.filePos = pos;
    }

    void setTokens(byte[] t) {
        this.tokens = t;
    }

    protected final byte[] getTokens() {
        return this.tokens;
    }

    protected final ExternalSheet getExternalSheet() {
        return this.externalSheet;
    }

    protected final WorkbookMethods getNameTable() {
        return this.nameTable;
    }

    public Record getRecord() {
        return super.getRecord();
    }

    final int getFilePos() {
        return this.filePos;
    }
}
