package jxl.read.biff;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.CellType;
import jxl.biff.FormattingRecords;
import jxl.common.Logger;
import jxl.format.CellFormat;

class MulBlankCell implements Cell, CellFeaturesAccessor {
    private static Logger logger = Logger.getLogger(MulBlankCell.class);
    private CellFormat cellFormat;
    private int column;
    private CellFeatures features;
    private FormattingRecords formattingRecords;
    private boolean initialized = false;
    private int row;
    private SheetImpl sheet;
    private int xfIndex;

    public MulBlankCell(int r, int c, int xfi, FormattingRecords fr, SheetImpl si) {
        this.row = r;
        this.column = c;
        this.xfIndex = xfi;
        this.formattingRecords = fr;
        this.sheet = si;
    }

    public final int getRow() {
        return this.row;
    }

    public final int getColumn() {
        return this.column;
    }

    public String getContents() {
        return "";
    }

    public CellType getType() {
        return CellType.EMPTY;
    }

    public CellFormat getCellFormat() {
        if (!this.initialized) {
            this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
            this.initialized = true;
        }
        return this.cellFormat;
    }

    public CellFeatures getCellFeatures() {
        return this.features;
    }

    public void setCellFeatures(CellFeatures cf) {
        if (this.features != null) {
            logger.warn("current cell features not null - overwriting");
        }
        this.features = cf;
    }
}
