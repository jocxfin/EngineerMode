package jxl.write;

import jxl.Cell;
import jxl.format.CellFormat;

public interface WritableCell extends Cell {
    WritableCellFeatures getWritableCellFeatures();

    void setCellFeatures(WritableCellFeatures writableCellFeatures);

    void setCellFormat(CellFormat cellFormat);
}
