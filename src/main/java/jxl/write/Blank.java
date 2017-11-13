package jxl.write;

import jxl.Cell;
import jxl.format.CellFormat;
import jxl.write.biff.BlankRecord;

public class Blank extends BlankRecord implements WritableCell {
    public Blank(int c, int r) {
        super(c, r);
    }

    public Blank(int c, int r, CellFormat st) {
        super(c, r, st);
    }

    public Blank(Cell lc) {
        super(lc);
    }
}
