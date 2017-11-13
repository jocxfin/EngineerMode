package jxl.read.biff;

import jxl.biff.DoubleHelper;
import jxl.biff.RecordData;
import jxl.biff.Type;

abstract class MarginRecord extends RecordData {
    private double margin;

    protected MarginRecord(Type t, Record r) {
        super(t);
        this.margin = DoubleHelper.getIEEEDouble(r.getData(), 0);
    }

    double getMargin() {
        return this.margin;
    }
}
