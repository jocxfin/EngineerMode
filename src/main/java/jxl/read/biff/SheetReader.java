package jxl.read.biff;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.HeaderFooter;
import jxl.Range;
import jxl.SheetSettings;
import jxl.WorkbookSettings;
import jxl.biff.AutoFilter;
import jxl.biff.AutoFilterInfoRecord;
import jxl.biff.AutoFilterRecord;
import jxl.biff.ConditionalFormat;
import jxl.biff.ConditionalFormatRangeRecord;
import jxl.biff.ConditionalFormatRecord;
import jxl.biff.ContinueRecord;
import jxl.biff.DataValidation;
import jxl.biff.DataValidityListRecord;
import jxl.biff.DataValiditySettingsRecord;
import jxl.biff.FilterModeRecord;
import jxl.biff.FormattingRecords;
import jxl.biff.Type;
import jxl.biff.WorkspaceInformationRecord;
import jxl.biff.drawing.Button;
import jxl.biff.drawing.Chart;
import jxl.biff.drawing.CheckBox;
import jxl.biff.drawing.ComboBox;
import jxl.biff.drawing.Comment;
import jxl.biff.drawing.Drawing;
import jxl.biff.drawing.Drawing2;
import jxl.biff.drawing.DrawingData;
import jxl.biff.drawing.DrawingDataException;
import jxl.biff.drawing.MsoDrawingRecord;
import jxl.biff.drawing.NoteRecord;
import jxl.biff.drawing.ObjRecord;
import jxl.biff.drawing.TextObjectRecord;
import jxl.biff.formula.FormulaException;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.format.PageOrder;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;

final class SheetReader {
    private static Logger logger = Logger.getLogger(SheetReader.class);
    private AutoFilter autoFilter;
    private ButtonPropertySetRecord buttonPropertySet;
    private Cell[][] cells;
    private ArrayList charts = new ArrayList();
    private int[] columnBreaks;
    private ArrayList columnInfosArray = new ArrayList();
    private ArrayList conditionalFormats = new ArrayList();
    private DataValidation dataValidation;
    private DrawingData drawingData;
    private ArrayList drawings = new ArrayList();
    private File excelFile;
    private FormattingRecords formattingRecords;
    private ArrayList hyperlinks = new ArrayList();
    private int maxColumnOutlineLevel;
    private int maxRowOutlineLevel;
    private Range[] mergedCells;
    private boolean nineteenFour;
    private int numCols;
    private int numRows;
    private ArrayList outOfBoundsCells = new ArrayList();
    private PLSRecord plsRecord;
    private int[] rowBreaks;
    private ArrayList rowProperties = new ArrayList(10);
    private SheetSettings settings;
    private ArrayList sharedFormulas = new ArrayList();
    private SSTRecord sharedStrings;
    private SheetImpl sheet;
    private BOFRecord sheetBof;
    private int startPosition;
    private WorkbookParser workbook;
    private BOFRecord workbookBof;
    private WorkbookSettings workbookSettings;
    private WorkspaceInformationRecord workspaceOptions;

    SheetReader(File f, SSTRecord sst, FormattingRecords fr, BOFRecord sb, BOFRecord wb, boolean nf, WorkbookParser wp, int sp, SheetImpl sh) {
        this.excelFile = f;
        this.sharedStrings = sst;
        this.formattingRecords = fr;
        this.sheetBof = sb;
        this.workbookBof = wb;
        this.nineteenFour = nf;
        this.workbook = wp;
        this.startPosition = sp;
        this.sheet = sh;
        this.settings = new SheetSettings(sh);
        this.workbookSettings = this.workbook.getSettings();
    }

    private void addCell(Cell cell) {
        if (cell.getRow() < this.numRows && cell.getColumn() < this.numCols) {
            if (this.cells[cell.getRow()][cell.getColumn()] != null) {
                StringBuffer sb = new StringBuffer();
                CellReferenceHelper.getCellReference(cell.getColumn(), cell.getRow(), sb);
                logger.warn("Cell " + sb.toString() + " already contains data");
            }
            this.cells[cell.getRow()][cell.getColumn()] = cell;
            return;
        }
        this.outOfBoundsCells.add(cell);
    }

    final void read() {
        BaseSharedFormulaRecord sharedFormula;
        FormulaException e;
        boolean sharedFormulaAdded = false;
        boolean cont = true;
        this.excelFile.setPos(this.startPosition);
        MsoDrawingRecord msoRecord = null;
        ObjRecord objRecord = null;
        boolean firstMsoRecord = true;
        ConditionalFormat condFormat = null;
        FilterModeRecord filterMode = null;
        AutoFilterInfoRecord autoFilterInfo = null;
        Window2Record window2Record = null;
        HashMap comments = new HashMap();
        ArrayList objectIds = new ArrayList();
        ContinueRecord continueRecord = null;
        BaseSharedFormulaRecord sharedFormula2 = null;
        while (cont) {
            Record r = this.excelFile.next();
            Type type = r.getType();
            if (type == Type.UNKNOWN && r.getCode() == 0) {
                logger.warn("Biff code zero found");
                if (r.getLength() != 10) {
                    logger.warn("Biff code zero found - Ignoring.");
                } else {
                    logger.warn("Biff code zero found - trying a dimension record.");
                    r.setType(Type.DIMENSION);
                }
            }
            if (type == Type.DIMENSION) {
                DimensionRecord dimensionRecord;
                if (this.workbookBof.isBiff8()) {
                    dimensionRecord = new DimensionRecord(r);
                } else {
                    dimensionRecord = new DimensionRecord(r, DimensionRecord.biff7);
                }
                this.numRows = dr.getNumberOfRows();
                this.numCols = dr.getNumberOfColumns();
                int i = this.numRows;
                int i2 = this.numCols;
                this.cells = (Cell[][]) Array.newInstance(Cell.class, new int[]{i, i2});
                sharedFormula = sharedFormula2;
            } else if (type == Type.LABELSST) {
                addCell(new LabelSSTRecord(r, this.sharedStrings, this.formattingRecords, this.sheet));
                sharedFormula = sharedFormula2;
            } else if (type == Type.RK || type == Type.RK2) {
                RKRecord rkr = new RKRecord(r, this.formattingRecords, this.sheet);
                if (this.formattingRecords.isDate(rkr.getXFIndex())) {
                    addCell(new DateRecord(rkr, rkr.getXFIndex(), this.formattingRecords, this.nineteenFour, this.sheet));
                } else {
                    addCell(rkr);
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.HLINK) {
                this.hyperlinks.add(new HyperlinkRecord(r, this.sheet, this.workbookSettings));
                sharedFormula = sharedFormula2;
            } else if (type == Type.MERGEDCELLS) {
                MergedCellsRecord mergedCellsRecord = new MergedCellsRecord(r, this.sheet);
                if (this.mergedCells != null) {
                    Object newMergedCells = new Range[(this.mergedCells.length + mergedCellsRecord.getRanges().length)];
                    System.arraycopy(this.mergedCells, 0, newMergedCells, 0, this.mergedCells.length);
                    System.arraycopy(mergedCellsRecord.getRanges(), 0, newMergedCells, this.mergedCells.length, mergedCellsRecord.getRanges().length);
                    this.mergedCells = newMergedCells;
                } else {
                    this.mergedCells = mergedCellsRecord.getRanges();
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.MULRK) {
                MulRKRecord mulRKRecord = new MulRKRecord(r);
                num = mulRKRecord.getNumberOfColumns();
                for (i = 0; i < num; i++) {
                    int ixf = mulRKRecord.getXFIndex(i);
                    NumberValue nv = new NumberValue(mulRKRecord.getRow(), mulRKRecord.getFirstColumn() + i, RKHelper.getDouble(mulRKRecord.getRKNumber(i)), ixf, this.formattingRecords, this.sheet);
                    if (this.formattingRecords.isDate(ixf)) {
                        addCell(new DateRecord(nv, ixf, this.formattingRecords, this.nineteenFour, this.sheet));
                    } else {
                        nv.setNumberFormat(this.formattingRecords.getNumberFormat(ixf));
                        addCell(nv);
                    }
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.NUMBER) {
                NumberRecord nr = new NumberRecord(r, this.formattingRecords, this.sheet);
                if (this.formattingRecords.isDate(nr.getXFIndex())) {
                    addCell(new DateRecord(nr, nr.getXFIndex(), this.formattingRecords, this.nineteenFour, this.sheet));
                } else {
                    addCell(nr);
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.BOOLERR) {
                BooleanRecord booleanRecord = new BooleanRecord(r, this.formattingRecords, this.sheet);
                if (booleanRecord.isError()) {
                    addCell(new ErrorRecord(booleanRecord.getRecord(), this.formattingRecords, this.sheet));
                } else {
                    addCell(booleanRecord);
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.PRINTGRIDLINES) {
                this.settings.setPrintGridLines(new PrintGridLinesRecord(r).getPrintGridLines());
                sharedFormula = sharedFormula2;
            } else if (type == Type.PRINTHEADERS) {
                this.settings.setPrintHeaders(new PrintHeadersRecord(r).getPrintHeaders());
                sharedFormula = sharedFormula2;
            } else if (type == Type.WINDOW2) {
                Window2Record window2Record2;
                if (this.workbookBof.isBiff8()) {
                    window2Record2 = new Window2Record(r);
                } else {
                    window2Record2 = new Window2Record(r, Window2Record.biff7);
                }
                this.settings.setShowGridLines(window2Record.getShowGridLines());
                this.settings.setDisplayZeroValues(window2Record.getDisplayZeroValues());
                this.settings.setSelected(true);
                this.settings.setPageBreakPreviewMode(window2Record.isPageBreakPreview());
                sharedFormula = sharedFormula2;
            } else if (type == Type.PANE) {
                PaneRecord paneRecord = new PaneRecord(r);
                if (window2Record != null && window2Record.getFrozen()) {
                    this.settings.setVerticalFreeze(paneRecord.getRowsVisible());
                    this.settings.setHorizontalFreeze(paneRecord.getColumnsVisible());
                }
                sharedFormula = sharedFormula2;
            } else if (type == Type.CONTINUE) {
                ContinueRecord continueRecord2 = new ContinueRecord(r);
                sharedFormula = sharedFormula2;
            } else if (type != Type.NOTE) {
                if (type == Type.ARRAY) {
                    sharedFormula = sharedFormula2;
                } else if (type == Type.PROTECT) {
                    this.settings.setProtected(new ProtectRecord(r).isProtected());
                    sharedFormula = sharedFormula2;
                } else if (type == Type.SHAREDFORMULA) {
                    if (sharedFormula2 != null) {
                        sharedFormula = sharedFormula2;
                    } else {
                        logger.warn("Shared template formula is null - trying most recent formula template");
                        SharedFormulaRecord lastSharedFormula = (SharedFormulaRecord) this.sharedFormulas.get(this.sharedFormulas.size() - 1);
                        if (lastSharedFormula == null) {
                            sharedFormula = sharedFormula2;
                        } else {
                            sharedFormula = lastSharedFormula.getTemplateFormula();
                        }
                    }
                    this.sharedFormulas.add(new SharedFormulaRecord(r, sharedFormula, this.workbook, this.workbook, this.sheet));
                    sharedFormula = null;
                } else if (type == Type.FORMULA || type == Type.FORMULA2) {
                    FormulaRecord fr = new FormulaRecord(r, this.excelFile, this.formattingRecords, this.workbook, this.workbook, this.sheet, this.workbookSettings);
                    if (fr.isShared()) {
                        BaseSharedFormulaRecord prevSharedFormula = sharedFormula2;
                        sharedFormula = (BaseSharedFormulaRecord) fr.getFormula();
                        sharedFormulaAdded = addToSharedFormulas(sharedFormula);
                        if (sharedFormulaAdded) {
                            sharedFormula = prevSharedFormula;
                        }
                        if (!(sharedFormulaAdded || prevSharedFormula == null)) {
                            addCell(revertSharedFormula(prevSharedFormula));
                        }
                    } else {
                        Cell cell = fr.getFormula();
                        Cell cell2;
                        try {
                            if (fr.getFormula().getType() != CellType.NUMBER_FORMULA) {
                                cell2 = cell;
                            } else {
                                NumberFormulaRecord nfr = (NumberFormulaRecord) fr.getFormula();
                                if (this.formattingRecords.isDate(nfr.getXFIndex())) {
                                    cell2 = new DateFormulaRecord(nfr, this.formattingRecords, this.workbook, this.workbook, this.nineteenFour, this.sheet);
                                } else {
                                    cell2 = cell;
                                }
                            }
                            try {
                                addCell(cell2);
                                sharedFormula = sharedFormula2;
                            } catch (FormulaException e2) {
                                e = e2;
                                logger.warn(CellReferenceHelper.getCellReference(cell2.getColumn(), cell2.getRow()) + " " + e.getMessage());
                                sharedFormula = sharedFormula2;
                                sharedFormula2 = sharedFormula;
                            }
                        } catch (FormulaException e3) {
                            e = e3;
                            cell2 = cell;
                            logger.warn(CellReferenceHelper.getCellReference(cell2.getColumn(), cell2.getRow()) + " " + e.getMessage());
                            sharedFormula = sharedFormula2;
                            sharedFormula2 = sharedFormula;
                        }
                    }
                } else if (type == Type.LABEL) {
                    LabelRecord lr;
                    if (this.workbookBof.isBiff8()) {
                        LabelRecord labelRecord = new LabelRecord(r, this.formattingRecords, this.sheet, this.workbookSettings);
                    } else {
                        lr = new LabelRecord(r, this.formattingRecords, this.sheet, this.workbookSettings, LabelRecord.biff7);
                    }
                    addCell(lr);
                    sharedFormula = sharedFormula2;
                } else if (type == Type.RSTRING) {
                    if (this.workbookBof.isBiff8()) {
                        r6 = false;
                    } else {
                        r6 = true;
                    }
                    Assert.verify(r6);
                    addCell(new RStringRecord(r, this.formattingRecords, this.sheet, this.workbookSettings, RStringRecord.biff7));
                    sharedFormula = sharedFormula2;
                } else if (type == Type.NAME) {
                    sharedFormula = sharedFormula2;
                } else if (type == Type.PASSWORD) {
                    this.settings.setPasswordHash(new PasswordRecord(r).getPasswordHash());
                    sharedFormula = sharedFormula2;
                } else if (type == Type.ROW) {
                    RowRecord rowRecord = new RowRecord(r);
                    if (!rowRecord.isDefaultHeight() || !rowRecord.matchesDefaultFontHeight() || rowRecord.isCollapsed() || rowRecord.hasDefaultFormat() || rowRecord.getOutlineLevel() != 0) {
                        this.rowProperties.add(rowRecord);
                    }
                    sharedFormula = sharedFormula2;
                } else if (type != Type.BLANK) {
                    if (type != Type.MULBLANK) {
                        if (type == Type.SCL) {
                            this.settings.setZoomFactor(new SCLRecord(r).getZoomFactor());
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.COLINFO) {
                            this.columnInfosArray.add(new ColumnInfoRecord(r));
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.HEADER) {
                            HeaderRecord headerRecord;
                            if (this.workbookBof.isBiff8()) {
                                headerRecord = new HeaderRecord(r, this.workbookSettings);
                            } else {
                                headerRecord = new HeaderRecord(r, this.workbookSettings, HeaderRecord.biff7);
                            }
                            this.settings.setHeader(new HeaderFooter(hr.getHeader()));
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.FOOTER) {
                            FooterRecord footerRecord;
                            if (this.workbookBof.isBiff8()) {
                                footerRecord = new FooterRecord(r, this.workbookSettings);
                            } else {
                                footerRecord = new FooterRecord(r, this.workbookSettings, FooterRecord.biff7);
                            }
                            this.settings.setFooter(new HeaderFooter(fr.getFooter()));
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.SETUP) {
                            SetupRecord setupRecord = new SetupRecord(r);
                            if (setupRecord.getInitialized()) {
                                if (setupRecord.isPortrait()) {
                                    this.settings.setOrientation(PageOrientation.PORTRAIT);
                                } else {
                                    this.settings.setOrientation(PageOrientation.LANDSCAPE);
                                }
                                if (setupRecord.isRightDown()) {
                                    this.settings.setPageOrder(PageOrder.RIGHT_THEN_DOWN);
                                } else {
                                    this.settings.setPageOrder(PageOrder.DOWN_THEN_RIGHT);
                                }
                                this.settings.setPaperSize(PaperSize.getPaperSize(setupRecord.getPaperSize()));
                                this.settings.setHeaderMargin(setupRecord.getHeaderMargin());
                                this.settings.setFooterMargin(setupRecord.getFooterMargin());
                                this.settings.setScaleFactor(setupRecord.getScaleFactor());
                                this.settings.setPageStart(setupRecord.getPageStart());
                                this.settings.setFitWidth(setupRecord.getFitWidth());
                                this.settings.setFitHeight(setupRecord.getFitHeight());
                                this.settings.setHorizontalPrintResolution(setupRecord.getHorizontalPrintResolution());
                                this.settings.setVerticalPrintResolution(setupRecord.getVerticalPrintResolution());
                                this.settings.setCopies(setupRecord.getCopies());
                                if (this.workspaceOptions != null) {
                                    this.settings.setFitToPages(this.workspaceOptions.getFitToPages());
                                }
                            }
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.WSBOOL) {
                            this.workspaceOptions = new WorkspaceInformationRecord(r);
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.DEFCOLWIDTH) {
                            this.settings.setDefaultColumnWidth(new DefaultColumnWidthRecord(r).getWidth());
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.DEFAULTROWHEIGHT) {
                            DefaultRowHeightRecord defaultRowHeightRecord = new DefaultRowHeightRecord(r);
                            if (defaultRowHeightRecord.getHeight() != 0) {
                                this.settings.setDefaultRowHeight(defaultRowHeightRecord.getHeight());
                            }
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.CONDFMT) {
                            ConditionalFormat conditionalFormat = new ConditionalFormat(new ConditionalFormatRangeRecord(r));
                            this.conditionalFormats.add(conditionalFormat);
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.CF) {
                            condFormat.addCondition(new ConditionalFormatRecord(r));
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.FILTERMODE) {
                            FilterModeRecord filterModeRecord = new FilterModeRecord(r);
                            sharedFormula = sharedFormula2;
                        } else if (type == Type.AUTOFILTERINFO) {
                            AutoFilterInfoRecord autoFilterInfoRecord = new AutoFilterInfoRecord(r);
                            sharedFormula = sharedFormula2;
                        } else if (type != Type.AUTOFILTER) {
                            if (type == Type.LEFTMARGIN) {
                                this.settings.setLeftMargin(new LeftMarginRecord(r).getMargin());
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.RIGHTMARGIN) {
                                this.settings.setRightMargin(new RightMarginRecord(r).getMargin());
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.TOPMARGIN) {
                                this.settings.setTopMargin(new TopMarginRecord(r).getMargin());
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.BOTTOMMARGIN) {
                                this.settings.setBottomMargin(new BottomMarginRecord(r).getMargin());
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.HORIZONTALPAGEBREAKS) {
                                HorizontalPageBreaksRecord horizontalPageBreaksRecord;
                                if (this.workbookBof.isBiff8()) {
                                    horizontalPageBreaksRecord = new HorizontalPageBreaksRecord(r);
                                } else {
                                    horizontalPageBreaksRecord = new HorizontalPageBreaksRecord(r, HorizontalPageBreaksRecord.biff7);
                                }
                                this.rowBreaks = dr.getRowBreaks();
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.VERTICALPAGEBREAKS) {
                                VerticalPageBreaksRecord verticalPageBreaksRecord;
                                if (this.workbookBof.isBiff8()) {
                                    verticalPageBreaksRecord = new VerticalPageBreaksRecord(r);
                                } else {
                                    verticalPageBreaksRecord = new VerticalPageBreaksRecord(r, VerticalPageBreaksRecord.biff7);
                                }
                                this.columnBreaks = dr.getColumnBreaks();
                                sharedFormula = sharedFormula2;
                            } else if (type == Type.PLS) {
                                this.plsRecord = new PLSRecord(r);
                                while (this.excelFile.peek().getType() == Type.CONTINUE) {
                                    r.addContinueRecord(this.excelFile.next());
                                }
                                sharedFormula = sharedFormula2;
                            } else if (type != Type.DVAL) {
                                if (type == Type.HCENTER) {
                                    this.settings.setHorizontalCentre(new CentreRecord(r).isCentre());
                                    sharedFormula = sharedFormula2;
                                } else if (type == Type.VCENTER) {
                                    this.settings.setVerticalCentre(new CentreRecord(r).isCentre());
                                    sharedFormula = sharedFormula2;
                                } else if (type != Type.DV) {
                                    MsoDrawingRecord msoDrawingRecord;
                                    if (type == Type.OBJ) {
                                        ObjRecord objRecord2 = new ObjRecord(r);
                                        if (!this.workbookSettings.getDrawingsDisabled()) {
                                            if (msoRecord == null && continueRecord != null) {
                                                logger.warn("Cannot find drawing record - using continue record");
                                                msoDrawingRecord = new MsoDrawingRecord(continueRecord.getRecord());
                                                continueRecord = null;
                                            }
                                            handleObjectRecord(objRecord2, msoRecord, comments);
                                            objectIds.add(new Integer(objRecord2.getObjectId()));
                                        }
                                        if (objRecord2.getType() == ObjRecord.CHART) {
                                            sharedFormula = sharedFormula2;
                                        } else {
                                            objRecord = null;
                                            msoRecord = null;
                                            sharedFormula = sharedFormula2;
                                        }
                                    } else if (type != Type.MSODRAWING) {
                                        if (type == Type.BUTTONPROPERTYSET) {
                                            this.buttonPropertySet = new ButtonPropertySetRecord(r);
                                            sharedFormula = sharedFormula2;
                                        } else if (type == Type.CALCMODE) {
                                            this.settings.setAutomaticFormulaCalculation(new CalcModeRecord(r).isAutomatic());
                                            sharedFormula = sharedFormula2;
                                        } else if (type == Type.SAVERECALC) {
                                            this.settings.setRecalculateFormulasBeforeSave(new SaveRecalcRecord(r).getRecalculateOnSave());
                                            sharedFormula = sharedFormula2;
                                        } else if (type == Type.GUTS) {
                                            GuttersRecord guttersRecord = new GuttersRecord(r);
                                            this.maxRowOutlineLevel = guttersRecord.getRowOutlineLevel() <= 0 ? 0 : guttersRecord.getRowOutlineLevel() - 1;
                                            this.maxColumnOutlineLevel = guttersRecord.getColumnOutlineLevel() <= 0 ? 0 : guttersRecord.getRowOutlineLevel() - 1;
                                            sharedFormula = sharedFormula2;
                                        } else if (type == Type.BOF) {
                                            BOFRecord bOFRecord = new BOFRecord(r);
                                            if (bOFRecord.isWorksheet()) {
                                                r6 = false;
                                            } else {
                                                r6 = true;
                                            }
                                            Assert.verify(r6);
                                            int startpos = (this.excelFile.getPos() - r.getLength()) - 4;
                                            Record r2 = this.excelFile.next();
                                            while (r2.getCode() != Type.EOF.value) {
                                                r2 = this.excelFile.next();
                                            }
                                            if (bOFRecord.isChart()) {
                                                if (this.workbook.getWorkbookBof().isBiff8()) {
                                                    if (this.drawingData == null) {
                                                        this.drawingData = new DrawingData();
                                                    }
                                                    if (!this.workbookSettings.getDrawingsDisabled()) {
                                                        Chart chart = new Chart(msoRecord, objRecord, this.drawingData, startpos, this.excelFile.getPos(), this.excelFile, this.workbookSettings);
                                                        this.charts.add(chart);
                                                        if (this.workbook.getDrawingGroup() != null) {
                                                            this.workbook.getDrawingGroup().add(chart);
                                                        }
                                                    }
                                                } else {
                                                    logger.warn("only biff8 charts are supported");
                                                }
                                                msoRecord = null;
                                                objRecord = null;
                                            }
                                            if (this.sheetBof.isChart()) {
                                                cont = false;
                                            }
                                            sharedFormula = sharedFormula2;
                                        } else if (type != Type.EOF) {
                                            sharedFormula = sharedFormula2;
                                        } else {
                                            cont = false;
                                            sharedFormula = sharedFormula2;
                                        }
                                    } else if (this.workbookSettings.getDrawingsDisabled()) {
                                        sharedFormula = sharedFormula2;
                                    } else {
                                        if (msoRecord != null) {
                                            this.drawingData.addRawData(msoRecord.getData());
                                        }
                                        msoDrawingRecord = new MsoDrawingRecord(r);
                                        if (firstMsoRecord) {
                                            msoDrawingRecord.setFirst();
                                            firstMsoRecord = false;
                                            sharedFormula = sharedFormula2;
                                        } else {
                                            sharedFormula = sharedFormula2;
                                        }
                                    }
                                } else if (this.workbookSettings.getCellValidationDisabled()) {
                                    sharedFormula = sharedFormula2;
                                } else {
                                    DataValiditySettingsRecord dataValiditySettingsRecord = new DataValiditySettingsRecord(r, this.workbook, this.workbook, this.workbook.getSettings());
                                    if (this.dataValidation == null) {
                                        logger.warn("cannot add data validity settings");
                                    } else {
                                        this.dataValidation.add(dataValiditySettingsRecord);
                                        addCellValidation(dataValiditySettingsRecord.getFirstColumn(), dataValiditySettingsRecord.getFirstRow(), dataValiditySettingsRecord.getLastColumn(), dataValiditySettingsRecord.getLastRow(), dataValiditySettingsRecord);
                                    }
                                    sharedFormula = sharedFormula2;
                                }
                            } else if (this.workbookSettings.getCellValidationDisabled()) {
                                sharedFormula = sharedFormula2;
                            } else {
                                DataValidityListRecord dataValidityListRecord = new DataValidityListRecord(r);
                                if (dataValidityListRecord.getObjectId() != -1) {
                                    if (objectIds.contains(new Integer(dataValidityListRecord.getObjectId()))) {
                                        this.dataValidation = new DataValidation(dataValidityListRecord);
                                    } else {
                                        logger.warn("object id " + dataValidityListRecord.getObjectId() + " referenced " + " by data validity list record not found - ignoring");
                                    }
                                } else if (msoRecord != null && objRecord == null) {
                                    if (this.drawingData == null) {
                                        this.drawingData = new DrawingData();
                                    }
                                    this.drawings.add(new Drawing2(msoRecord, this.drawingData, this.workbook.getDrawingGroup()));
                                    msoRecord = null;
                                    this.dataValidation = new DataValidation(dataValidityListRecord);
                                } else {
                                    this.dataValidation = new DataValidation(dataValidityListRecord);
                                }
                                sharedFormula = sharedFormula2;
                            }
                        } else if (this.workbookSettings.getAutoFilterDisabled()) {
                            sharedFormula = sharedFormula2;
                        } else {
                            AutoFilterRecord autoFilterRecord = new AutoFilterRecord(r);
                            if (this.autoFilter == null) {
                                this.autoFilter = new AutoFilter(filterMode, autoFilterInfo);
                                filterMode = null;
                                autoFilterInfo = null;
                            }
                            this.autoFilter.add(autoFilterRecord);
                            sharedFormula = sharedFormula2;
                        }
                    } else if (this.workbookSettings.getIgnoreBlanks()) {
                        sharedFormula = sharedFormula2;
                    } else {
                        MulBlankRecord mulBlankRecord = new MulBlankRecord(r);
                        num = mulBlankRecord.getNumberOfColumns();
                        for (i = 0; i < num; i++) {
                            addCell(new MulBlankCell(mulBlankRecord.getRow(), mulBlankRecord.getFirstColumn() + i, mulBlankRecord.getXFIndex(i), this.formattingRecords, this.sheet));
                        }
                        sharedFormula = sharedFormula2;
                    }
                } else if (this.workbookSettings.getIgnoreBlanks()) {
                    sharedFormula = sharedFormula2;
                } else {
                    addCell(new BlankCell(r, this.formattingRecords, this.sheet));
                    sharedFormula = sharedFormula2;
                }
            } else if (this.workbookSettings.getDrawingsDisabled()) {
                sharedFormula = sharedFormula2;
            } else {
                NoteRecord noteRecord = new NoteRecord(r);
                Comment comment = (Comment) comments.remove(new Integer(noteRecord.getObjectId()));
                if (comment != null) {
                    comment.setNote(noteRecord);
                    this.drawings.add(comment);
                    addCellComment(comment.getColumn(), comment.getRow(), comment.getText(), comment.getWidth(), comment.getHeight());
                } else {
                    logger.warn(" cannot find comment for note id " + noteRecord.getObjectId() + "...ignoring");
                }
                sharedFormula = sharedFormula2;
            }
            sharedFormula2 = sharedFormula;
        }
        this.excelFile.restorePos();
        if (this.outOfBoundsCells.size() > 0) {
            handleOutOfBoundsCells();
        }
        Iterator i3 = this.sharedFormulas.iterator();
        while (i3.hasNext()) {
            Cell[] sfnr = ((SharedFormulaRecord) i3.next()).getFormulas(this.formattingRecords, this.nineteenFour);
            for (Cell addCell : sfnr) {
                addCell(addCell);
            }
        }
        if (!(sharedFormulaAdded || sharedFormula2 == null)) {
            addCell(revertSharedFormula(sharedFormula2));
        }
        if (!(msoRecord == null || this.workbook.getDrawingGroup() == null)) {
            this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, objRecord);
        }
        if (!comments.isEmpty()) {
            logger.warn("Not all comments have a corresponding Note record");
        }
    }

    private boolean addToSharedFormulas(BaseSharedFormulaRecord fr) {
        boolean added = false;
        int size = this.sharedFormulas.size();
        for (int i = 0; i < size && !added; i++) {
            added = ((SharedFormulaRecord) this.sharedFormulas.get(i)).add(fr);
        }
        return added;
    }

    private Cell revertSharedFormula(BaseSharedFormulaRecord f) {
        int pos = this.excelFile.getPos();
        this.excelFile.setPos(f.getFilePos());
        FormulaRecord fr = new FormulaRecord(f.getRecord(), this.excelFile, this.formattingRecords, this.workbook, this.workbook, FormulaRecord.ignoreSharedFormula, this.sheet, this.workbookSettings);
        try {
            Cell cell = fr.getFormula();
            if (fr.getFormula().getType() == CellType.NUMBER_FORMULA) {
                NumberFormulaRecord nfr = (NumberFormulaRecord) fr.getFormula();
                if (this.formattingRecords.isDate(fr.getXFIndex())) {
                    cell = new DateFormulaRecord(nfr, this.formattingRecords, this.workbook, this.workbook, this.nineteenFour, this.sheet);
                }
            }
            this.excelFile.setPos(pos);
            return cell;
        } catch (FormulaException e) {
            logger.warn(CellReferenceHelper.getCellReference(fr.getColumn(), fr.getRow()) + " " + e.getMessage());
            return null;
        }
    }

    final int getNumRows() {
        return this.numRows;
    }

    final int getNumCols() {
        return this.numCols;
    }

    final Cell[][] getCells() {
        return this.cells;
    }

    final ArrayList getRowProperties() {
        return this.rowProperties;
    }

    final ArrayList getColumnInfosArray() {
        return this.columnInfosArray;
    }

    final ArrayList getHyperlinks() {
        return this.hyperlinks;
    }

    final ArrayList getConditionalFormats() {
        return this.conditionalFormats;
    }

    final AutoFilter getAutoFilter() {
        return this.autoFilter;
    }

    final ArrayList getCharts() {
        return this.charts;
    }

    final ArrayList getDrawings() {
        return this.drawings;
    }

    final DataValidation getDataValidation() {
        return this.dataValidation;
    }

    final Range[] getMergedCells() {
        return this.mergedCells;
    }

    final SheetSettings getSettings() {
        return this.settings;
    }

    final int[] getRowBreaks() {
        return this.rowBreaks;
    }

    final int[] getColumnBreaks() {
        return this.columnBreaks;
    }

    final WorkspaceInformationRecord getWorkspaceOptions() {
        return this.workspaceOptions;
    }

    final PLSRecord getPLS() {
        return this.plsRecord;
    }

    final ButtonPropertySetRecord getButtonPropertySet() {
        return this.buttonPropertySet;
    }

    private void addCellComment(int col, int row, String text, double width, double height) {
        Cell c = this.cells[row][col];
        CellFeatures cf;
        if (c != null) {
            if (c instanceof CellFeaturesAccessor) {
                CellFeaturesAccessor cv = (CellFeaturesAccessor) c;
                cf = cv.getCellFeatures();
                if (cf == null) {
                    cf = new CellFeatures();
                    cv.setCellFeatures(cf);
                }
                cf.setReadComment(text, width, height);
            } else {
                logger.warn("Not able to add comment to cell type " + c.getClass().getName() + " at " + CellReferenceHelper.getCellReference(col, row));
            }
            return;
        }
        logger.warn("Cell at " + CellReferenceHelper.getCellReference(col, row) + " not present - adding a blank");
        MulBlankCell mbc = new MulBlankCell(row, col, 0, this.formattingRecords, this.sheet);
        cf = new CellFeatures();
        cf.setReadComment(text, width, height);
        mbc.setCellFeatures(cf);
        addCell(mbc);
    }

    private void addCellValidation(int col1, int row1, int col2, int row2, DataValiditySettingsRecord dvsr) {
        int row = row1;
        while (row <= row2) {
            int col = col1;
            while (col <= col2) {
                CellFeaturesAccessor c = null;
                if (this.cells.length > row && this.cells[row].length > col) {
                    c = this.cells[row][col];
                }
                CellFeatures cf;
                if (c == null) {
                    MulBlankCell mbc = new MulBlankCell(row, col, 0, this.formattingRecords, this.sheet);
                    cf = new CellFeatures();
                    cf.setValidationSettings(dvsr);
                    mbc.setCellFeatures(cf);
                    addCell(mbc);
                } else if (c instanceof CellFeaturesAccessor) {
                    CellFeaturesAccessor cv = c;
                    cf = cv.getCellFeatures();
                    if (cf == null) {
                        cf = new CellFeatures();
                        cv.setCellFeatures(cf);
                    }
                    cf.setValidationSettings(dvsr);
                } else {
                    logger.warn("Not able to add comment to cell type " + c.getClass().getName() + " at " + CellReferenceHelper.getCellReference(col, row));
                }
                col++;
            }
            row++;
        }
    }

    private void handleObjectRecord(ObjRecord objRecord, MsoDrawingRecord msoRecord, HashMap comments) {
        if (msoRecord != null) {
            try {
                if (objRecord.getType() == ObjRecord.PICTURE) {
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    this.drawings.add(new Drawing(msoRecord, objRecord, this.drawingData, this.workbook.getDrawingGroup(), this.sheet));
                } else if (objRecord.getType() == ObjRecord.EXCELNOTE) {
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    Comment comment = new Comment(msoRecord, objRecord, this.drawingData, this.workbook.getDrawingGroup(), this.workbookSettings);
                    r2 = this.excelFile.next();
                    if (r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE) {
                        comment.addMso(new MsoDrawingRecord(r2));
                        r2 = this.excelFile.next();
                    }
                    if (r2.getType() != Type.TXO) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    comment.setTextObject(new TextObjectRecord(r2));
                    r2 = this.excelFile.next();
                    if (r2.getType() != Type.CONTINUE) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    comment.setText(new ContinueRecord(r2));
                    r2 = this.excelFile.next();
                    if (r2.getType() == Type.CONTINUE) {
                        comment.setFormatting(new ContinueRecord(r2));
                    }
                    comments.put(new Integer(comment.getObjectId()), comment);
                } else if (objRecord.getType() == ObjRecord.COMBOBOX) {
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    this.drawings.add(new ComboBox(msoRecord, objRecord, this.drawingData, this.workbook.getDrawingGroup(), this.workbookSettings));
                } else if (objRecord.getType() == ObjRecord.CHECKBOX) {
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    CheckBox checkBox = new CheckBox(msoRecord, objRecord, this.drawingData, this.workbook.getDrawingGroup(), this.workbookSettings);
                    r2 = this.excelFile.next();
                    r7 = r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE;
                    Assert.verify(r7);
                    if (r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE) {
                        checkBox.addMso(new MsoDrawingRecord(r2));
                        r2 = this.excelFile.next();
                    }
                    if (r2.getType() != Type.TXO) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    TextObjectRecord textObjectRecord = new TextObjectRecord(r2);
                    checkBox.setTextObject(textObjectRecord);
                    if (textObjectRecord.getTextLength() != 0) {
                        r2 = this.excelFile.next();
                        if (r2.getType() != Type.CONTINUE) {
                            r7 = false;
                        } else {
                            r7 = true;
                        }
                        Assert.verify(r7);
                        checkBox.setText(new ContinueRecord(r2));
                        r2 = this.excelFile.next();
                        if (r2.getType() == Type.CONTINUE) {
                            checkBox.setFormatting(new ContinueRecord(r2));
                        }
                        this.drawings.add(checkBox);
                    }
                } else if (objRecord.getType() == ObjRecord.BUTTON) {
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    Button button = new Button(msoRecord, objRecord, this.drawingData, this.workbook.getDrawingGroup(), this.workbookSettings);
                    r2 = this.excelFile.next();
                    r7 = r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE;
                    Assert.verify(r7);
                    if (r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE) {
                        button.addMso(new MsoDrawingRecord(r2));
                        r2 = this.excelFile.next();
                    }
                    if (r2.getType() != Type.TXO) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    button.setTextObject(new TextObjectRecord(r2));
                    r2 = this.excelFile.next();
                    if (r2.getType() != Type.CONTINUE) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    button.setText(new ContinueRecord(r2));
                    r2 = this.excelFile.next();
                    if (r2.getType() == Type.CONTINUE) {
                        button.setFormatting(new ContinueRecord(r2));
                    }
                    this.drawings.add(button);
                } else if (objRecord.getType() != ObjRecord.TEXT) {
                    if (objRecord.getType() != ObjRecord.CHART) {
                        logger.warn(objRecord.getType() + " Object on sheet \"" + this.sheet.getName() + "\" not supported - omitting");
                        if (this.drawingData == null) {
                            this.drawingData = new DrawingData();
                        }
                        this.drawingData.addData(msoRecord.getData());
                        if (this.workbook.getDrawingGroup() != null) {
                            this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, objRecord);
                        }
                    }
                } else {
                    logger.warn(objRecord.getType() + " Object on sheet \"" + this.sheet.getName() + "\" not supported - omitting");
                    if (this.drawingData == null) {
                        this.drawingData = new DrawingData();
                    }
                    this.drawingData.addData(msoRecord.getData());
                    r2 = this.excelFile.next();
                    r7 = r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE;
                    Assert.verify(r7);
                    if (r2.getType() == Type.MSODRAWING || r2.getType() == Type.CONTINUE) {
                        this.drawingData.addRawData(new MsoDrawingRecord(r2).getData());
                        r2 = this.excelFile.next();
                    }
                    if (r2.getType() != Type.TXO) {
                        r7 = false;
                    } else {
                        r7 = true;
                    }
                    Assert.verify(r7);
                    if (this.workbook.getDrawingGroup() != null) {
                        this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, objRecord);
                    }
                }
            } catch (DrawingDataException e) {
                logger.warn(e.getMessage() + "...disabling drawings for the remainder of the workbook");
                this.workbookSettings.setDrawingsDisabled(true);
            }
        } else {
            logger.warn("Object record is not associated with a drawing  record - ignoring");
        }
    }

    private void handleOutOfBoundsCells() {
        int resizedRows = this.numRows;
        int resizedCols = this.numCols;
        Iterator i = this.outOfBoundsCells.iterator();
        while (i.hasNext()) {
            Cell cell = (Cell) i.next();
            resizedRows = Math.max(resizedRows, cell.getRow() + 1);
            resizedCols = Math.max(resizedCols, cell.getColumn() + 1);
        }
        if (resizedCols > this.numCols) {
            for (int r = 0; r < this.numRows; r++) {
                Cell[] newRow = new Cell[resizedCols];
                Cell[] oldRow = this.cells[r];
                System.arraycopy(oldRow, 0, newRow, 0, oldRow.length);
                this.cells[r] = newRow;
            }
        }
        if (resizedRows > this.numRows) {
            Cell[][] newCells = new Cell[resizedRows][];
            System.arraycopy(this.cells, 0, newCells, 0, this.cells.length);
            this.cells = newCells;
            for (int i2 = this.numRows; i2 < resizedRows; i2++) {
                newCells[i2] = new Cell[resizedCols];
            }
        }
        this.numRows = resizedRows;
        this.numCols = resizedCols;
        i = this.outOfBoundsCells.iterator();
        while (i.hasNext()) {
            addCell((Cell) i.next());
        }
        this.outOfBoundsCells.clear();
    }

    public int getMaxColumnOutlineLevel() {
        return this.maxColumnOutlineLevel;
    }

    public int getMaxRowOutlineLevel() {
        return this.maxRowOutlineLevel;
    }
}
