package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;
import jxl.common.Assert;
import jxl.common.Logger;

class Area3d extends Operand {
    private static Logger logger = Logger.getLogger(Area3d.class);
    private int columnFirst;
    private boolean columnFirstRelative;
    private int columnLast;
    private boolean columnLastRelative;
    private int rowFirst;
    private boolean rowFirstRelative;
    private int rowLast;
    private boolean rowLastRelative;
    private int sheet;
    private ExternalSheet workbook;

    Area3d(ExternalSheet es) {
        this.workbook = es;
    }

    Area3d(String s, ExternalSheet es) throws FormulaException {
        boolean z;
        this.workbook = es;
        int seppos = s.lastIndexOf(":");
        if (seppos == -1) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        String endcell = s.substring(seppos + 1);
        int sep = s.indexOf(33);
        String cellString = s.substring(sep + 1, seppos);
        this.columnFirst = CellReferenceHelper.getColumn(cellString);
        this.rowFirst = CellReferenceHelper.getRow(cellString);
        String sheetName = s.substring(0, sep);
        if (sheetName.charAt(0) == '\'' && sheetName.charAt(sheetName.length() - 1) == '\'') {
            sheetName = sheetName.substring(1, sheetName.length() - 1);
        }
        this.sheet = es.getExternalSheetIndex(sheetName);
        if (this.sheet >= 0) {
            this.columnLast = CellReferenceHelper.getColumn(endcell);
            this.rowLast = CellReferenceHelper.getRow(endcell);
            this.columnFirstRelative = true;
            this.rowFirstRelative = true;
            this.columnLastRelative = true;
            this.rowLastRelative = true;
            return;
        }
        throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, sheetName);
    }

    int getFirstColumn() {
        return this.columnFirst;
    }

    int getLastColumn() {
        return this.columnLast;
    }

    public int read(byte[] data, int pos) {
        boolean z;
        boolean z2 = false;
        this.sheet = IntegerHelper.getInt(data[pos], data[pos + 1]);
        this.rowFirst = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
        this.rowLast = IntegerHelper.getInt(data[pos + 4], data[pos + 5]);
        int columnMask = IntegerHelper.getInt(data[pos + 6], data[pos + 7]);
        this.columnFirst = columnMask & Light.MAIN_KEY_MAX;
        this.columnFirstRelative = (columnMask & 16384) != 0;
        if ((columnMask & 32768) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.rowFirstRelative = z;
        columnMask = IntegerHelper.getInt(data[pos + 8], data[pos + 9]);
        this.columnLast = columnMask & Light.MAIN_KEY_MAX;
        if ((columnMask & 16384) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.columnLastRelative = z;
        if ((columnMask & 32768) != 0) {
            z2 = true;
        }
        this.rowLastRelative = z2;
        return 10;
    }

    public void getString(StringBuffer buf) {
        CellReferenceHelper.getCellReference(this.sheet, this.columnFirst, this.rowFirst, this.workbook, buf);
        buf.append(':');
        CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
    }

    byte[] getBytes() {
        byte[] data = new byte[11];
        data[0] = (byte) Token.AREA3D.getCode();
        IntegerHelper.getTwoBytes(this.sheet, data, 1);
        IntegerHelper.getTwoBytes(this.rowFirst, data, 3);
        IntegerHelper.getTwoBytes(this.rowLast, data, 5);
        int grcol = this.columnFirst;
        if (this.rowFirstRelative) {
            grcol |= 32768;
        }
        if (this.columnFirstRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 7);
        grcol = this.columnLast;
        if (this.rowLastRelative) {
            grcol |= 32768;
        }
        if (this.columnLastRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 9);
        return data;
    }

    protected void setRangeData(int sht, int colFirst, int colLast, int rwFirst, int rwLast, boolean colFirstRel, boolean colLastRel, boolean rowFirstRel, boolean rowLastRel) {
        this.sheet = sht;
        this.columnFirst = colFirst;
        this.columnLast = colLast;
        this.rowFirst = rwFirst;
        this.rowLast = rwLast;
        this.columnFirstRelative = colFirstRel;
        this.columnLastRelative = colLastRel;
        this.rowFirstRelative = rowFirstRel;
        this.rowLastRelative = rowLastRel;
    }
}
