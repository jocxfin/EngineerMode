package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.BuiltInName;
import jxl.biff.ByteData;
import jxl.biff.CountryCode;
import jxl.biff.Fonts;
import jxl.biff.FormattingRecords;
import jxl.biff.IndexMapping;
import jxl.biff.IntegerHelper;
import jxl.biff.WorkbookMethods;
import jxl.biff.XCTRecord;
import jxl.biff.drawing.DrawingGroup;
import jxl.biff.drawing.DrawingGroupObject;
import jxl.biff.drawing.Origin;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.BOFRecord;
import jxl.read.biff.NameRecord;
import jxl.read.biff.SupbookRecord;
import jxl.read.biff.WorkbookParser;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class WritableWorkbookImpl extends WritableWorkbook implements ExternalSheet, WorkbookMethods {
    private static Object SYNCHRONIZER = new Object();
    private static Logger logger = Logger.getLogger(WritableWorkbookImpl.class);
    private String[] addInFunctionNames;
    private ButtonPropertySetRecord buttonPropertySet;
    private boolean closeStream;
    private boolean containsMacros;
    private CountryRecord countryRecord;
    private DrawingGroup drawingGroup;
    private ExternalSheetRecord externSheet;
    private Fonts fonts;
    private FormattingRecords formatRecords;
    private HashMap nameRecords;
    private ArrayList names;
    private File outputFile;
    private ArrayList rcirCells;
    private WorkbookSettings settings;
    private SharedStrings sharedStrings;
    private ArrayList sheets;
    private Styles styles;
    private ArrayList supbooks;
    private boolean wbProtected;
    private XCTRecord[] xctRecords;

    public WritableWorkbookImpl(OutputStream os, boolean cs, WorkbookSettings ws) throws IOException {
        this.outputFile = new File(os, ws, null);
        this.sheets = new ArrayList();
        this.sharedStrings = new SharedStrings();
        this.nameRecords = new HashMap();
        this.closeStream = cs;
        this.wbProtected = false;
        this.containsMacros = false;
        this.settings = ws;
        this.rcirCells = new ArrayList();
        this.styles = new Styles();
        synchronized (SYNCHRONIZER) {
            WritableWorkbook.ARIAL_10_PT.uninitialize();
            WritableWorkbook.HYPERLINK_FONT.uninitialize();
            WritableWorkbook.NORMAL_STYLE.uninitialize();
            WritableWorkbook.HYPERLINK_STYLE.uninitialize();
            WritableWorkbook.HIDDEN_STYLE.uninitialize();
            DateRecord.defaultDateFormat.uninitialize();
        }
        this.fonts = new WritableFonts(this);
        this.formatRecords = new WritableFormattingRecords(this.fonts, this.styles);
    }

    public WritableWorkbookImpl(OutputStream os, Workbook w, boolean cs, WorkbookSettings ws) throws IOException {
        int i;
        WorkbookParser wp = (WorkbookParser) w;
        synchronized (SYNCHRONIZER) {
            WritableWorkbook.ARIAL_10_PT.uninitialize();
            WritableWorkbook.HYPERLINK_FONT.uninitialize();
            WritableWorkbook.NORMAL_STYLE.uninitialize();
            WritableWorkbook.HYPERLINK_STYLE.uninitialize();
            WritableWorkbook.HIDDEN_STYLE.uninitialize();
            DateRecord.defaultDateFormat.uninitialize();
        }
        this.closeStream = cs;
        this.sheets = new ArrayList();
        this.sharedStrings = new SharedStrings();
        this.nameRecords = new HashMap();
        this.fonts = wp.getFonts();
        this.formatRecords = wp.getFormattingRecords();
        this.wbProtected = false;
        this.settings = ws;
        this.rcirCells = new ArrayList();
        this.styles = new Styles();
        this.outputFile = new File(os, ws, wp.getCompoundFile());
        this.containsMacros = false;
        if (!ws.getPropertySetsDisabled()) {
            this.containsMacros = wp.containsMacros();
        }
        if (wp.getCountryRecord() != null) {
            this.countryRecord = new CountryRecord(wp.getCountryRecord());
        }
        this.addInFunctionNames = wp.getAddInFunctionNames();
        this.xctRecords = wp.getXCTRecords();
        if (wp.getExternalSheetRecord() != null) {
            this.externSheet = new ExternalSheetRecord(wp.getExternalSheetRecord());
            SupbookRecord[] readsr = wp.getSupbookRecords();
            this.supbooks = new ArrayList(readsr.length);
            for (SupbookRecord readSupbook : readsr) {
                if (readSupbook.getType() == SupbookRecord.INTERNAL || readSupbook.getType() == SupbookRecord.EXTERNAL) {
                    this.supbooks.add(new SupbookRecord(readSupbook, this.settings));
                } else if (readSupbook.getType() != SupbookRecord.ADDIN) {
                    logger.warn("unsupported supbook type - ignoring");
                }
            }
        }
        if (wp.getDrawingGroup() != null) {
            this.drawingGroup = new DrawingGroup(wp.getDrawingGroup());
        }
        if (this.containsMacros && wp.getButtonPropertySet() != null) {
            this.buttonPropertySet = new ButtonPropertySetRecord(wp.getButtonPropertySet());
        }
        if (!this.settings.getNamesDisabled()) {
            NameRecord[] na = wp.getNameRecords();
            this.names = new ArrayList(na.length);
            for (i = 0; i < na.length; i++) {
                if (na[i].isBiff8()) {
                    NameRecord n = new NameRecord(na[i], i);
                    this.names.add(n);
                    this.nameRecords.put(n.getName(), n);
                } else {
                    logger.warn("Cannot copy Biff7 name records - ignoring");
                }
            }
        }
        copyWorkbook(w);
        if (this.drawingGroup != null) {
            this.drawingGroup.updateData(wp.getDrawingGroup());
        }
    }

    public WritableSheet[] getSheets() {
        WritableSheet[] sheetArray = new WritableSheet[getNumberOfSheets()];
        for (int i = 0; i < getNumberOfSheets(); i++) {
            sheetArray[i] = getSheet(i);
        }
        return sheetArray;
    }

    public String[] getSheetNames() {
        String[] sheetNames = new String[getNumberOfSheets()];
        for (int i = 0; i < sheetNames.length; i++) {
            sheetNames[i] = getSheet(i).getName();
        }
        return sheetNames;
    }

    public WritableSheet getSheet(int index) {
        return (WritableSheet) this.sheets.get(index);
    }

    public int getNumberOfSheets() {
        return this.sheets.size();
    }

    public void close() throws IOException, JxlWriteException {
        this.outputFile.close(this.closeStream);
    }

    private WritableSheet createSheet(String name, int index, boolean handleRefs) {
        WritableSheet w = new WritableSheetImpl(name, this.outputFile, this.formatRecords, this.sharedStrings, this.settings, this);
        int pos = index;
        if (index <= 0) {
            pos = 0;
            this.sheets.add(0, w);
        } else if (index <= this.sheets.size()) {
            this.sheets.add(index, w);
        } else {
            pos = this.sheets.size();
            this.sheets.add(w);
        }
        if (handleRefs && this.externSheet != null) {
            this.externSheet.sheetInserted(pos);
        }
        if (this.supbooks != null && this.supbooks.size() > 0) {
            SupbookRecord supbook = (SupbookRecord) this.supbooks.get(0);
            if (supbook.getType() == SupbookRecord.INTERNAL) {
                supbook.adjustInternal(this.sheets.size());
            }
        }
        return w;
    }

    public WritableSheet createSheet(String name, int index) {
        return createSheet(name, index, true);
    }

    public void write() throws IOException {
        int i;
        for (i = 0; i < getNumberOfSheets(); i++) {
            WritableSheetImpl wsi = (WritableSheetImpl) getSheet(i);
            wsi.checkMergedBorders();
            Range range = wsi.getSettings().getPrintArea();
            if (range != null) {
                addNameArea(BuiltInName.PRINT_AREA, wsi, range.getTopLeft().getColumn(), range.getTopLeft().getRow(), range.getBottomRight().getColumn(), range.getBottomRight().getRow(), false);
            }
            Range rangeR = wsi.getSettings().getPrintTitlesRow();
            Range rangeC = wsi.getSettings().getPrintTitlesCol();
            if (rangeR != null && rangeC != null) {
                addNameArea(BuiltInName.PRINT_TITLES, wsi, rangeR.getTopLeft().getColumn(), rangeR.getTopLeft().getRow(), rangeR.getBottomRight().getColumn(), rangeR.getBottomRight().getRow(), rangeC.getTopLeft().getColumn(), rangeC.getTopLeft().getRow(), rangeC.getBottomRight().getColumn(), rangeC.getBottomRight().getRow(), false);
            } else if (rangeR != null) {
                addNameArea(BuiltInName.PRINT_TITLES, wsi, rangeR.getTopLeft().getColumn(), rangeR.getTopLeft().getRow(), rangeR.getBottomRight().getColumn(), rangeR.getBottomRight().getRow(), false);
            } else if (rangeC != null) {
                addNameArea(BuiltInName.PRINT_TITLES, wsi, rangeC.getTopLeft().getColumn(), rangeC.getTopLeft().getRow(), rangeC.getBottomRight().getColumn(), rangeC.getBottomRight().getRow(), false);
            }
        }
        if (!this.settings.getRationalizationDisabled()) {
            rationalize();
        }
        this.outputFile.write(new BOFRecord(BOFRecord.workbookGlobals));
        if (this.settings.getTemplate()) {
            this.outputFile.write(new TemplateRecord());
        }
        this.outputFile.write(new InterfaceHeaderRecord());
        this.outputFile.write(new MMSRecord(0, 0));
        this.outputFile.write(new InterfaceEndRecord());
        this.outputFile.write(new WriteAccessRecord(this.settings.getWriteAccess()));
        this.outputFile.write(new CodepageRecord());
        this.outputFile.write(new DSFRecord());
        if (this.settings.getExcel9File()) {
            this.outputFile.write(new Excel9FileRecord());
        }
        this.outputFile.write(new TabIdRecord(getNumberOfSheets()));
        if (this.containsMacros) {
            this.outputFile.write(new ObjProjRecord());
        }
        if (this.buttonPropertySet != null) {
            this.outputFile.write(this.buttonPropertySet);
        }
        this.outputFile.write(new FunctionGroupCountRecord());
        this.outputFile.write(new WindowProtectRecord(this.settings.getWindowProtected()));
        this.outputFile.write(new ProtectRecord(this.wbProtected));
        this.outputFile.write(new PasswordRecord(null));
        this.outputFile.write(new Prot4RevRecord(false));
        this.outputFile.write(new Prot4RevPassRecord());
        boolean sheetSelected = false;
        int selectedSheetIndex = 0;
        for (i = 0; i < getNumberOfSheets() && !sheetSelected; i++) {
            if (((WritableSheetImpl) getSheet(i)).getSettings().isSelected()) {
                sheetSelected = true;
                selectedSheetIndex = i;
            }
        }
        if (!sheetSelected) {
            ((WritableSheetImpl) getSheet(0)).getSettings().setSelected(true);
            selectedSheetIndex = 0;
        }
        this.outputFile.write(new Window1Record(selectedSheetIndex));
        this.outputFile.write(new BackupRecord(false));
        this.outputFile.write(new HideobjRecord(this.settings.getHideobj()));
        this.outputFile.write(new NineteenFourRecord(false));
        this.outputFile.write(new PrecisionRecord(false));
        this.outputFile.write(new RefreshAllRecord(this.settings.getRefreshAll()));
        this.outputFile.write(new BookboolRecord(true));
        this.fonts.write(this.outputFile);
        this.formatRecords.write(this.outputFile);
        if (this.formatRecords.getPalette() != null) {
            this.outputFile.write(this.formatRecords.getPalette());
        }
        this.outputFile.write(new UsesElfsRecord());
        int[] boundsheetPos = new int[getNumberOfSheets()];
        for (i = 0; i < getNumberOfSheets(); i++) {
            boundsheetPos[i] = this.outputFile.getPos();
            Sheet sheet = getSheet(i);
            BoundsheetRecord boundsheetRecord = new BoundsheetRecord(sheet.getName());
            if (sheet.getSettings().isHidden()) {
                boundsheetRecord.setHidden();
            }
            if (((WritableSheetImpl) this.sheets.get(i)).isChartOnly()) {
                boundsheetRecord.setChartOnly();
            }
            this.outputFile.write(boundsheetRecord);
        }
        if (this.countryRecord == null) {
            CountryCode lang = CountryCode.getCountryCode(this.settings.getExcelDisplayLanguage());
            if (lang == CountryCode.UNKNOWN) {
                logger.warn("Unknown country code " + this.settings.getExcelDisplayLanguage() + " using " + CountryCode.USA.getCode());
                lang = CountryCode.USA;
            }
            CountryCode region = CountryCode.getCountryCode(this.settings.getExcelRegionalSettings());
            this.countryRecord = new CountryRecord(lang, region);
            if (region == CountryCode.UNKNOWN) {
                logger.warn("Unknown country code " + this.settings.getExcelDisplayLanguage() + " using " + CountryCode.UK.getCode());
                CountryCode countryCode = CountryCode.UK;
            }
        }
        this.outputFile.write(this.countryRecord);
        if (this.addInFunctionNames != null && this.addInFunctionNames.length > 0) {
            for (String externalNameRecord : this.addInFunctionNames) {
                this.outputFile.write(new ExternalNameRecord(externalNameRecord));
            }
        }
        if (this.xctRecords != null) {
            for (ByteData write : this.xctRecords) {
                this.outputFile.write(write);
            }
        }
        if (this.externSheet != null) {
            for (i = 0; i < this.supbooks.size(); i++) {
                this.outputFile.write((SupbookRecord) this.supbooks.get(i));
            }
            this.outputFile.write(this.externSheet);
        }
        if (this.names != null) {
            for (i = 0; i < this.names.size(); i++) {
                this.outputFile.write((NameRecord) this.names.get(i));
            }
        }
        if (this.drawingGroup != null) {
            this.drawingGroup.write(this.outputFile);
        }
        this.sharedStrings.write(this.outputFile);
        this.outputFile.write(new EOFRecord());
        for (i = 0; i < getNumberOfSheets(); i++) {
            this.outputFile.setData(IntegerHelper.getFourBytes(this.outputFile.getPos()), boundsheetPos[i] + 4);
            ((WritableSheetImpl) getSheet(i)).write();
        }
    }

    private void copyWorkbook(Workbook w) {
        int numSheets = w.getNumberOfSheets();
        this.wbProtected = w.isProtected();
        for (int i = 0; i < numSheets; i++) {
            Sheet s = w.getSheet(i);
            ((WritableSheetImpl) createSheet(s.getName(), i, false)).copy(s);
        }
    }

    private void rationalize() {
        IndexMapping fontMapping = this.formatRecords.rationalizeFonts();
        IndexMapping formatMapping = this.formatRecords.rationalizeDisplayFormats();
        IndexMapping xfMapping = this.formatRecords.rationalize(fontMapping, formatMapping);
        for (int i = 0; i < this.sheets.size(); i++) {
            ((WritableSheetImpl) this.sheets.get(i)).rationalize(xfMapping, fontMapping, formatMapping);
        }
    }

    private int getInternalSheetIndex(String name) {
        String[] names = getSheetNames();
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) {
                return i;
            }
        }
        return -1;
    }

    public String getExternalSheetName(int index) {
        SupbookRecord sr = (SupbookRecord) this.supbooks.get(this.externSheet.getSupbookIndex(index));
        int firstTab = this.externSheet.getFirstTabIndex(index);
        if (sr.getType() == SupbookRecord.INTERNAL) {
            return getSheet(firstTab).getName();
        }
        if (sr.getType() == SupbookRecord.EXTERNAL) {
            return sr.getFileName() + sr.getSheetName(firstTab);
        }
        logger.warn("Unknown Supbook 1");
        return "[UNKNOWN]";
    }

    public BOFRecord getWorkbookBof() {
        return null;
    }

    public int getExternalSheetIndex(String sheetName) {
        if (this.externSheet == null) {
            this.externSheet = new ExternalSheetRecord();
            this.supbooks = new ArrayList();
            this.supbooks.add(new SupbookRecord(getNumberOfSheets(), this.settings));
        }
        boolean found = false;
        Iterator i = this.sheets.iterator();
        int sheetpos = 0;
        while (i.hasNext() && !found) {
            if (((WritableSheetImpl) i.next()).getName().equals(sheetName)) {
                found = true;
            } else {
                sheetpos++;
            }
        }
        if (found) {
            SupbookRecord supbook = (SupbookRecord) this.supbooks.get(0);
            if (supbook.getType() != SupbookRecord.INTERNAL || supbook.getNumberOfSheets() != getNumberOfSheets()) {
                logger.warn("Cannot find sheet " + sheetName + " in supbook record");
            }
            return this.externSheet.getIndex(0, sheetpos);
        }
        int closeSquareBracketsIndex = sheetName.lastIndexOf(93);
        int openSquareBracketsIndex = sheetName.lastIndexOf(91);
        if (closeSquareBracketsIndex == -1 || openSquareBracketsIndex == -1) {
            logger.warn("Square brackets");
            return -1;
        }
        String worksheetName = sheetName.substring(closeSquareBracketsIndex + 1);
        String fileName = sheetName.substring(0, openSquareBracketsIndex) + sheetName.substring(openSquareBracketsIndex + 1, closeSquareBracketsIndex);
        boolean supbookFound = false;
        SupbookRecord externalSupbook = null;
        int supbookIndex = -1;
        for (int ind = 0; ind < this.supbooks.size() && !supbookFound; ind++) {
            externalSupbook = (SupbookRecord) this.supbooks.get(ind);
            if (externalSupbook.getType() == SupbookRecord.EXTERNAL && externalSupbook.getFileName().equals(fileName)) {
                supbookFound = true;
                supbookIndex = ind;
            }
        }
        if (!supbookFound) {
            externalSupbook = new SupbookRecord(fileName, this.settings);
            supbookIndex = this.supbooks.size();
            this.supbooks.add(externalSupbook);
        }
        return this.externSheet.getIndex(supbookIndex, externalSupbook.getSheetIndex(worksheetName));
    }

    public String getName(int index) {
        boolean z = false;
        if (index >= 0 && index < this.names.size()) {
            z = true;
        }
        Assert.verify(z);
        return ((NameRecord) this.names.get(index)).getName();
    }

    public int getNameIndex(String name) {
        NameRecord nr = (NameRecord) this.nameRecords.get(name);
        return nr == null ? -1 : nr.getIndex();
    }

    void addRCIRCell(CellValue cv) {
        this.rcirCells.add(cv);
    }

    void addDrawing(DrawingGroupObject d) {
        if (this.drawingGroup == null) {
            this.drawingGroup = new DrawingGroup(Origin.WRITE);
        }
        this.drawingGroup.add(d);
    }

    DrawingGroup getDrawingGroup() {
        return this.drawingGroup;
    }

    Styles getStyles() {
        return this.styles;
    }

    void addNameArea(BuiltInName name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow, boolean global) {
        if (this.names == null) {
            this.names = new ArrayList();
        }
        NameRecord nr = new NameRecord(name, getInternalSheetIndex(sheet.getName()), getExternalSheetIndex(sheet.getName()), firstRow, lastRow, firstCol, lastCol, global);
        this.names.add(nr);
        this.nameRecords.put(name, nr);
    }

    void addNameArea(BuiltInName name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow, int firstCol2, int firstRow2, int lastCol2, int lastRow2, boolean global) {
        if (this.names == null) {
            this.names = new ArrayList();
        }
        NameRecord nr = new NameRecord(name, getInternalSheetIndex(sheet.getName()), getExternalSheetIndex(sheet.getName()), firstRow2, lastRow2, firstCol2, lastCol2, firstRow, lastRow, firstCol, lastCol, global);
        this.names.add(nr);
        this.nameRecords.put(name, nr);
    }

    WorkbookSettings getSettings() {
        return this.settings;
    }
}
