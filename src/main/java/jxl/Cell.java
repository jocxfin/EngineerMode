package jxl;

import jxl.format.CellFormat;

public interface Cell {
    CellFeatures getCellFeatures();

    CellFormat getCellFormat();

    int getColumn();

    String getContents();

    int getRow();

    CellType getType();
}
