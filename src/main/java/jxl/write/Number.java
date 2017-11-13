package jxl.write;

import jxl.NumberCell;
import jxl.write.biff.NumberRecord;

public class Number extends NumberRecord implements WritableCell, NumberCell {
    public Number(int c, int r, double val) {
        super(c, r, val);
    }

    public Number(NumberCell nc) {
        super(nc);
    }
}
