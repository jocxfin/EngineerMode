package jxl.write.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Range;
import jxl.SheetSettings;
import jxl.WorkbookSettings;
import jxl.biff.AutoFilter;
import jxl.biff.ConditionalFormat;
import jxl.biff.DataValidation;
import jxl.biff.DataValiditySettingsRecord;
import jxl.biff.WorkspaceInformationRecord;
import jxl.biff.XFRecord;
import jxl.biff.drawing.Chart;
import jxl.biff.drawing.SheetDrawingWriter;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.Blank;
import jxl.write.WritableCell;
import jxl.write.WritableHyperlink;
import jxl.write.WriteException;

final class SheetWriter {
    private static Logger logger = Logger.getLogger(SheetWriter.class);
    private AutoFilter autoFilter;
    private ButtonPropertySetRecord buttonPropertySet;
    private boolean chartOnly;
    private ArrayList columnBreaks;
    private TreeSet columnFormats;
    private ArrayList conditionalFormats;
    private DataValidation dataValidation;
    private SheetDrawingWriter drawingWriter;
    private ArrayList hyperlinks;
    private int maxColumnOutlineLevel;
    private int maxRowOutlineLevel;
    private MergedCells mergedCells;
    private int numCols;
    private int numRows;
    private File outputFile;
    private PLSRecord plsRecord;
    private ArrayList rowBreaks;
    private RowRecord[] rows;
    private SheetSettings settings;
    private WritableSheetImpl sheet;
    private ArrayList validatedCells;
    private WorkbookSettings workbookSettings;
    private WorkspaceInformationRecord workspaceOptions = new WorkspaceInformationRecord();

    public SheetWriter(File of, WritableSheetImpl wsi, WorkbookSettings ws) {
        this.outputFile = of;
        this.sheet = wsi;
        this.workbookSettings = ws;
        this.chartOnly = false;
        this.drawingWriter = new SheetDrawingWriter(ws);
    }

    public void write() throws IOException {
        boolean z;
        if (this.rows == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        if (this.chartOnly) {
            this.drawingWriter.write(this.outputFile);
            return;
        }
        int[] rb;
        int i;
        this.outputFile.write(new BOFRecord(BOFRecord.sheet));
        int numBlocks = this.numRows / 32;
        if (this.numRows - (numBlocks * 32) != 0) {
            numBlocks++;
        }
        int indexPos = this.outputFile.getPos();
        IndexRecord indexRecord = new IndexRecord(0, this.numRows, numBlocks);
        this.outputFile.write(indexRecord);
        if (this.settings.getAutomaticFormulaCalculation()) {
            this.outputFile.write(new CalcModeRecord(CalcModeRecord.automatic));
        } else {
            this.outputFile.write(new CalcModeRecord(CalcModeRecord.manual));
        }
        this.outputFile.write(new CalcCountRecord(100));
        this.outputFile.write(new RefModeRecord());
        this.outputFile.write(new IterationRecord(false));
        this.outputFile.write(new DeltaRecord(0.001d));
        this.outputFile.write(new SaveRecalcRecord(this.settings.getRecalculateFormulasBeforeSave()));
        this.outputFile.write(new PrintHeadersRecord(this.settings.getPrintHeaders()));
        this.outputFile.write(new PrintGridLinesRecord(this.settings.getPrintGridLines()));
        this.outputFile.write(new GridSetRecord(true));
        GuttersRecord gutr = new GuttersRecord();
        gutr.setMaxColumnOutline(this.maxColumnOutlineLevel + 1);
        gutr.setMaxRowOutline(this.maxRowOutlineLevel + 1);
        this.outputFile.write(gutr);
        int defaultRowHeight = this.settings.getDefaultRowHeight();
        if (this.settings.getDefaultRowHeight() == 255) {
            z = false;
        } else {
            z = true;
        }
        this.outputFile.write(new DefaultRowHeightRecord(defaultRowHeight, z));
        if (this.maxRowOutlineLevel > 0) {
            this.workspaceOptions.setRowOutlines(true);
        }
        if (this.maxColumnOutlineLevel > 0) {
            this.workspaceOptions.setColumnOutlines(true);
        }
        this.workspaceOptions.setFitToPages(this.settings.getFitToPages());
        this.outputFile.write(this.workspaceOptions);
        if (this.rowBreaks.size() > 0) {
            rb = new int[this.rowBreaks.size()];
            for (i = 0; i < rb.length; i++) {
                rb[i] = ((Integer) this.rowBreaks.get(i)).intValue();
            }
            this.outputFile.write(new HorizontalPageBreaksRecord(rb));
        }
        if (this.columnBreaks.size() > 0) {
            rb = new int[this.columnBreaks.size()];
            for (i = 0; i < rb.length; i++) {
                rb[i] = ((Integer) this.columnBreaks.get(i)).intValue();
            }
            this.outputFile.write(new VerticalPageBreaksRecord(rb));
        }
        this.outputFile.write(new HeaderRecord(this.settings.getHeader().toString()));
        this.outputFile.write(new FooterRecord(this.settings.getFooter().toString()));
        this.outputFile.write(new HorizontalCentreRecord(this.settings.isHorizontalCentre()));
        this.outputFile.write(new VerticalCentreRecord(this.settings.isVerticalCentre()));
        if (this.settings.getLeftMargin() != this.settings.getDefaultWidthMargin()) {
            this.outputFile.write(new LeftMarginRecord(this.settings.getLeftMargin()));
        }
        if (this.settings.getRightMargin() != this.settings.getDefaultWidthMargin()) {
            this.outputFile.write(new RightMarginRecord(this.settings.getRightMargin()));
        }
        if (this.settings.getTopMargin() != this.settings.getDefaultHeightMargin()) {
            this.outputFile.write(new TopMarginRecord(this.settings.getTopMargin()));
        }
        if (this.settings.getBottomMargin() != this.settings.getDefaultHeightMargin()) {
            this.outputFile.write(new BottomMarginRecord(this.settings.getBottomMargin()));
        }
        if (this.plsRecord != null) {
            this.outputFile.write(this.plsRecord);
        }
        this.outputFile.write(new SetupRecord(this.settings));
        if (this.settings.isProtected()) {
            this.outputFile.write(new ProtectRecord(this.settings.isProtected()));
            this.outputFile.write(new ScenarioProtectRecord(this.settings.isProtected()));
            this.outputFile.write(new ObjectProtectRecord(this.settings.isProtected()));
            if (this.settings.getPassword() != null) {
                this.outputFile.write(new PasswordRecord(this.settings.getPassword()));
            } else if (this.settings.getPasswordHash() != 0) {
                this.outputFile.write(new PasswordRecord(this.settings.getPasswordHash()));
            }
        }
        indexRecord.setDataStartPosition(this.outputFile.getPos());
        this.outputFile.write(new DefaultColumnWidth(this.settings.getDefaultColumnWidth()));
        XFRecord normalStyle = this.sheet.getWorkbook().getStyles().getNormalStyle();
        CellFormat defaultDateFormat = this.sheet.getWorkbook().getStyles().getDefaultDateFormat();
        Iterator colit = this.columnFormats.iterator();
        while (colit.hasNext()) {
            ColumnInfoRecord cir = (ColumnInfoRecord) colit.next();
            if (cir.getColumn() < 256) {
                this.outputFile.write(cir);
            }
            XFRecord xfr = cir.getCellFormat();
            if (xfr != normalStyle && cir.getColumn() < 256) {
                Cell[] cells = getColumn(cir.getColumn());
                i = 0;
                while (i < cells.length) {
                    if (cells[i] != null) {
                        if (cells[i].getCellFormat() == normalStyle || cells[i].getCellFormat() == defaultDateFormat) {
                            ((WritableCell) cells[i]).setCellFormat(xfr);
                        }
                    }
                    i++;
                }
            }
        }
        if (this.autoFilter != null) {
            this.autoFilter.write(this.outputFile);
        }
        this.outputFile.write(new DimensionRecord(this.numRows, this.numCols));
        for (int block = 0; block < numBlocks; block++) {
            DBCellRecord dbcell = new DBCellRecord(this.outputFile.getPos());
            int blockRows = Math.min(32, this.numRows - (block * 32));
            boolean firstRow = true;
            for (i = block * 32; i < (block * 32) + blockRows; i++) {
                if (this.rows[i] != null) {
                    this.rows[i].write(this.outputFile);
                    if (firstRow) {
                        dbcell.setCellOffset(this.outputFile.getPos());
                        firstRow = false;
                    }
                }
            }
            for (i = block * 32; i < (block * 32) + blockRows; i++) {
                if (this.rows[i] != null) {
                    dbcell.addCellRowPosition(this.outputFile.getPos());
                    this.rows[i].writeCells(this.outputFile);
                }
            }
            indexRecord.addBlockPosition(this.outputFile.getPos());
            dbcell.setPosition(this.outputFile.getPos());
            this.outputFile.write(dbcell);
        }
        if (!this.workbookSettings.getDrawingsDisabled()) {
            this.drawingWriter.write(this.outputFile);
        }
        this.outputFile.write(new Window2Record(this.settings));
        if (this.settings.getHorizontalFreeze() == 0 && this.settings.getVerticalFreeze() == 0) {
            this.outputFile.write(new SelectionRecord(SelectionRecord.upperLeft, 0, 0));
        } else {
            this.outputFile.write(new PaneRecord(this.settings.getHorizontalFreeze(), this.settings.getVerticalFreeze()));
            this.outputFile.write(new SelectionRecord(SelectionRecord.upperLeft, 0, 0));
            if (this.settings.getHorizontalFreeze() != 0) {
                this.outputFile.write(new SelectionRecord(SelectionRecord.upperRight, this.settings.getHorizontalFreeze(), 0));
            }
            if (this.settings.getVerticalFreeze() != 0) {
                this.outputFile.write(new SelectionRecord(SelectionRecord.lowerLeft, 0, this.settings.getVerticalFreeze()));
            }
            if (!(this.settings.getHorizontalFreeze() == 0 || this.settings.getVerticalFreeze() == 0)) {
                this.outputFile.write(new SelectionRecord(SelectionRecord.lowerRight, this.settings.getHorizontalFreeze(), this.settings.getVerticalFreeze()));
            }
            this.outputFile.write(new Weird1Record());
        }
        if (this.settings.getZoomFactor() != 100) {
            this.outputFile.write(new SCLRecord(this.settings.getZoomFactor()));
        }
        this.mergedCells.write(this.outputFile);
        Iterator hi = this.hyperlinks.iterator();
        while (hi.hasNext()) {
            this.outputFile.write((WritableHyperlink) hi.next());
        }
        if (this.buttonPropertySet != null) {
            this.outputFile.write(this.buttonPropertySet);
        }
        if (this.dataValidation != null || this.validatedCells.size() > 0) {
            writeDataValidation();
        }
        if (this.conditionalFormats != null && this.conditionalFormats.size() > 0) {
            Iterator i2 = this.conditionalFormats.iterator();
            while (i2.hasNext()) {
                ((ConditionalFormat) i2.next()).write(this.outputFile);
            }
        }
        this.outputFile.write(new EOFRecord());
        this.outputFile.setData(indexRecord.getData(), indexPos + 4);
    }

    void setWriteData(RowRecord[] rws, ArrayList rb, ArrayList cb, ArrayList hl, MergedCells mc, TreeSet cf, int mrol, int mcol) {
        this.rows = rws;
        this.rowBreaks = rb;
        this.columnBreaks = cb;
        this.hyperlinks = hl;
        this.mergedCells = mc;
        this.columnFormats = cf;
        this.maxRowOutlineLevel = mrol;
        this.maxColumnOutlineLevel = mcol;
    }

    void setDimensions(int rws, int cls) {
        this.numRows = rws;
        this.numCols = cls;
    }

    void setSettings(SheetSettings sr) {
        this.settings = sr;
    }

    void setWorkspaceOptions(WorkspaceInformationRecord wo) {
        if (wo != null) {
            this.workspaceOptions = wo;
        }
    }

    void setCharts(Chart[] ch) {
        this.drawingWriter.setCharts(ch);
    }

    void setDrawings(ArrayList dr, boolean mod) {
        this.drawingWriter.setDrawings(dr, mod);
    }

    Chart[] getCharts() {
        return this.drawingWriter.getCharts();
    }

    void checkMergedBorders() {
        Range[] mcells = this.mergedCells.getMergedCells();
        ArrayList borderFormats = new ArrayList();
        for (Range range : mcells) {
            Cell topLeft = range.getTopLeft();
            XFRecord tlformat = (XFRecord) topLeft.getCellFormat();
            if (!(tlformat == null || !tlformat.hasBorders() || tlformat.isRead())) {
                try {
                    int i;
                    CellXFRecord cf1 = new CellXFRecord(tlformat);
                    Cell bottomRight = range.getBottomRight();
                    cf1.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                    cf1.setBorder(Border.LEFT, tlformat.getBorderLine(Border.LEFT), tlformat.getBorderColour(Border.LEFT));
                    cf1.setBorder(Border.TOP, tlformat.getBorderLine(Border.TOP), tlformat.getBorderColour(Border.TOP));
                    if (topLeft.getRow() == bottomRight.getRow()) {
                        cf1.setBorder(Border.BOTTOM, tlformat.getBorderLine(Border.BOTTOM), tlformat.getBorderColour(Border.BOTTOM));
                    }
                    if (topLeft.getColumn() == bottomRight.getColumn()) {
                        cf1.setBorder(Border.RIGHT, tlformat.getBorderLine(Border.RIGHT), tlformat.getBorderColour(Border.RIGHT));
                    }
                    int index = borderFormats.indexOf(cf1);
                    if (index == -1) {
                        borderFormats.add(cf1);
                    } else {
                        cf1 = (CellXFRecord) borderFormats.get(index);
                    }
                    ((WritableCell) topLeft).setCellFormat(cf1);
                    if (bottomRight.getRow() > topLeft.getRow()) {
                        if (bottomRight.getColumn() != topLeft.getColumn()) {
                            CellXFRecord cf2 = new CellXFRecord(tlformat);
                            cf2.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf2.setBorder(Border.LEFT, tlformat.getBorderLine(Border.LEFT), tlformat.getBorderColour(Border.LEFT));
                            cf2.setBorder(Border.BOTTOM, tlformat.getBorderLine(Border.BOTTOM), tlformat.getBorderColour(Border.BOTTOM));
                            index = borderFormats.indexOf(cf2);
                            if (index == -1) {
                                borderFormats.add(cf2);
                            } else {
                                cf2 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(topLeft.getColumn(), bottomRight.getRow(), cf2));
                        }
                        for (i = topLeft.getRow() + 1; i < bottomRight.getRow(); i++) {
                            CellXFRecord cf3 = new CellXFRecord(tlformat);
                            cf3.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf3.setBorder(Border.LEFT, tlformat.getBorderLine(Border.LEFT), tlformat.getBorderColour(Border.LEFT));
                            if (topLeft.getColumn() == bottomRight.getColumn()) {
                                cf3.setBorder(Border.RIGHT, tlformat.getBorderLine(Border.RIGHT), tlformat.getBorderColour(Border.RIGHT));
                            }
                            index = borderFormats.indexOf(cf3);
                            if (index == -1) {
                                borderFormats.add(cf3);
                            } else {
                                cf3 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(topLeft.getColumn(), i, cf3));
                        }
                    }
                    if (bottomRight.getColumn() > topLeft.getColumn()) {
                        if (bottomRight.getRow() != topLeft.getRow()) {
                            CellXFRecord cf6 = new CellXFRecord(tlformat);
                            cf6.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf6.setBorder(Border.RIGHT, tlformat.getBorderLine(Border.RIGHT), tlformat.getBorderColour(Border.RIGHT));
                            cf6.setBorder(Border.TOP, tlformat.getBorderLine(Border.TOP), tlformat.getBorderColour(Border.TOP));
                            index = borderFormats.indexOf(cf6);
                            if (index == -1) {
                                borderFormats.add(cf6);
                            } else {
                                cf6 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(bottomRight.getColumn(), topLeft.getRow(), cf6));
                        }
                        for (i = topLeft.getRow() + 1; i < bottomRight.getRow(); i++) {
                            CellXFRecord cf7 = new CellXFRecord(tlformat);
                            cf7.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf7.setBorder(Border.RIGHT, tlformat.getBorderLine(Border.RIGHT), tlformat.getBorderColour(Border.RIGHT));
                            index = borderFormats.indexOf(cf7);
                            if (index == -1) {
                                borderFormats.add(cf7);
                            } else {
                                cf7 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(bottomRight.getColumn(), i, cf7));
                        }
                        for (i = topLeft.getColumn() + 1; i < bottomRight.getColumn(); i++) {
                            CellXFRecord cf8 = new CellXFRecord(tlformat);
                            cf8.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf8.setBorder(Border.TOP, tlformat.getBorderLine(Border.TOP), tlformat.getBorderColour(Border.TOP));
                            if (topLeft.getRow() == bottomRight.getRow()) {
                                cf8.setBorder(Border.BOTTOM, tlformat.getBorderLine(Border.BOTTOM), tlformat.getBorderColour(Border.BOTTOM));
                            }
                            index = borderFormats.indexOf(cf8);
                            if (index == -1) {
                                borderFormats.add(cf8);
                            } else {
                                cf8 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(i, topLeft.getRow(), cf8));
                        }
                    }
                    if (bottomRight.getColumn() > topLeft.getColumn() || bottomRight.getRow() > topLeft.getRow()) {
                        CellXFRecord cf4 = new CellXFRecord(tlformat);
                        cf4.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                        cf4.setBorder(Border.RIGHT, tlformat.getBorderLine(Border.RIGHT), tlformat.getBorderColour(Border.RIGHT));
                        cf4.setBorder(Border.BOTTOM, tlformat.getBorderLine(Border.BOTTOM), tlformat.getBorderColour(Border.BOTTOM));
                        if (bottomRight.getRow() == topLeft.getRow()) {
                            cf4.setBorder(Border.TOP, tlformat.getBorderLine(Border.TOP), tlformat.getBorderColour(Border.TOP));
                        }
                        if (bottomRight.getColumn() == topLeft.getColumn()) {
                            cf4.setBorder(Border.LEFT, tlformat.getBorderLine(Border.LEFT), tlformat.getBorderColour(Border.LEFT));
                        }
                        index = borderFormats.indexOf(cf4);
                        if (index == -1) {
                            borderFormats.add(cf4);
                        } else {
                            cf4 = (CellXFRecord) borderFormats.get(index);
                        }
                        this.sheet.addCell(new Blank(bottomRight.getColumn(), bottomRight.getRow(), cf4));
                        for (i = topLeft.getColumn() + 1; i < bottomRight.getColumn(); i++) {
                            CellXFRecord cf5 = new CellXFRecord(tlformat);
                            cf5.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
                            cf5.setBorder(Border.BOTTOM, tlformat.getBorderLine(Border.BOTTOM), tlformat.getBorderColour(Border.BOTTOM));
                            if (topLeft.getRow() == bottomRight.getRow()) {
                                cf5.setBorder(Border.TOP, tlformat.getBorderLine(Border.TOP), tlformat.getBorderColour(Border.TOP));
                            }
                            index = borderFormats.indexOf(cf5);
                            if (index == -1) {
                                borderFormats.add(cf5);
                            } else {
                                cf5 = (CellXFRecord) borderFormats.get(index);
                            }
                            this.sheet.addCell(new Blank(i, bottomRight.getRow(), cf5));
                        }
                    }
                } catch (WriteException e) {
                    logger.warn(e.toString());
                }
            }
        }
    }

    private Cell[] getColumn(int col) {
        boolean found = false;
        int row = this.numRows - 1;
        while (row >= 0 && !found) {
            if (this.rows[row] == null || this.rows[row].getCell(col) == null) {
                row--;
            } else {
                found = true;
            }
        }
        Cell[] cells = new Cell[(row + 1)];
        for (int i = 0; i <= row; i++) {
            cells[i] = this.rows[i] == null ? null : this.rows[i].getCell(col);
        }
        return cells;
    }

    void setChartOnly() {
        this.chartOnly = true;
    }

    void setPLS(PLSRecord pls) {
        this.plsRecord = pls;
    }

    void setButtonPropertySet(ButtonPropertySetRecord bps) {
        this.buttonPropertySet = bps;
    }

    void setDataValidation(DataValidation dv, ArrayList vc) {
        this.dataValidation = dv;
        this.validatedCells = vc;
    }

    void setConditionalFormats(ArrayList cf) {
        this.conditionalFormats = cf;
    }

    void setAutoFilter(AutoFilter af) {
        this.autoFilter = af;
    }

    private void writeDataValidation() throws IOException {
        if (this.dataValidation != null && this.validatedCells.size() == 0) {
            this.dataValidation.write(this.outputFile);
            return;
        }
        if (this.dataValidation == null && this.validatedCells.size() > 0) {
            this.dataValidation = new DataValidation(this.sheet.getComboBox() == null ? -1 : this.sheet.getComboBox().getObjectId(), this.sheet.getWorkbook(), this.sheet.getWorkbook(), this.workbookSettings);
        }
        Iterator i = this.validatedCells.iterator();
        while (i.hasNext()) {
            CellValue cv = (CellValue) i.next();
            CellFeatures cf = cv.getCellFeatures();
            if (!cf.getDVParser().copied()) {
                if (!cf.getDVParser().extendedCellsValidation()) {
                    this.dataValidation.add(new DataValiditySettingsRecord(cf.getDVParser()));
                } else if (cv.getColumn() == cf.getDVParser().getFirstColumn() && cv.getRow() == cf.getDVParser().getFirstRow()) {
                    this.dataValidation.add(new DataValiditySettingsRecord(cf.getDVParser()));
                }
            }
        }
        this.dataValidation.write(this.outputFile);
    }
}
