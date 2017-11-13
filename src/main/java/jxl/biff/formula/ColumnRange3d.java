package jxl.biff.formula;

import jxl.biff.CellReferenceHelper;
import jxl.common.Assert;
import jxl.common.Logger;

class ColumnRange3d extends Area3d {
    private static Logger logger = Logger.getLogger(ColumnRange3d.class);
    private int sheet;
    private ExternalSheet workbook;

    ColumnRange3d(String s, ExternalSheet es) throws FormulaException {
        boolean z;
        super(es);
        this.workbook = es;
        int seppos = s.lastIndexOf(":");
        if (seppos == -1) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        String startcell = s.substring(0, seppos);
        String endcell = s.substring(seppos + 1);
        int sep = s.indexOf(33);
        int columnFirst = CellReferenceHelper.getColumn(s.substring(sep + 1, seppos));
        String sheetName = s.substring(0, sep);
        int sheetNamePos = sheetName.lastIndexOf(93);
        if (sheetName.charAt(0) == '\'') {
            if (sheetName.charAt(sheetName.length() - 1) == '\'') {
                sheetName = sheetName.substring(1, sheetName.length() - 1);
            }
        }
        this.sheet = es.getExternalSheetIndex(sheetName);
        if (this.sheet >= 0) {
            setRangeData(this.sheet, columnFirst, CellReferenceHelper.getColumn(endcell), 0, 65535, true, true, true, true);
            return;
        }
        throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, sheetName);
    }

    public void getString(StringBuffer buf) {
        buf.append('\'');
        buf.append(this.workbook.getExternalSheetName(this.sheet));
        buf.append('\'');
        buf.append('!');
        CellReferenceHelper.getColumnReference(getFirstColumn(), buf);
        buf.append(':');
        CellReferenceHelper.getColumnReference(getLastColumn(), buf);
    }
}
