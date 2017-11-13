package jxl;

import jxl.biff.BaseCellFeatures;

public class CellFeatures extends BaseCellFeatures {
    protected CellFeatures(CellFeatures cf) {
        super(cf);
    }

    public String getComment() {
        return super.getComment();
    }
}
