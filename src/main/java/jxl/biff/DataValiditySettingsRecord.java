package jxl.biff;

import jxl.WorkbookSettings;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.Record;

public class DataValiditySettingsRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(DataValiditySettingsRecord.class);
    private byte[] data;
    private DataValidation dataValidation;
    private DVParser dvParser;
    private ExternalSheet externalSheet;
    private WorkbookMethods workbook;
    private WorkbookSettings workbookSettings;

    public DataValiditySettingsRecord(Record t, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws) {
        super(t);
        this.data = t.getData();
        this.externalSheet = es;
        this.workbook = wm;
        this.workbookSettings = ws;
    }

    DataValiditySettingsRecord(DataValiditySettingsRecord dvsr, ExternalSheet es, WorkbookMethods w, WorkbookSettings ws) {
        boolean z = true;
        super(Type.DV);
        this.workbook = w;
        this.externalSheet = es;
        this.workbookSettings = ws;
        Assert.verify(w != null);
        if (es == null) {
            z = false;
        }
        Assert.verify(z);
        this.data = new byte[dvsr.data.length];
        System.arraycopy(dvsr.data, 0, this.data, 0, this.data.length);
    }

    public DataValiditySettingsRecord(DVParser dvp) {
        super(Type.DV);
        this.dvParser = dvp;
    }

    private void initialize() {
        if (this.dvParser == null) {
            this.dvParser = new DVParser(this.data, this.externalSheet, this.workbook, this.workbookSettings);
        }
    }

    public byte[] getData() {
        if (this.dvParser != null) {
            return this.dvParser.getData();
        }
        return this.data;
    }

    public int getFirstColumn() {
        if (this.dvParser == null) {
            initialize();
        }
        return this.dvParser.getFirstColumn();
    }

    public int getLastColumn() {
        if (this.dvParser == null) {
            initialize();
        }
        return this.dvParser.getLastColumn();
    }

    public int getFirstRow() {
        if (this.dvParser == null) {
            initialize();
        }
        return this.dvParser.getFirstRow();
    }

    public int getLastRow() {
        if (this.dvParser == null) {
            initialize();
        }
        return this.dvParser.getLastRow();
    }

    void setDataValidation(DataValidation dv) {
        this.dataValidation = dv;
    }

    DVParser getDVParser() {
        return this.dvParser;
    }
}
