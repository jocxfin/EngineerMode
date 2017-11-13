package jxl.read.biff;

import jxl.biff.Type;

class RightMarginRecord extends MarginRecord {
    RightMarginRecord(Record r) {
        super(Type.RIGHTMARGIN, r);
    }
}
