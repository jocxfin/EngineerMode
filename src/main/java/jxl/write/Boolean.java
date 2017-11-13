package jxl.write;

import jxl.BooleanCell;
import jxl.write.biff.BooleanRecord;

public class Boolean extends BooleanRecord implements WritableCell, BooleanCell {
    public Boolean(BooleanCell nc) {
        super(nc);
    }
}
