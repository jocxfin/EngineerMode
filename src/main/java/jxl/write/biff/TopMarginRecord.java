package jxl.write.biff;

import jxl.biff.Type;

class TopMarginRecord extends MarginRecord {
    TopMarginRecord(double v) {
        super(Type.TOPMARGIN, v);
    }
}
