package jxl.biff;

import jxl.CellFeatures;
import jxl.CellType;
import jxl.format.CellFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;

public class EmptyCell implements WritableCell {
    private int col;
    private int row;

    public EmptyCell(int c, int r) {
        this.row = r;
        this.col = c;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.col;
    }

    public CellType getType() {
        return CellType.EMPTY;
    }

    public String getContents() {
        return "";
    }

    public CellFormat getCellFormat() {
        return null;
    }

    public void setCellFormat(CellFormat cf) {
    }

    public CellFeatures getCellFeatures() {
        return null;
    }

    public WritableCellFeatures getWritableCellFeatures() {
        return null;
    }

    public void setCellFeatures(WritableCellFeatures wcf) {
    }
}
