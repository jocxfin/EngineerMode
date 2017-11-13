package jxl.write;

import jxl.Sheet;
import jxl.write.biff.RowsExceededException;

public interface WritableSheet extends Sheet {
    void addCell(WritableCell writableCell) throws WriteException, RowsExceededException;
}
