package jxl.read.biff;

import com.android.engineeringmode.functions.Light;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Cell;
import jxl.Hyperlink;
import jxl.Range;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.WorkbookSettings;
import jxl.biff.AutoFilter;
import jxl.biff.BuiltInName;
import jxl.biff.ConditionalFormat;
import jxl.biff.DataValidation;
import jxl.biff.EmptyCell;
import jxl.biff.FormattingRecords;
import jxl.biff.Type;
import jxl.biff.WorkspaceInformationRecord;
import jxl.biff.drawing.Chart;
import jxl.biff.drawing.DrawingGroupObject;
import jxl.common.Logger;
import jxl.read.biff.NameRecord.NameRange;

public class SheetImpl implements Sheet {
    private static Logger logger = Logger.getLogger(SheetImpl.class);
    private AutoFilter autoFilter;
    private ButtonPropertySetRecord buttonPropertySet;
    private Cell[][] cells;
    private ArrayList charts;
    private int[] columnBreaks;
    private ColumnInfoRecord[] columnInfos;
    private ArrayList columnInfosArray = new ArrayList();
    private boolean columnInfosInitialized = false;
    private ArrayList conditionalFormats;
    private DataValidation dataValidation;
    private ArrayList drawings;
    private File excelFile;
    private FormattingRecords formattingRecords;
    private boolean hidden;
    private ArrayList hyperlinks = new ArrayList();
    private ArrayList localNames;
    private int maxColumnOutlineLevel;
    private int maxRowOutlineLevel;
    private Range[] mergedCells;
    private String name;
    private boolean nineteenFour;
    private int numCols;
    private int numRows;
    private PLSRecord plsRecord;
    private int[] rowBreaks;
    private ArrayList rowProperties = new ArrayList(10);
    private boolean rowRecordsInitialized = false;
    private SheetSettings settings;
    private ArrayList sharedFormulas = new ArrayList();
    private SSTRecord sharedStrings;
    private BOFRecord sheetBof;
    private int startPosition;
    private WorkbookParser workbook;
    private BOFRecord workbookBof;
    private WorkbookSettings workbookSettings;
    private WorkspaceInformationRecord workspaceOptions;

    SheetImpl(File f, SSTRecord sst, FormattingRecords fr, BOFRecord sb, BOFRecord wb, boolean nf, WorkbookParser wp) throws BiffException {
        this.excelFile = f;
        this.sharedStrings = sst;
        this.formattingRecords = fr;
        this.sheetBof = sb;
        this.workbookBof = wb;
        this.nineteenFour = nf;
        this.workbook = wp;
        this.workbookSettings = this.workbook.getSettings();
        this.startPosition = f.getPos();
        if (this.sheetBof.isChart()) {
            this.startPosition -= this.sheetBof.getLength() + 4;
        }
        int bofs = 1;
        while (bofs >= 1) {
            Record r = f.next();
            if (r.getCode() == Type.EOF.value) {
                bofs--;
            }
            if (r.getCode() == Type.BOF.value) {
                bofs++;
            }
        }
    }

    public Cell getCell(int column, int row) {
        if (this.cells == null) {
            readSheet();
        }
        Cell c = this.cells[row][column];
        if (c != null) {
            return c;
        }
        c = new EmptyCell(column, row);
        this.cells[row][column] = c;
        return c;
    }

    public int getRows() {
        if (this.cells == null) {
            readSheet();
        }
        return this.numRows;
    }

    public int getColumns() {
        if (this.cells == null) {
            readSheet();
        }
        return this.numCols;
    }

    public Cell[] getRow(int row) {
        if (this.cells == null) {
            readSheet();
        }
        boolean found = false;
        int col = this.numCols - 1;
        while (col >= 0 && !found) {
            if (this.cells[row][col] == null) {
                col--;
            } else {
                found = true;
            }
        }
        Cell[] c = new Cell[(col + 1)];
        for (int i = 0; i <= col; i++) {
            c[i] = getCell(i, row);
        }
        return c;
    }

    public String getName() {
        return this.name;
    }

    final void setName(String s) {
        this.name = s;
    }

    public ColumnInfoRecord[] getColumnInfos() {
        ColumnInfoRecord[] infos = new ColumnInfoRecord[this.columnInfosArray.size()];
        for (int i = 0; i < this.columnInfosArray.size(); i++) {
            infos[i] = (ColumnInfoRecord) this.columnInfosArray.get(i);
        }
        return infos;
    }

    final void setHidden(boolean h) {
        this.hidden = h;
    }

    final void clear() {
        this.cells = (Cell[][]) null;
        this.mergedCells = null;
        this.columnInfosArray.clear();
        this.sharedFormulas.clear();
        this.hyperlinks.clear();
        this.columnInfosInitialized = false;
        if (!this.workbookSettings.getGCDisabled()) {
            System.gc();
        }
    }

    final void readSheet() {
        if (!this.sheetBof.isWorksheet()) {
            this.numRows = 0;
            this.numCols = 0;
            this.cells = (Cell[][]) Array.newInstance(Cell.class, new int[]{0, 0});
        }
        SheetReader reader = new SheetReader(this.excelFile, this.sharedStrings, this.formattingRecords, this.sheetBof, this.workbookBof, this.nineteenFour, this.workbook, this.startPosition, this);
        reader.read();
        this.numRows = reader.getNumRows();
        this.numCols = reader.getNumCols();
        this.cells = reader.getCells();
        this.rowProperties = reader.getRowProperties();
        this.columnInfosArray = reader.getColumnInfosArray();
        this.hyperlinks = reader.getHyperlinks();
        this.conditionalFormats = reader.getConditionalFormats();
        this.autoFilter = reader.getAutoFilter();
        this.charts = reader.getCharts();
        this.drawings = reader.getDrawings();
        this.dataValidation = reader.getDataValidation();
        this.mergedCells = reader.getMergedCells();
        this.settings = reader.getSettings();
        this.settings.setHidden(this.hidden);
        this.rowBreaks = reader.getRowBreaks();
        this.columnBreaks = reader.getColumnBreaks();
        this.workspaceOptions = reader.getWorkspaceOptions();
        this.plsRecord = reader.getPLS();
        this.buttonPropertySet = reader.getButtonPropertySet();
        this.maxRowOutlineLevel = reader.getMaxRowOutlineLevel();
        this.maxColumnOutlineLevel = reader.getMaxColumnOutlineLevel();
        if (!this.workbookSettings.getGCDisabled()) {
            System.gc();
        }
        if (this.columnInfosArray.size() <= 0) {
            this.columnInfos = new ColumnInfoRecord[0];
        } else {
            this.columnInfos = new ColumnInfoRecord[(((ColumnInfoRecord) this.columnInfosArray.get(this.columnInfosArray.size() - 1)).getEndColumn() + 1)];
        }
        if (this.localNames != null) {
            Iterator it = this.localNames.iterator();
            while (it.hasNext()) {
                NameRecord nr = (NameRecord) it.next();
                NameRange rng;
                if (nr.getBuiltInName() != BuiltInName.PRINT_AREA) {
                    if (nr.getBuiltInName() == BuiltInName.PRINT_TITLES) {
                        for (NameRange rng2 : nr.getRanges()) {
                            if (rng2.getFirstColumn() == 0 && rng2.getLastColumn() == Light.MAIN_KEY_MAX) {
                                this.settings.setPrintTitlesRow(rng2.getFirstRow(), rng2.getLastRow());
                            } else {
                                this.settings.setPrintTitlesCol(rng2.getFirstColumn(), rng2.getLastColumn());
                            }
                        }
                    }
                } else if (nr.getRanges().length > 0) {
                    rng2 = nr.getRanges()[0];
                    this.settings.setPrintArea(rng2.getFirstColumn(), rng2.getFirstRow(), rng2.getLastColumn(), rng2.getLastRow());
                }
            }
        }
    }

    public Hyperlink[] getHyperlinks() {
        Hyperlink[] hl = new Hyperlink[this.hyperlinks.size()];
        for (int i = 0; i < this.hyperlinks.size(); i++) {
            hl[i] = (Hyperlink) this.hyperlinks.get(i);
        }
        return hl;
    }

    public Range[] getMergedCells() {
        if (this.mergedCells != null) {
            return this.mergedCells;
        }
        return new Range[0];
    }

    public RowRecord[] getRowProperties() {
        RowRecord[] rp = new RowRecord[this.rowProperties.size()];
        for (int i = 0; i < rp.length; i++) {
            rp[i] = (RowRecord) this.rowProperties.get(i);
        }
        return rp;
    }

    public DataValidation getDataValidation() {
        return this.dataValidation;
    }

    public final int[] getRowPageBreaks() {
        return this.rowBreaks;
    }

    public final int[] getColumnPageBreaks() {
        return this.columnBreaks;
    }

    public final Chart[] getCharts() {
        Chart[] ch = new Chart[this.charts.size()];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = (Chart) this.charts.get(i);
        }
        return ch;
    }

    public final DrawingGroupObject[] getDrawings() {
        return (DrawingGroupObject[]) this.drawings.toArray(new DrawingGroupObject[this.drawings.size()]);
    }

    public WorkspaceInformationRecord getWorkspaceOptions() {
        return this.workspaceOptions;
    }

    public SheetSettings getSettings() {
        return this.settings;
    }

    public WorkbookParser getWorkbook() {
        return this.workbook;
    }

    public BOFRecord getSheetBof() {
        return this.sheetBof;
    }

    public BOFRecord getWorkbookBof() {
        return this.workbookBof;
    }

    public PLSRecord getPLS() {
        return this.plsRecord;
    }

    public ButtonPropertySetRecord getButtonPropertySet() {
        return this.buttonPropertySet;
    }

    void addLocalName(NameRecord nr) {
        if (this.localNames == null) {
            this.localNames = new ArrayList();
        }
        this.localNames.add(nr);
    }

    public ConditionalFormat[] getConditionalFormats() {
        return (ConditionalFormat[]) this.conditionalFormats.toArray(new ConditionalFormat[this.conditionalFormats.size()]);
    }

    public AutoFilter getAutoFilter() {
        return this.autoFilter;
    }

    public int getMaxColumnOutlineLevel() {
        return this.maxColumnOutlineLevel;
    }

    public int getMaxRowOutlineLevel() {
        return this.maxRowOutlineLevel;
    }
}
