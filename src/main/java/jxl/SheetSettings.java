package jxl;

import com.android.engineeringmode.functions.Light;

import jxl.biff.SheetRangeImpl;
import jxl.common.Assert;
import jxl.format.PageOrder;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;

public final class SheetSettings {
    private static final PageOrder DEFAULT_ORDER = PageOrder.RIGHT_THEN_DOWN;
    private static final PageOrientation DEFAULT_ORIENTATION = PageOrientation.PORTRAIT;
    private static final PaperSize DEFAULT_PAPER_SIZE = PaperSize.A4;
    private boolean automaticFormulaCalculation;
    private double bottomMargin;
    private int copies;
    private int defaultColumnWidth;
    private int defaultRowHeight;
    private boolean displayZeroValues;
    private int fitHeight;
    private boolean fitToPages;
    private int fitWidth;
    private HeaderFooter footer;
    private double footerMargin;
    private HeaderFooter header;
    private double headerMargin;
    private boolean hidden;
    private boolean horizontalCentre;
    private int horizontalFreeze;
    private int horizontalPrintResolution;
    private double leftMargin;
    private int normalMagnification;
    private PageOrientation orientation;
    private int pageBreakPreviewMagnification;
    private boolean pageBreakPreviewMode;
    private PageOrder pageOrder;
    private int pageStart;
    private PaperSize paperSize;
    private String password;
    private int passwordHash;
    private Range printArea;
    private boolean printGridLines;
    private boolean printHeaders;
    private Range printTitlesCol;
    private Range printTitlesRow;
    private boolean recalculateFormulasBeforeSave;
    private double rightMargin;
    private int scaleFactor;
    private boolean selected;
    private Sheet sheet;
    private boolean sheetProtected;
    private boolean showGridLines;
    private double topMargin;
    private boolean verticalCentre;
    private int verticalFreeze;
    private int verticalPrintResolution;
    private int zoomFactor;

    public SheetSettings(Sheet s) {
        this.sheet = s;
        this.orientation = DEFAULT_ORIENTATION;
        this.pageOrder = DEFAULT_ORDER;
        this.paperSize = DEFAULT_PAPER_SIZE;
        this.sheetProtected = false;
        this.hidden = false;
        this.selected = false;
        this.headerMargin = 0.5d;
        this.footerMargin = 0.5d;
        this.horizontalPrintResolution = 300;
        this.verticalPrintResolution = 300;
        this.leftMargin = 0.75d;
        this.rightMargin = 0.75d;
        this.topMargin = 1.0d;
        this.bottomMargin = 1.0d;
        this.fitToPages = false;
        this.showGridLines = true;
        this.printGridLines = false;
        this.printHeaders = false;
        this.pageBreakPreviewMode = false;
        this.displayZeroValues = true;
        this.defaultColumnWidth = 8;
        this.defaultRowHeight = Light.MAIN_KEY_MAX;
        this.zoomFactor = 100;
        this.pageBreakPreviewMagnification = 60;
        this.normalMagnification = 100;
        this.horizontalFreeze = 0;
        this.verticalFreeze = 0;
        this.copies = 1;
        this.header = new HeaderFooter();
        this.footer = new HeaderFooter();
        this.automaticFormulaCalculation = true;
        this.recalculateFormulasBeforeSave = true;
    }

    public SheetSettings(SheetSettings copy, Sheet s) {
        Assert.verify(copy != null);
        this.sheet = s;
        this.orientation = copy.orientation;
        this.pageOrder = copy.pageOrder;
        this.paperSize = copy.paperSize;
        this.sheetProtected = copy.sheetProtected;
        this.hidden = copy.hidden;
        this.selected = false;
        this.headerMargin = copy.headerMargin;
        this.footerMargin = copy.footerMargin;
        this.scaleFactor = copy.scaleFactor;
        this.pageStart = copy.pageStart;
        this.fitWidth = copy.fitWidth;
        this.fitHeight = copy.fitHeight;
        this.horizontalPrintResolution = copy.horizontalPrintResolution;
        this.verticalPrintResolution = copy.verticalPrintResolution;
        this.leftMargin = copy.leftMargin;
        this.rightMargin = copy.rightMargin;
        this.topMargin = copy.topMargin;
        this.bottomMargin = copy.bottomMargin;
        this.fitToPages = copy.fitToPages;
        this.password = copy.password;
        this.passwordHash = copy.passwordHash;
        this.defaultColumnWidth = copy.defaultColumnWidth;
        this.defaultRowHeight = copy.defaultRowHeight;
        this.zoomFactor = copy.zoomFactor;
        this.pageBreakPreviewMagnification = copy.pageBreakPreviewMagnification;
        this.normalMagnification = copy.normalMagnification;
        this.showGridLines = copy.showGridLines;
        this.displayZeroValues = copy.displayZeroValues;
        this.pageBreakPreviewMode = copy.pageBreakPreviewMode;
        this.horizontalFreeze = copy.horizontalFreeze;
        this.verticalFreeze = copy.verticalFreeze;
        this.horizontalCentre = copy.horizontalCentre;
        this.verticalCentre = copy.verticalCentre;
        this.copies = copy.copies;
        this.header = new HeaderFooter(copy.header);
        this.footer = new HeaderFooter(copy.footer);
        this.automaticFormulaCalculation = copy.automaticFormulaCalculation;
        this.recalculateFormulasBeforeSave = copy.recalculateFormulasBeforeSave;
        if (copy.printArea != null) {
            this.printArea = new SheetRangeImpl(this.sheet, copy.getPrintArea().getTopLeft().getColumn(), copy.getPrintArea().getTopLeft().getRow(), copy.getPrintArea().getBottomRight().getColumn(), copy.getPrintArea().getBottomRight().getRow());
        }
        if (copy.printTitlesRow != null) {
            this.printTitlesRow = new SheetRangeImpl(this.sheet, copy.getPrintTitlesRow().getTopLeft().getColumn(), copy.getPrintTitlesRow().getTopLeft().getRow(), copy.getPrintTitlesRow().getBottomRight().getColumn(), copy.getPrintTitlesRow().getBottomRight().getRow());
        }
        if (copy.printTitlesCol != null) {
            this.printTitlesCol = new SheetRangeImpl(this.sheet, copy.getPrintTitlesCol().getTopLeft().getColumn(), copy.getPrintTitlesCol().getTopLeft().getRow(), copy.getPrintTitlesCol().getBottomRight().getColumn(), copy.getPrintTitlesCol().getBottomRight().getRow());
        }
    }

    public void setOrientation(PageOrientation po) {
        this.orientation = po;
    }

    public PageOrientation getOrientation() {
        return this.orientation;
    }

    public PageOrder getPageOrder() {
        return this.pageOrder;
    }

    public void setPageOrder(PageOrder order) {
        this.pageOrder = order;
    }

    public void setPaperSize(PaperSize ps) {
        this.paperSize = ps;
    }

    public PaperSize getPaperSize() {
        return this.paperSize;
    }

    public boolean isProtected() {
        return this.sheetProtected;
    }

    public void setProtected(boolean p) {
        this.sheetProtected = p;
    }

    public void setHeaderMargin(double d) {
        this.headerMargin = d;
    }

    public double getHeaderMargin() {
        return this.headerMargin;
    }

    public void setFooterMargin(double d) {
        this.footerMargin = d;
    }

    public double getFooterMargin() {
        return this.footerMargin;
    }

    public void setHidden(boolean h) {
        this.hidden = h;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setSelected(boolean s) {
        this.selected = s;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setScaleFactor(int sf) {
        this.scaleFactor = sf;
        this.fitToPages = false;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }

    public void setPageStart(int ps) {
        this.pageStart = ps;
    }

    public int getPageStart() {
        return this.pageStart;
    }

    public void setFitWidth(int fw) {
        this.fitWidth = fw;
        this.fitToPages = true;
    }

    public int getFitWidth() {
        return this.fitWidth;
    }

    public void setFitHeight(int fh) {
        this.fitHeight = fh;
        this.fitToPages = true;
    }

    public int getFitHeight() {
        return this.fitHeight;
    }

    public void setHorizontalPrintResolution(int hpw) {
        this.horizontalPrintResolution = hpw;
    }

    public int getHorizontalPrintResolution() {
        return this.horizontalPrintResolution;
    }

    public void setVerticalPrintResolution(int vpw) {
        this.verticalPrintResolution = vpw;
    }

    public int getVerticalPrintResolution() {
        return this.verticalPrintResolution;
    }

    public void setRightMargin(double m) {
        this.rightMargin = m;
    }

    public double getRightMargin() {
        return this.rightMargin;
    }

    public void setLeftMargin(double m) {
        this.leftMargin = m;
    }

    public double getLeftMargin() {
        return this.leftMargin;
    }

    public void setTopMargin(double m) {
        this.topMargin = m;
    }

    public double getTopMargin() {
        return this.topMargin;
    }

    public void setBottomMargin(double m) {
        this.bottomMargin = m;
    }

    public double getBottomMargin() {
        return this.bottomMargin;
    }

    public double getDefaultWidthMargin() {
        return 0.75d;
    }

    public double getDefaultHeightMargin() {
        return 1.0d;
    }

    public boolean getFitToPages() {
        return this.fitToPages;
    }

    public void setFitToPages(boolean b) {
        this.fitToPages = b;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(int ph) {
        this.passwordHash = ph;
    }

    public int getDefaultColumnWidth() {
        return this.defaultColumnWidth;
    }

    public void setDefaultColumnWidth(int w) {
        this.defaultColumnWidth = w;
    }

    public int getDefaultRowHeight() {
        return this.defaultRowHeight;
    }

    public void setDefaultRowHeight(int h) {
        this.defaultRowHeight = h;
    }

    public int getZoomFactor() {
        return this.zoomFactor;
    }

    public void setZoomFactor(int zf) {
        this.zoomFactor = zf;
    }

    public int getPageBreakPreviewMagnification() {
        return this.pageBreakPreviewMagnification;
    }

    public int getNormalMagnification() {
        return this.normalMagnification;
    }

    public boolean getDisplayZeroValues() {
        return this.displayZeroValues;
    }

    public void setDisplayZeroValues(boolean b) {
        this.displayZeroValues = b;
    }

    public boolean getShowGridLines() {
        return this.showGridLines;
    }

    public void setShowGridLines(boolean b) {
        this.showGridLines = b;
    }

    public boolean getPageBreakPreviewMode() {
        return this.pageBreakPreviewMode;
    }

    public void setPageBreakPreviewMode(boolean b) {
        this.pageBreakPreviewMode = b;
    }

    public boolean getPrintGridLines() {
        return this.printGridLines;
    }

    public void setPrintGridLines(boolean b) {
        this.printGridLines = b;
    }

    public boolean getPrintHeaders() {
        return this.printHeaders;
    }

    public void setPrintHeaders(boolean b) {
        this.printHeaders = b;
    }

    public int getHorizontalFreeze() {
        return this.horizontalFreeze;
    }

    public void setHorizontalFreeze(int row) {
        this.horizontalFreeze = Math.max(row, 0);
    }

    public int getVerticalFreeze() {
        return this.verticalFreeze;
    }

    public void setVerticalFreeze(int col) {
        this.verticalFreeze = Math.max(col, 0);
    }

    public void setCopies(int c) {
        this.copies = c;
    }

    public int getCopies() {
        return this.copies;
    }

    public HeaderFooter getHeader() {
        return this.header;
    }

    public void setHeader(HeaderFooter h) {
        this.header = h;
    }

    public void setFooter(HeaderFooter f) {
        this.footer = f;
    }

    public HeaderFooter getFooter() {
        return this.footer;
    }

    public boolean isHorizontalCentre() {
        return this.horizontalCentre;
    }

    public void setHorizontalCentre(boolean horizCentre) {
        this.horizontalCentre = horizCentre;
    }

    public boolean isVerticalCentre() {
        return this.verticalCentre;
    }

    public void setVerticalCentre(boolean vertCentre) {
        this.verticalCentre = vertCentre;
    }

    public void setAutomaticFormulaCalculation(boolean auto) {
        this.automaticFormulaCalculation = auto;
    }

    public boolean getAutomaticFormulaCalculation() {
        return this.automaticFormulaCalculation;
    }

    public void setRecalculateFormulasBeforeSave(boolean recalc) {
        this.recalculateFormulasBeforeSave = recalc;
    }

    public boolean getRecalculateFormulasBeforeSave() {
        return this.recalculateFormulasBeforeSave;
    }

    public void setPrintArea(int firstCol, int firstRow, int lastCol, int lastRow) {
        this.printArea = new SheetRangeImpl(this.sheet, firstCol, firstRow, lastCol, lastRow);
    }

    public Range getPrintArea() {
        return this.printArea;
    }

    public void setPrintTitlesRow(int firstRow, int lastRow) {
        this.printTitlesRow = new SheetRangeImpl(this.sheet, 0, firstRow, Light.MAIN_KEY_MAX, lastRow);
    }

    public void setPrintTitlesCol(int firstCol, int lastCol) {
        this.printTitlesCol = new SheetRangeImpl(this.sheet, firstCol, 0, lastCol, 65535);
    }

    public Range getPrintTitlesRow() {
        return this.printTitlesRow;
    }

    public Range getPrintTitlesCol() {
        return this.printTitlesCol;
    }
}
