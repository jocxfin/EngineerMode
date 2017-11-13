package jxl.read.biff;

import jxl.biff.Type;

class BottomMarginRecord extends MarginRecord {
    BottomMarginRecord(Record r) {
        super(Type.BOTTOMMARGIN, r);
    }
}
