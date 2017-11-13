package jxl.write.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import jxl.Cell;
import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.WorkbookSettings;
import jxl.biff.AutoFilter;
import jxl.biff.DVParser;
import jxl.biff.DataValidation;
import jxl.biff.EmptyCell;
import jxl.biff.FormattingRecords;
import jxl.biff.IndexMapping;
import jxl.biff.drawing.Chart;
import jxl.biff.drawing.ComboBox;
import jxl.biff.drawing.Drawing;
import jxl.biff.drawing.DrawingGroupObject;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.format.Font;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

class WritableSheetImpl implements WritableSheet {
    private static final char[] illegalSheetNameCharacters = new char[]{'*', ':', '?', '\\'};
    private static final String[] imageTypes = new String[]{"png"};
    private static Logger logger = Logger.getLogger(WritableSheetImpl.class);
    private AutoFilter autoFilter;
    private TreeSet autosizedColumns;
    private ButtonPropertySetRecord buttonPropertySet;
    private boolean chartOnly = false;
    private ArrayList columnBreaks;
    private TreeSet columnFormats;
    private ComboBox comboBox;
    private ArrayList conditionalFormats;
    private DataValidation dataValidation;
    private ArrayList drawings;
    private boolean drawingsModified;
    private FormattingRecords formatRecords;
    private ArrayList hyperlinks;
    private ArrayList images;
    private int maxColumnOutlineLevel;
    private int maxRowOutlineLevel;
    private MergedCells mergedCells;
    private String name;
    private int numColumns = 0;
    private int numRows = 0;
    private File outputFile;
    private PLSRecord plsRecord;
    private ArrayList rowBreaks;
    private RowRecord[] rows = new RowRecord[0];
    private SheetSettings settings;
    private SharedStrings sharedStrings;
    private SheetWriter sheetWriter;
    private ArrayList validatedCells;
    private WritableWorkbookImpl workbook;
    private WorkbookSettings workbookSettings;

    private static class ColumnInfoComparator implements Comparator {
        private ColumnInfoComparator() {
        }

        public boolean equals(Object o) {
            return o == this;
        }

        public int compare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }
            Assert.verify(o1 instanceof ColumnInfoRecord);
            Assert.verify(o2 instanceof ColumnInfoRecord);
            return ((ColumnInfoRecord) o1).getColumn() - ((ColumnInfoRecord) o2).getColumn();
        }
    }

    public WritableSheetImpl(String n, File of, FormattingRecords fr, SharedStrings ss, WorkbookSettings ws, WritableWorkbookImpl ww) {
        this.name = validateName(n);
        this.outputFile = of;
        this.workbook = ww;
        this.formatRecords = fr;
        this.sharedStrings = ss;
        this.workbookSettings = ws;
        this.drawingsModified = false;
        this.columnFormats = new TreeSet(new ColumnInfoComparator());
        this.autosizedColumns = new TreeSet();
        this.hyperlinks = new ArrayList();
        this.mergedCells = new MergedCells(this);
        this.rowBreaks = new ArrayList();
        this.columnBreaks = new ArrayList();
        this.drawings = new ArrayList();
        this.images = new ArrayList();
        this.conditionalFormats = new ArrayList();
        this.validatedCells = new ArrayList();
        this.settings = new SheetSettings(this);
        this.sheetWriter = new SheetWriter(this.outputFile, this, this.workbookSettings);
    }

    public Cell getCell(int column, int row) {
        return getWritableCell(column, row);
    }

    public WritableCell getWritableCell(int column, int row) {
        WritableCell c = null;
        if (row < this.rows.length && this.rows[row] != null) {
            c = this.rows[row].getCell(column);
        }
        if (c != null) {
            return c;
        }
        return new EmptyCell(column, row);
    }

    public int getRows() {
        return this.numRows;
    }

    public int getColumns() {
        return this.numColumns;
    }

    public String getName() {
        return this.name;
    }

    public void addCell(WritableCell cell) throws WriteException, RowsExceededException {
        boolean curSharedValidation = false;
        if (cell.getType() != CellType.EMPTY || cell == null || cell.getCellFormat() != null) {
            CellValue cv = (CellValue) cell;
            if (cv.isReferenced()) {
                throw new JxlWriteException(JxlWriteException.cellReferenced);
            }
            int row = cell.getRow();
            RowRecord rowrec = getRowRecord(row);
            CellValue curcell = rowrec.getCell(cv.getColumn());
            if (!(curcell == null || curcell.getCellFeatures() == null || curcell.getCellFeatures().getDVParser() == null || !curcell.getCellFeatures().getDVParser().extendedCellsValidation())) {
                curSharedValidation = true;
            }
            if (cell.getCellFeatures() != null && cell.getCellFeatures().hasDataValidation() && curSharedValidation) {
                DVParser dvp = curcell.getCellFeatures().getDVParser();
                logger.warn("Cannot add cell at " + CellReferenceHelper.getCellReference(cv) + " because it is part of the shared cell validation group " + CellReferenceHelper.getCellReference(dvp.getFirstColumn(), dvp.getFirstRow()) + "-" + CellReferenceHelper.getCellReference(dvp.getLastColumn(), dvp.getLastRow()));
                return;
            }
            if (curSharedValidation) {
                WritableCellFeatures wcf = cell.getWritableCellFeatures();
                if (wcf == null) {
                    wcf = new WritableCellFeatures();
                    cell.setCellFeatures(wcf);
                }
                wcf.shareDataValidation(curcell.getCellFeatures());
            }
            rowrec.addCell(cv);
            this.numRows = Math.max(row + 1, this.numRows);
            this.numColumns = Math.max(this.numColumns, rowrec.getMaxColumn());
            cv.setCellDetails(this.formatRecords, this.sharedStrings, this);
        }
    }

    RowRecord getRowRecord(int row) throws RowsExceededException {
        if (row < 65536) {
            if (row >= this.rows.length) {
                RowRecord[] oldRows = this.rows;
                this.rows = new RowRecord[Math.max(oldRows.length + 10, row + 1)];
                System.arraycopy(oldRows, 0, this.rows, 0, oldRows.length);
            }
            RowRecord rowrec = this.rows[row];
            if (rowrec != null) {
                return rowrec;
            }
            rowrec = new RowRecord(row, this);
            this.rows[row] = rowrec;
            return rowrec;
        }
        throw new RowsExceededException();
    }

    ColumnInfoRecord getColumnInfo(int c) {
        Iterator i = this.columnFormats.iterator();
        ColumnInfoRecord cir = null;
        boolean stop = false;
        while (i.hasNext() && !stop) {
            cir = (ColumnInfoRecord) i.next();
            if (cir.getColumn() >= c) {
                stop = true;
            }
        }
        if (!stop) {
            return null;
        }
        if (cir.getColumn() != c) {
            cir = null;
        }
        return cir;
    }

    public void write() throws IOException {
        boolean dmod = this.drawingsModified;
        if (this.workbook.getDrawingGroup() != null) {
            dmod |= this.workbook.getDrawingGroup().hasDrawingsOmitted();
        }
        if (this.autosizedColumns.size() > 0) {
            autosizeColumns();
        }
        this.sheetWriter.setWriteData(this.rows, this.rowBreaks, this.columnBreaks, this.hyperlinks, this.mergedCells, this.columnFormats, this.maxRowOutlineLevel, this.maxColumnOutlineLevel);
        this.sheetWriter.setDimensions(getRows(), getColumns());
        this.sheetWriter.setSettings(this.settings);
        this.sheetWriter.setPLS(this.plsRecord);
        this.sheetWriter.setDrawings(this.drawings, dmod);
        this.sheetWriter.setButtonPropertySet(this.buttonPropertySet);
        this.sheetWriter.setDataValidation(this.dataValidation, this.validatedCells);
        this.sheetWriter.setConditionalFormats(this.conditionalFormats);
        this.sheetWriter.setAutoFilter(this.autoFilter);
        this.sheetWriter.write();
    }

    void copy(Sheet s) {
        this.settings = new SheetSettings(s.getSettings(), this);
        SheetCopier si = new SheetCopier(s, this);
        si.setColumnFormats(this.columnFormats);
        si.setFormatRecords(this.formatRecords);
        si.setHyperlinks(this.hyperlinks);
        si.setMergedCells(this.mergedCells);
        si.setRowBreaks(this.rowBreaks);
        si.setColumnBreaks(this.columnBreaks);
        si.setSheetWriter(this.sheetWriter);
        si.setDrawings(this.drawings);
        si.setImages(this.images);
        si.setConditionalFormats(this.conditionalFormats);
        si.setValidatedCells(this.validatedCells);
        si.copySheet();
        this.dataValidation = si.getDataValidation();
        this.comboBox = si.getComboBox();
        this.plsRecord = si.getPLSRecord();
        this.chartOnly = si.isChartOnly();
        this.buttonPropertySet = si.getButtonPropertySet();
        this.numRows = si.getRows();
        this.autoFilter = si.getAutoFilter();
        this.maxRowOutlineLevel = si.getMaxRowOutlineLevel();
        this.maxColumnOutlineLevel = si.getMaxColumnOutlineLevel();
    }

    public SheetSettings getSettings() {
        return this.settings;
    }

    WorkbookSettings getWorkbookSettings() {
        return this.workbookSettings;
    }

    Chart[] getCharts() {
        return this.sheetWriter.getCharts();
    }

    void checkMergedBorders() {
        this.sheetWriter.setWriteData(this.rows, this.rowBreaks, this.columnBreaks, this.hyperlinks, this.mergedCells, this.columnFormats, this.maxRowOutlineLevel, this.maxColumnOutlineLevel);
        this.sheetWriter.setDimensions(getRows(), getColumns());
        this.sheetWriter.checkMergedBorders();
    }

    void rationalize(IndexMapping xfMapping, IndexMapping fontMapping, IndexMapping formatMapping) {
        Iterator i = this.columnFormats.iterator();
        while (i.hasNext()) {
            ((ColumnInfoRecord) i.next()).rationalize(xfMapping);
        }
        for (int i2 = 0; i2 < this.rows.length; i2++) {
            if (this.rows[i2] != null) {
                this.rows[i2].rationalize(xfMapping);
            }
        }
        Chart[] charts = getCharts();
        for (Chart rationalize : charts) {
            rationalize.rationalize(xfMapping, fontMapping, formatMapping);
        }
    }

    WritableWorkbookImpl getWorkbook() {
        return this.workbook;
    }

    boolean isChartOnly() {
        return this.chartOnly;
    }

    private String validateName(String n) {
        if (n.length() > 31) {
            logger.warn("Sheet name " + n + " too long - truncating");
            n = n.substring(0, 31);
        }
        if (n.charAt(0) == '\'') {
            logger.warn("Sheet naming cannot start with ' - removing");
            n = n.substring(1);
        }
        for (int i = 0; i < illegalSheetNameCharacters.length; i++) {
            String newname = n.replace(illegalSheetNameCharacters[i], '@');
            if (n != newname) {
                logger.warn(illegalSheetNameCharacters[i] + " is not a valid character within a sheet name - replacing");
            }
            n = newname;
        }
        return n;
    }

    void addDrawing(DrawingGroupObject o) {
        boolean z = false;
        this.drawings.add(o);
        if (!(o instanceof Drawing)) {
            z = true;
        }
        Assert.verify(z);
    }

    void removeDrawing(DrawingGroupObject o) {
        boolean z = true;
        int origSize = this.drawings.size();
        this.drawings.remove(o);
        int newSize = this.drawings.size();
        this.drawingsModified = true;
        if (newSize != origSize - 1) {
            z = false;
        }
        Assert.verify(z);
    }

    void removeDataValidation(CellValue cv) {
        if (this.dataValidation != null) {
            this.dataValidation.removeDataValidation(cv.getColumn(), cv.getRow());
        }
        if (this.validatedCells != null && !this.validatedCells.remove(cv)) {
            logger.warn("Could not remove validated cell " + CellReferenceHelper.getCellReference(cv));
        }
    }

    void addValidationCell(CellValue cv) {
        this.validatedCells.add(cv);
    }

    ComboBox getComboBox() {
        return this.comboBox;
    }

    void setComboBox(ComboBox cb) {
        this.comboBox = cb;
    }

    private void autosizeColumns() {
        Iterator i = this.autosizedColumns.iterator();
        while (i.hasNext()) {
            autosizeColumn(((Integer) i.next()).intValue());
        }
    }

    private void autosizeColumn(int col) {
        int maxWidth = 0;
        ColumnInfoRecord cir = getColumnInfo(col);
        Font columnFont = cir.getCellFormat().getFont();
        Font defaultFont = WritableWorkbook.NORMAL_STYLE.getFont();
        for (int i = 0; i < this.numRows; i++) {
            Cell cell = null;
            if (this.rows[i] != null) {
                cell = this.rows[i].getCell(col);
            }
            if (cell != null) {
                Font activeFont;
                String contents = cell.getContents();
                Font font = cell.getCellFormat().getFont();
                if (font.equals(defaultFont)) {
                    activeFont = columnFont;
                } else {
                    activeFont = font;
                }
                int pointSize = activeFont.getPointSize();
                int numChars = contents.length();
                if (activeFont.isItalic() || activeFont.getBoldWeight() > 400) {
                    numChars += 2;
                }
                maxWidth = Math.max(maxWidth, (numChars * pointSize) * 256);
            }
        }
        cir.setWidth(maxWidth / defaultFont.getPointSize());
    }
}
