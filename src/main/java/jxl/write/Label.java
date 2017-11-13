package jxl.write;

import jxl.LabelCell;
import jxl.write.biff.LabelRecord;

public class Label extends LabelRecord implements WritableCell, LabelCell {
    public Label(int c, int r, String cont) {
        super(c, r, cont);
    }

    public Label(LabelCell lc) {
        super(lc);
    }
}
