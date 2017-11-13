package jxl.read.biff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.BuiltInName;
import jxl.biff.FontRecord;
import jxl.biff.Fonts;
import jxl.biff.FormatRecord;
import jxl.biff.FormattingRecords;
import jxl.biff.NameRangeException;
import jxl.biff.NumFormatRecordsException;
import jxl.biff.PaletteRecord;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WorkbookMethods;
import jxl.biff.XCTRecord;
import jxl.biff.XFRecord;
import jxl.biff.drawing.DrawingGroup;
import jxl.biff.drawing.MsoDrawingGroupRecord;
import jxl.biff.drawing.Origin;
import jxl.biff.formula.ExternalSheet;
import jxl.common.Assert;
import jxl.common.Logger;

public class WorkbookParser extends Workbook implements ExternalSheet, WorkbookMethods {
    private static Logger logger = Logger.getLogger(WorkbookParser.class);
    private ArrayList addInFunctions;
    private int bofs;
    private ArrayList boundsheets = new ArrayList(10);
    private ButtonPropertySetRecord buttonPropertySet;
    private boolean containsMacros = false;
    private CountryRecord countryRecord;
    private DrawingGroup drawingGroup;
    private File excelFile;
    private ExternalSheetRecord externSheet;
    private Fonts fonts = new Fonts();
    private FormattingRecords formattingRecords = new FormattingRecords(this.fonts);
    private SheetImpl lastSheet;
    private int lastSheetIndex = -1;
    private MsoDrawingGroupRecord msoDrawingGroup;
    private ArrayList nameTable;
    private HashMap namedRecords = new HashMap();
    private boolean nineteenFour;
    private WorkbookSettings settings;
    private SSTRecord sharedStrings;
    private ArrayList sheets = new ArrayList(10);
    private ArrayList supbooks = new ArrayList(10);
    private boolean wbProtected = false;
    private BOFRecord workbookBof;
    private ArrayList xctRecords;

    public WorkbookParser(File f, WorkbookSettings s) {
        this.excelFile = f;
        this.settings = s;
        this.xctRecords = new ArrayList(10);
    }

    public Sheet getSheet(int index) {
        if (this.lastSheet != null && this.lastSheetIndex == index) {
            return this.lastSheet;
        }
        if (this.lastSheet != null) {
            this.lastSheet.clear();
            if (!this.settings.getGCDisabled()) {
                System.gc();
            }
        }
        this.lastSheet = (SheetImpl) this.sheets.get(index);
        this.lastSheetIndex = index;
        this.lastSheet.readSheet();
        return this.lastSheet;
    }

    public String getExternalSheetName(int index) {
        if (this.workbookBof.isBiff7()) {
            return ((BoundsheetRecord) this.boundsheets.get(index)).getName();
        }
        SupbookRecord sr = (SupbookRecord) this.supbooks.get(this.externSheet.getSupbookIndex(index));
        int firstTab = this.externSheet.getFirstTabIndex(index);
        int lastTab = this.externSheet.getLastTabIndex(index);
        String firstTabName = "";
        String lastTabName = "";
        if (sr.getType() == SupbookRecord.INTERNAL) {
            String sheetName;
            if (firstTab != 65535) {
                firstTabName = ((BoundsheetRecord) this.boundsheets.get(firstTab)).getName();
            } else {
                firstTabName = "#REF";
            }
            if (lastTab != 65535) {
                lastTabName = ((BoundsheetRecord) this.boundsheets.get(lastTab)).getName();
            } else {
                lastTabName = "#REF";
            }
            if (firstTab != lastTab) {
                sheetName = firstTabName + ':' + lastTabName;
            } else {
                sheetName = firstTabName;
            }
            if (sheetName.indexOf(39) != -1) {
                sheetName = StringHelper.replace(sheetName, "'", "''");
            }
            if (sheetName.indexOf(32) != -1) {
                sheetName = '\'' + sheetName + '\'';
            }
            return sheetName;
        } else if (sr.getType() != SupbookRecord.EXTERNAL) {
            logger.warn("Unknown Supbook 3");
            return "[UNKNOWN]";
        } else {
            String sheetName2;
            StringBuffer sb = new StringBuffer();
            File fl = new File(sr.getFileName());
            sb.append("'");
            sb.append(fl.getAbsolutePath());
            sb.append("[");
            sb.append(fl.getName());
            sb.append("]");
            if (firstTab != 65535) {
                sheetName2 = sr.getSheetName(firstTab);
            } else {
                sheetName2 = "#REF";
            }
            sb.append(sheetName2);
            if (lastTab != firstTab) {
                sb.append(sr.getSheetName(lastTab));
            }
            sb.append("'");
            return sb.toString();
        }
    }

    public int getNumberOfSheets() {
        return this.sheets.size();
    }

    final void addSheet(Sheet s) {
        this.sheets.add(s);
    }

    protected void parse() throws BiffException, PasswordException {
        Record r = null;
        BOFRecord bof = new BOFRecord(this.excelFile.next());
        this.workbookBof = bof;
        this.bofs++;
        if (!bof.isBiff8() && !bof.isBiff7()) {
            throw new BiffException(BiffException.unrecognizedBiffVersion);
        } else if (bof.isWorkbookGlobals()) {
            NameRecord nr;
            BoundsheetRecord br;
            ArrayList continueRecords = new ArrayList();
            ArrayList localNames = new ArrayList();
            this.nameTable = new ArrayList();
            this.addInFunctions = new ArrayList();
            while (this.bofs == 1) {
                r = this.excelFile.next();
                Record nextrec;
                if (r.getType() == Type.SST) {
                    continueRecords.clear();
                    nextrec = this.excelFile.peek();
                    while (nextrec.getType() == Type.CONTINUE) {
                        continueRecords.add(this.excelFile.next());
                        nextrec = this.excelFile.peek();
                    }
                    this.sharedStrings = new SSTRecord(r, (Record[]) continueRecords.toArray(new Record[continueRecords.size()]), this.settings);
                } else if (r.getType() == Type.FILEPASS) {
                    throw new PasswordException();
                } else if (r.getType() == Type.NAME) {
                    NameRecord nameRecord;
                    if (bof.isBiff8()) {
                        nameRecord = new NameRecord(r, this.settings, this.nameTable.size());
                    } else {
                        nameRecord = new NameRecord(r, this.settings, this.nameTable.size(), NameRecord.biff7);
                    }
                    this.nameTable.add(nr);
                    if (nr.isGlobal()) {
                        this.namedRecords.put(nr.getName(), nr);
                    } else {
                        localNames.add(nr);
                    }
                } else if (r.getType() == Type.FONT) {
                    FontRecord fontRecord;
                    if (bof.isBiff8()) {
                        fontRecord = new FontRecord(r, this.settings);
                    } else {
                        fontRecord = new FontRecord(r, this.settings, FontRecord.biff7);
                    }
                    this.fonts.addFont(fr);
                } else if (r.getType() == Type.PALETTE) {
                    this.formattingRecords.setPalette(new PaletteRecord(r));
                } else if (r.getType() == Type.NINETEENFOUR) {
                    this.nineteenFour = new NineteenFourRecord(r).is1904();
                } else if (r.getType() == Type.FORMAT) {
                    FormatRecord formatRecord;
                    if (bof.isBiff8()) {
                        formatRecord = new FormatRecord(r, this.settings, FormatRecord.biff8);
                    } else {
                        formatRecord = new FormatRecord(r, this.settings, FormatRecord.biff7);
                    }
                    try {
                        this.formattingRecords.addFormat(fr);
                    } catch (NumFormatRecordsException e) {
                        Assert.verify(false, e.getMessage());
                    }
                } else if (r.getType() == Type.XF) {
                    XFRecord xFRecord;
                    if (bof.isBiff8()) {
                        xFRecord = new XFRecord(r, this.settings, XFRecord.biff8);
                    } else {
                        xFRecord = new XFRecord(r, this.settings, XFRecord.biff7);
                    }
                    try {
                        this.formattingRecords.addStyle(xfr);
                    } catch (NumFormatRecordsException e2) {
                        Assert.verify(false, e2.getMessage());
                    }
                } else if (r.getType() == Type.BOUNDSHEET) {
                    if (bof.isBiff8()) {
                        br = new BoundsheetRecord(r, this.settings);
                    } else {
                        br = new BoundsheetRecord(r, BoundsheetRecord.biff7);
                    }
                    if (br.isSheet()) {
                        this.boundsheets.add(br);
                    } else if (br.isChart() && !this.settings.getDrawingsDisabled()) {
                        this.boundsheets.add(br);
                    }
                } else if (r.getType() != Type.EXTERNSHEET) {
                    if (r.getType() == Type.XCT) {
                        this.xctRecords.add(new XCTRecord(r));
                    } else if (r.getType() == Type.CODEPAGE) {
                        this.settings.setCharacterSet(new CodepageRecord(r).getCharacterSet());
                    } else if (r.getType() == Type.SUPBOOK) {
                        nextrec = this.excelFile.peek();
                        while (nextrec.getType() == Type.CONTINUE) {
                            r.addContinueRecord(this.excelFile.next());
                            nextrec = this.excelFile.peek();
                        }
                        this.supbooks.add(new SupbookRecord(r, this.settings));
                    } else if (r.getType() == Type.EXTERNNAME) {
                        ExternalNameRecord enr = new ExternalNameRecord(r, this.settings);
                        if (enr.isAddInFunction()) {
                            this.addInFunctions.add(enr.getName());
                        }
                    } else if (r.getType() == Type.PROTECT) {
                        this.wbProtected = new ProtectRecord(r).isProtected();
                    } else if (r.getType() == Type.OBJPROJ) {
                        this.containsMacros = true;
                    } else if (r.getType() == Type.COUNTRY) {
                        this.countryRecord = new CountryRecord(r);
                    } else if (r.getType() != Type.MSODRAWINGGROUP) {
                        if (r.getType() == Type.BUTTONPROPERTYSET) {
                            this.buttonPropertySet = new ButtonPropertySetRecord(r);
                        } else if (r.getType() == Type.EOF) {
                            this.bofs--;
                        } else if (r.getType() == Type.REFRESHALL) {
                            this.settings.setRefreshAll(new RefreshAllRecord(r).getRefreshAll());
                        } else if (r.getType() == Type.TEMPLATE) {
                            this.settings.setTemplate(new TemplateRecord(r).getTemplate());
                        } else if (r.getType() == Type.EXCEL9FILE) {
                            this.settings.setExcel9File(new Excel9FileRecord(r).getExcel9File());
                        } else if (r.getType() == Type.WINDOWPROTECT) {
                            this.settings.setWindowProtected(new WindowProtectedRecord(r).getWindowProtected());
                        } else if (r.getType() == Type.HIDEOBJ) {
                            this.settings.setHideobj(new HideobjRecord(r).getHideMode());
                        } else if (r.getType() == Type.WRITEACCESS) {
                            this.settings.setWriteAccess(new WriteAccessRecord(r, bof.isBiff8(), this.settings).getWriteAccess());
                        }
                    } else if (!this.settings.getDrawingsDisabled()) {
                        this.msoDrawingGroup = new MsoDrawingGroupRecord(r);
                        if (this.drawingGroup == null) {
                            this.drawingGroup = new DrawingGroup(Origin.READ);
                        }
                        this.drawingGroup.add(this.msoDrawingGroup);
                        nextrec = this.excelFile.peek();
                        while (nextrec.getType() == Type.CONTINUE) {
                            this.drawingGroup.add(this.excelFile.next());
                            nextrec = this.excelFile.peek();
                        }
                    }
                } else if (bof.isBiff8()) {
                    this.externSheet = new ExternalSheetRecord(r, this.settings);
                } else {
                    this.externSheet = new ExternalSheetRecord(r, this.settings, ExternalSheetRecord.biff7);
                }
            }
            bof = null;
            if (this.excelFile.hasNext()) {
                r = this.excelFile.next();
                if (r.getType() == Type.BOF) {
                    bof = new BOFRecord(r);
                }
            }
            while (bof != null && getNumberOfSheets() < this.boundsheets.size()) {
                if (bof.isBiff8() || bof.isBiff7()) {
                    SheetImpl s;
                    if (bof.isWorksheet()) {
                        s = new SheetImpl(this.excelFile, this.sharedStrings, this.formattingRecords, bof, this.workbookBof, this.nineteenFour, this);
                        br = (BoundsheetRecord) this.boundsheets.get(getNumberOfSheets());
                        s.setName(br.getName());
                        s.setHidden(br.isHidden());
                        addSheet(s);
                    } else if (bof.isChart()) {
                        s = new SheetImpl(this.excelFile, this.sharedStrings, this.formattingRecords, bof, this.workbookBof, this.nineteenFour, this);
                        br = (BoundsheetRecord) this.boundsheets.get(getNumberOfSheets());
                        s.setName(br.getName());
                        s.setHidden(br.isHidden());
                        addSheet(s);
                    } else {
                        logger.warn("BOF is unrecognized");
                        while (this.excelFile.hasNext() && r26.getType() != Type.EOF) {
                            r = this.excelFile.next();
                        }
                    }
                    bof = null;
                    if (this.excelFile.hasNext()) {
                        r = this.excelFile.next();
                        if (r.getType() == Type.BOF) {
                            bof = new BOFRecord(r);
                        }
                    }
                } else {
                    throw new BiffException(BiffException.unrecognizedBiffVersion);
                }
            }
            Iterator it = localNames.iterator();
            while (it.hasNext()) {
                nr = (NameRecord) it.next();
                if (nr.getBuiltInName() == null) {
                    logger.warn("Usage of a local non-builtin name");
                } else if (nr.getBuiltInName() == BuiltInName.PRINT_AREA || nr.getBuiltInName() == BuiltInName.PRINT_TITLES) {
                    ((SheetImpl) this.sheets.get(nr.getSheetRef() - 1)).addLocalName(nr);
                }
            }
        } else {
            throw new BiffException(BiffException.expectedGlobals);
        }
    }

    public FormattingRecords getFormattingRecords() {
        return this.formattingRecords;
    }

    public ExternalSheetRecord getExternalSheetRecord() {
        return this.externSheet;
    }

    public SupbookRecord[] getSupbookRecords() {
        return (SupbookRecord[]) this.supbooks.toArray(new SupbookRecord[this.supbooks.size()]);
    }

    public NameRecord[] getNameRecords() {
        return (NameRecord[]) this.nameTable.toArray(new NameRecord[this.nameTable.size()]);
    }

    public Fonts getFonts() {
        return this.fonts;
    }

    public BOFRecord getWorkbookBof() {
        return this.workbookBof;
    }

    public boolean isProtected() {
        return this.wbProtected;
    }

    public WorkbookSettings getSettings() {
        return this.settings;
    }

    public int getExternalSheetIndex(String sheetName) {
        return 0;
    }

    public String getName(int index) throws NameRangeException {
        if (index >= 0 && index < this.nameTable.size()) {
            return ((NameRecord) this.nameTable.get(index)).getName();
        }
        throw new NameRangeException();
    }

    public int getNameIndex(String name) {
        NameRecord nr = (NameRecord) this.namedRecords.get(name);
        return nr == null ? 0 : nr.getIndex();
    }

    public DrawingGroup getDrawingGroup() {
        return this.drawingGroup;
    }

    public CompoundFile getCompoundFile() {
        return this.excelFile.getCompoundFile();
    }

    public boolean containsMacros() {
        return this.containsMacros;
    }

    public ButtonPropertySetRecord getButtonPropertySet() {
        return this.buttonPropertySet;
    }

    public CountryRecord getCountryRecord() {
        return this.countryRecord;
    }

    public String[] getAddInFunctionNames() {
        return (String[]) this.addInFunctions.toArray(new String[0]);
    }

    public XCTRecord[] getXCTRecords() {
        return (XCTRecord[]) this.xctRecords.toArray(new XCTRecord[0]);
    }
}
