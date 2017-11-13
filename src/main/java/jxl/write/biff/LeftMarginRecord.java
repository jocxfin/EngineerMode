package jxl.write.biff;

import jxl.biff.Type;

class LeftMarginRecord extends MarginRecord {
    LeftMarginRecord(double v) {
        super(Type.LEFTMARGIN, v);
    }
}
