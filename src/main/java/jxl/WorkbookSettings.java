package jxl;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import jxl.biff.CountryCode;
import jxl.biff.formula.FunctionNames;
import jxl.common.Logger;

public final class WorkbookSettings {
    private static Logger logger = Logger.getLogger(WorkbookSettings.class);
    private int arrayGrowSize;
    private boolean autoFilterDisabled;
    private boolean cellValidationDisabled;
    private int characterSet;
    private boolean drawingsDisabled;
    private String encoding;
    private boolean excel9file;
    private String excelDisplayLanguage;
    private String excelRegionalSettings;
    private boolean formulaReferenceAdjustDisabled;
    private FunctionNames functionNames;
    private boolean gcDisabled;
    private int hideobj;
    private boolean ignoreBlankCells;
    private int initialFileSize;
    private Locale locale;
    private HashMap localeFunctionNames;
    private boolean mergedCellCheckingDisabled;
    private boolean namesDisabled;
    private boolean propertySetsDisabled;
    private boolean rationalizationDisabled;
    private boolean refreshAll;
    private boolean template;
    private File temporaryFileDuringWriteDirectory;
    private boolean useTemporaryFileDuringWrite;
    private boolean windowProtected;
    private String writeAccess;

    public WorkbookSettings() {
        boolean z = false;
        this.excel9file = false;
        this.initialFileSize = 5242880;
        this.arrayGrowSize = 1048576;
        this.localeFunctionNames = new HashMap();
        this.excelDisplayLanguage = CountryCode.USA.getCode();
        this.excelRegionalSettings = CountryCode.UK.getCode();
        this.refreshAll = false;
        this.template = false;
        this.excel9file = false;
        this.windowProtected = false;
        this.hideobj = 0;
        try {
            setSuppressWarnings(Boolean.getBoolean("jxl.nowarnings"));
            this.drawingsDisabled = Boolean.getBoolean("jxl.nodrawings");
            this.namesDisabled = Boolean.getBoolean("jxl.nonames");
            this.gcDisabled = Boolean.getBoolean("jxl.nogc");
            this.rationalizationDisabled = Boolean.getBoolean("jxl.norat");
            this.mergedCellCheckingDisabled = Boolean.getBoolean("jxl.nomergedcellchecks");
            this.formulaReferenceAdjustDisabled = Boolean.getBoolean("jxl.noformulaadjust");
            this.propertySetsDisabled = Boolean.getBoolean("jxl.nopropertysets");
            this.ignoreBlankCells = Boolean.getBoolean("jxl.ignoreblanks");
            this.cellValidationDisabled = Boolean.getBoolean("jxl.nocellvalidation");
            if (!Boolean.getBoolean("jxl.autofilter")) {
                z = true;
            }
            this.autoFilterDisabled = z;
            this.useTemporaryFileDuringWrite = Boolean.getBoolean("jxl.usetemporaryfileduringwrite");
            String tempdir = System.getProperty("jxl.temporaryfileduringwritedirectory");
            if (tempdir != null) {
                this.temporaryFileDuringWriteDirectory = new File(tempdir);
            }
            this.encoding = System.getProperty("file.encoding");
        } catch (SecurityException e) {
            logger.warn("Error accessing system properties.", e);
        }
        try {
            if (System.getProperty("jxl.lang") != null) {
                if (System.getProperty("jxl.country") != null) {
                    this.locale = new Locale(System.getProperty("jxl.lang"), System.getProperty("jxl.country"));
                    if (System.getProperty("jxl.encoding") == null) {
                        this.encoding = System.getProperty("jxl.encoding");
                    }
                }
            }
            this.locale = Locale.getDefault();
            if (System.getProperty("jxl.encoding") == null) {
                this.encoding = System.getProperty("jxl.encoding");
            }
        } catch (SecurityException e2) {
            logger.warn("Error accessing system properties.", e2);
            this.locale = Locale.getDefault();
        }
    }

    public int getArrayGrowSize() {
        return this.arrayGrowSize;
    }

    public int getInitialFileSize() {
        return this.initialFileSize;
    }

    public boolean getDrawingsDisabled() {
        return this.drawingsDisabled;
    }

    public boolean getGCDisabled() {
        return this.gcDisabled;
    }

    public boolean getNamesDisabled() {
        return this.namesDisabled;
    }

    public void setDrawingsDisabled(boolean b) {
        this.drawingsDisabled = b;
    }

    public boolean getRationalizationDisabled() {
        return this.rationalizationDisabled;
    }

    public boolean getMergedCellCheckingDisabled() {
        return this.mergedCellCheckingDisabled;
    }

    public boolean getPropertySetsDisabled() {
        return this.propertySetsDisabled;
    }

    public void setSuppressWarnings(boolean w) {
        logger.setSuppressWarnings(w);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public FunctionNames getFunctionNames() {
        if (this.functionNames == null) {
            this.functionNames = (FunctionNames) this.localeFunctionNames.get(this.locale);
            if (this.functionNames == null) {
                this.functionNames = new FunctionNames(this.locale);
                this.localeFunctionNames.put(this.locale, this.functionNames);
            }
        }
        return this.functionNames;
    }

    public void setCharacterSet(int cs) {
        this.characterSet = cs;
    }

    public boolean getIgnoreBlanks() {
        return this.ignoreBlankCells;
    }

    public boolean getCellValidationDisabled() {
        return this.cellValidationDisabled;
    }

    public String getExcelDisplayLanguage() {
        return this.excelDisplayLanguage;
    }

    public String getExcelRegionalSettings() {
        return this.excelRegionalSettings;
    }

    public boolean getAutoFilterDisabled() {
        return this.autoFilterDisabled;
    }

    public boolean getUseTemporaryFileDuringWrite() {
        return this.useTemporaryFileDuringWrite;
    }

    public File getTemporaryFileDuringWriteDirectory() {
        return this.temporaryFileDuringWriteDirectory;
    }

    public void setRefreshAll(boolean refreshAll) {
        this.refreshAll = refreshAll;
    }

    public boolean getRefreshAll() {
        return this.refreshAll;
    }

    public boolean getTemplate() {
        return this.template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public boolean getExcel9File() {
        return this.excel9file;
    }

    public void setExcel9File(boolean excel9file) {
        this.excel9file = excel9file;
    }

    public boolean getWindowProtected() {
        return this.windowProtected;
    }

    public void setWindowProtected(boolean windowprotected) {
        this.windowProtected = this.windowProtected;
    }

    public int getHideobj() {
        return this.hideobj;
    }

    public void setHideobj(int hideobj) {
        this.hideobj = hideobj;
    }

    public String getWriteAccess() {
        return this.writeAccess;
    }

    public void setWriteAccess(String writeAccess) {
        this.writeAccess = writeAccess;
    }
}
