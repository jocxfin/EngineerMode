package jxl.read.biff;

import jxl.biff.Type;

class LeftMarginRecord extends MarginRecord {
    LeftMarginRecord(Record r) {
        super(Type.LEFTMARGIN, r);
    }
}
