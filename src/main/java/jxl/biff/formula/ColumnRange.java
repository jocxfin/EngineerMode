package jxl.biff.formula;

import jxl.biff.CellReferenceHelper;
import jxl.common.Assert;
import jxl.common.Logger;

class ColumnRange extends Area {
    private static Logger logger = Logger.getLogger(ColumnRange.class);

    ColumnRange() {
    }

    ColumnRange(String s) {
        boolean z;
        int seppos = s.indexOf(":");
        if (seppos == -1) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        String startcell = s.substring(0, seppos);
        String endcell = s.substring(seppos + 1);
        setRangeData(CellReferenceHelper.getColumn(startcell), CellReferenceHelper.getColumn(endcell), 0, 65535, CellReferenceHelper.isColumnRelative(startcell), CellReferenceHelper.isColumnRelative(endcell), false, false);
    }

    public void getString(StringBuffer buf) {
        CellReferenceHelper.getColumnReference(getFirstColumn(), buf);
        buf.append(':');
        CellReferenceHelper.getColumnReference(getLastColumn(), buf);
    }
}
