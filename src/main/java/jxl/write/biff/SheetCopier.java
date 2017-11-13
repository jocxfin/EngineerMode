package jxl.write.biff;

import java.util.ArrayList;
import java.util.TreeSet;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Hyperlink;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Range;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.biff.AutoFilter;
import jxl.biff.ConditionalFormat;
import jxl.biff.DataValidation;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.SheetRangeImpl;
import jxl.biff.XFRecord;
import jxl.biff.drawing.Button;
import jxl.biff.drawing.CheckBox;
import jxl.biff.drawing.ComboBox;
import jxl.biff.drawing.Comment;
import jxl.biff.drawing.Drawing;
import jxl.biff.drawing.DrawingGroupObject;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.ColumnInfoRecord;
import jxl.read.biff.RowRecord;
import jxl.read.biff.SheetImpl;
import jxl.write.Blank;
import jxl.write.Boolean;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

class SheetCopier {
    private static Logger logger = Logger.getLogger(SheetCopier.class);
    private AutoFilter autoFilter;
    private ButtonPropertySetRecord buttonPropertySet;
    private boolean chartOnly = false;
    private ArrayList columnBreaks;
    private TreeSet columnFormats;
    private ComboBox comboBox;
    private ArrayList conditionalFormats;
    private DataValidation dataValidation;
    private ArrayList drawings;
    private FormattingRecords formatRecords;
    private SheetImpl fromSheet;
    private ArrayList hyperlinks;
    private ArrayList images;
    private int maxColumnOutlineLevel;
    private int maxRowOutlineLevel;
    private MergedCells mergedCells;
    private int numRows;
    private PLSRecord plsRecord;
    private ArrayList rowBreaks;
    private SheetWriter sheetWriter;
    private WritableSheetImpl toSheet;
    private ArrayList validatedCells;
    private WorkbookSettings workbookSettings = this.toSheet.getWorkbook().getSettings();

    public SheetCopier(Sheet f, WritableSheet t) {
        this.fromSheet = (SheetImpl) f;
        this.toSheet = (WritableSheetImpl) t;
    }

    void setColumnFormats(TreeSet cf) {
        this.columnFormats = cf;
    }

    void setFormatRecords(FormattingRecords fr) {
        this.formatRecords = fr;
    }

    void setHyperlinks(ArrayList h) {
        this.hyperlinks = h;
    }

    void setMergedCells(MergedCells mc) {
        this.mergedCells = mc;
    }

    void setRowBreaks(ArrayList rb) {
        this.rowBreaks = rb;
    }

    void setColumnBreaks(ArrayList cb) {
        this.columnBreaks = cb;
    }

    void setSheetWriter(SheetWriter sw) {
        this.sheetWriter = sw;
    }

    void setDrawings(ArrayList d) {
        this.drawings = d;
    }

    void setImages(ArrayList i) {
        this.images = i;
    }

    void setConditionalFormats(ArrayList cf) {
        this.conditionalFormats = cf;
    }

    void setValidatedCells(ArrayList vc) {
        this.validatedCells = vc;
    }

    AutoFilter getAutoFilter() {
        return this.autoFilter;
    }

    DataValidation getDataValidation() {
        return this.dataValidation;
    }

    ComboBox getComboBox() {
        return this.comboBox;
    }

    PLSRecord getPLSRecord() {
        return this.plsRecord;
    }

    boolean isChartOnly() {
        return this.chartOnly;
    }

    ButtonPropertySetRecord getButtonPropertySet() {
        return this.buttonPropertySet;
    }

    public void copySheet() {
        int i;
        shallowCopyCells();
        ColumnInfoRecord[] readCirs = this.fromSheet.getColumnInfos();
        for (ColumnInfoRecord rcir : readCirs) {
            for (int j = rcir.getStartColumn(); j <= rcir.getEndColumn(); j++) {
                ColumnInfoRecord cir = new ColumnInfoRecord(rcir, j, this.formatRecords);
                cir.setHidden(rcir.getHidden());
                this.columnFormats.add(cir);
            }
        }
        Hyperlink[] hls = this.fromSheet.getHyperlinks();
        for (Hyperlink writableHyperlink : hls) {
            this.hyperlinks.add(new WritableHyperlink(writableHyperlink, this.toSheet));
        }
        Range[] merged = this.fromSheet.getMergedCells();
        for (Range range : merged) {
            this.mergedCells.add(new SheetRangeImpl((SheetRangeImpl) range, this.toSheet));
        }
        try {
            RowRecord[] rowprops = this.fromSheet.getRowProperties();
            for (i = 0; i < rowprops.length; i++) {
                XFRecord format;
                RowRecord rr = this.toSheet.getRowRecord(rowprops[i].getRowNumber());
                if (rowprops[i].hasDefaultFormat()) {
                    format = this.formatRecords.getXFRecord(rowprops[i].getXFIndex());
                } else {
                    format = null;
                }
                rr.setRowDetails(rowprops[i].getRowHeight(), rowprops[i].matchesDefaultFontHeight(), rowprops[i].isCollapsed(), rowprops[i].getOutlineLevel(), rowprops[i].getGroupStart(), format);
                this.numRows = Math.max(this.numRows, rowprops[i].getRowNumber() + 1);
            }
        } catch (RowsExceededException e) {
            Assert.verify(false);
        }
        int[] rowbreaks = this.fromSheet.getRowPageBreaks();
        if (rowbreaks != null) {
            for (int num : rowbreaks) {
                this.rowBreaks.add(new Integer(num));
            }
        }
        int[] columnbreaks = this.fromSheet.getColumnPageBreaks();
        if (columnbreaks != null) {
            for (int num2 : columnbreaks) {
                this.columnBreaks.add(new Integer(num2));
            }
        }
        this.sheetWriter.setCharts(this.fromSheet.getCharts());
        DrawingGroupObject[] dr = this.fromSheet.getDrawings();
        for (i = 0; i < dr.length; i++) {
            if (dr[i] instanceof Drawing) {
                WritableImage writableImage = new WritableImage(dr[i], this.toSheet.getWorkbook().getDrawingGroup());
                this.drawings.add(writableImage);
                this.images.add(writableImage);
            } else if (dr[i] instanceof Comment) {
                boolean z;
                Comment c = new Comment(dr[i], this.toSheet.getWorkbook().getDrawingGroup(), this.workbookSettings);
                this.drawings.add(c);
                CellValue cv = (CellValue) this.toSheet.getWritableCell(c.getColumn(), c.getRow());
                if (cv.getCellFeatures() == null) {
                    z = false;
                } else {
                    z = true;
                }
                Assert.verify(z);
                cv.getWritableCellFeatures().setCommentDrawing(c);
            } else if (dr[i] instanceof Button) {
                this.drawings.add(new Button(dr[i], this.toSheet.getWorkbook().getDrawingGroup(), this.workbookSettings));
            } else if (dr[i] instanceof ComboBox) {
                this.drawings.add(new ComboBox(dr[i], this.toSheet.getWorkbook().getDrawingGroup(), this.workbookSettings));
            } else if (dr[i] instanceof CheckBox) {
                this.drawings.add(new CheckBox(dr[i], this.toSheet.getWorkbook().getDrawingGroup(), this.workbookSettings));
            }
        }
        DataValidation rdv = this.fromSheet.getDataValidation();
        if (rdv != null) {
            this.dataValidation = new DataValidation(rdv, this.toSheet.getWorkbook(), this.toSheet.getWorkbook(), this.workbookSettings);
            int objid = this.dataValidation.getComboBoxObjectId();
            if (objid != 0) {
                this.comboBox = (ComboBox) this.drawings.get(objid);
            }
        }
        ConditionalFormat[] cf = this.fromSheet.getConditionalFormats();
        if (cf.length > 0) {
            for (Object add : cf) {
                this.conditionalFormats.add(add);
            }
        }
        this.autoFilter = this.fromSheet.getAutoFilter();
        this.sheetWriter.setWorkspaceOptions(this.fromSheet.getWorkspaceOptions());
        if (this.fromSheet.getSheetBof().isChart()) {
            this.chartOnly = true;
            this.sheetWriter.setChartOnly();
        }
        if (this.fromSheet.getPLS() != null) {
            if (this.fromSheet.getWorkbookBof().isBiff7()) {
                logger.warn("Cannot copy Biff7 print settings record - ignoring");
            } else {
                this.plsRecord = new PLSRecord(this.fromSheet.getPLS());
            }
        }
        if (this.fromSheet.getButtonPropertySet() != null) {
            this.buttonPropertySet = new ButtonPropertySetRecord(this.fromSheet.getButtonPropertySet());
        }
        this.maxRowOutlineLevel = this.fromSheet.getMaxRowOutlineLevel();
        this.maxColumnOutlineLevel = this.fromSheet.getMaxColumnOutlineLevel();
    }

    private WritableCell shallowCopyCell(Cell cell) {
        CellType ct = cell.getType();
        if (ct == CellType.LABEL) {
            return new Label((LabelCell) cell);
        }
        if (ct == CellType.NUMBER) {
            return new Number((NumberCell) cell);
        }
        if (ct == CellType.DATE) {
            return new DateTime((DateCell) cell);
        }
        if (ct == CellType.BOOLEAN) {
            return new Boolean((BooleanCell) cell);
        }
        if (ct == CellType.NUMBER_FORMULA) {
            return new ReadNumberFormulaRecord((FormulaData) cell);
        }
        if (ct == CellType.STRING_FORMULA) {
            return new ReadStringFormulaRecord((FormulaData) cell);
        }
        if (ct == CellType.BOOLEAN_FORMULA) {
            return new ReadBooleanFormulaRecord((FormulaData) cell);
        }
        if (ct == CellType.DATE_FORMULA) {
            return new ReadDateFormulaRecord((FormulaData) cell);
        }
        if (ct == CellType.FORMULA_ERROR) {
            return new ReadErrorFormulaRecord((FormulaData) cell);
        }
        if (ct == CellType.EMPTY && cell.getCellFormat() != null) {
            return new Blank(cell);
        }
        return null;
    }

    void shallowCopyCells() {
        int cells = this.fromSheet.getRows();
        for (int i = 0; i < cells; i++) {
            Cell[] row = this.fromSheet.getRow(i);
            for (Cell cell : row) {
                WritableCell c = shallowCopyCell(cell);
                if (c != null) {
                    try {
                        this.toSheet.addCell(c);
                        if (c.getCellFeatures() != null && c.getCellFeatures().hasDataValidation()) {
                            this.validatedCells.add(c);
                        }
                    } catch (WriteException e) {
                        Assert.verify(false);
                    }
                }
            }
        }
        this.numRows = this.toSheet.getRows();
    }

    int getRows() {
        return this.numRows;
    }

    public int getMaxColumnOutlineLevel() {
        return this.maxColumnOutlineLevel;
    }

    public int getMaxRowOutlineLevel() {
        return this.maxRowOutlineLevel;
    }
}
