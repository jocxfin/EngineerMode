package jxl.read.biff;

import jxl.biff.Type;

class TopMarginRecord extends MarginRecord {
    TopMarginRecord(Record r) {
        super(Type.TOPMARGIN, r);
    }
}
