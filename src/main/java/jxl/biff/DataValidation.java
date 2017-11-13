package jxl.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.WorkbookSettings;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class DataValidation {
    private static Logger logger = Logger.getLogger(DataValidation.class);
    private int comboBoxObjectId;
    private boolean copied;
    private ExternalSheet externalSheet;
    private DataValidityListRecord validityList;
    private ArrayList validitySettings;
    private WorkbookMethods workbook;
    private WorkbookSettings workbookSettings;

    public DataValidation(DataValidityListRecord dvlr) {
        this.validityList = dvlr;
        this.validitySettings = new ArrayList(this.validityList.getNumberOfSettings());
        this.copied = false;
    }

    public DataValidation(int objId, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws) {
        this.workbook = wm;
        this.externalSheet = es;
        this.workbookSettings = ws;
        this.validitySettings = new ArrayList();
        this.comboBoxObjectId = objId;
        this.copied = false;
    }

    public DataValidation(DataValidation dv, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws) {
        this.workbook = wm;
        this.externalSheet = es;
        this.workbookSettings = ws;
        this.copied = true;
        this.validityList = new DataValidityListRecord(dv.getDataValidityList());
        this.validitySettings = new ArrayList();
        DataValiditySettingsRecord[] settings = dv.getDataValiditySettings();
        for (DataValiditySettingsRecord dataValiditySettingsRecord : settings) {
            this.validitySettings.add(new DataValiditySettingsRecord(dataValiditySettingsRecord, this.externalSheet, this.workbook, this.workbookSettings));
        }
    }

    public void add(DataValiditySettingsRecord dvsr) {
        boolean z = false;
        this.validitySettings.add(dvsr);
        dvsr.setDataValidation(this);
        if (this.copied) {
            if (this.validityList != null) {
                z = true;
            }
            Assert.verify(z);
            this.validityList.dvAdded();
        }
    }

    public DataValidityListRecord getDataValidityList() {
        return this.validityList;
    }

    public DataValiditySettingsRecord[] getDataValiditySettings() {
        return (DataValiditySettingsRecord[]) this.validitySettings.toArray(new DataValiditySettingsRecord[0]);
    }

    public void write(File outputFile) throws IOException {
        boolean z = false;
        if (this.validitySettings.size() > 65533) {
            logger.warn("Maximum number of data validations exceeded - truncating...");
            this.validitySettings = new ArrayList(this.validitySettings.subList(0, 65532));
            if (this.validitySettings.size() <= 65533) {
                z = true;
            }
            Assert.verify(z);
        }
        if (this.validityList == null) {
            this.validityList = new DataValidityListRecord(new DValParser(this.comboBoxObjectId, this.validitySettings.size()));
        }
        if (this.validityList.hasDVRecords()) {
            outputFile.write(this.validityList);
            Iterator i = this.validitySettings.iterator();
            while (i.hasNext()) {
                outputFile.write((DataValiditySettingsRecord) i.next());
            }
        }
    }

    public void removeDataValidation(int col, int row) {
        Iterator i = this.validitySettings.iterator();
        while (i.hasNext()) {
            DataValiditySettingsRecord dv = (DataValiditySettingsRecord) i.next();
            if (dv.getFirstColumn() == col && dv.getLastColumn() == col && dv.getFirstRow() == row && dv.getLastRow() == row) {
                i.remove();
                this.validityList.dvRemoved();
                return;
            }
        }
    }

    public int getComboBoxObjectId() {
        return this.comboBoxObjectId;
    }
}
