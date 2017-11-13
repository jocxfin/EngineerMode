package jxl.write.biff;

import jxl.biff.Type;

class RightMarginRecord extends MarginRecord {
    RightMarginRecord(double v) {
        super(Type.RIGHTMARGIN, v);
    }
}
