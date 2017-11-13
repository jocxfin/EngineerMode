package jxl.write.biff;

import jxl.Cell;
import jxl.CellType;
import jxl.biff.Type;
import jxl.common.Logger;
import jxl.format.CellFormat;

public abstract class BlankRecord extends CellValue {
    private static Logger logger = Logger.getLogger(BlankRecord.class);

    protected BlankRecord(int c, int r) {
        super(Type.BLANK, c, r);
    }

    protected BlankRecord(int c, int r, CellFormat st) {
        super(Type.BLANK, c, r, st);
    }

    protected BlankRecord(Cell c) {
        super(Type.BLANK, c);
    }

    public CellType getType() {
        return CellType.EMPTY;
    }

    public String getContents() {
        return "";
    }
}
