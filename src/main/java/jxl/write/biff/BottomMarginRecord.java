package jxl.write.biff;

import jxl.biff.Type;

class BottomMarginRecord extends MarginRecord {
    BottomMarginRecord(double v) {
        super(Type.BOTTOMMARGIN, v);
    }
}
