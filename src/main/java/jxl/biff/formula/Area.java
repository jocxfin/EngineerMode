package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;
import jxl.common.Assert;
import jxl.common.Logger;

class Area extends Operand {
    private static Logger logger = Logger.getLogger(Area.class);
    private int columnFirst;
    private boolean columnFirstRelative;
    private int columnLast;
    private boolean columnLastRelative;
    private int rowFirst;
    private boolean rowFirstRelative;
    private int rowLast;
    private boolean rowLastRelative;

    Area() {
    }

    Area(String s) {
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
        this.columnFirst = CellReferenceHelper.getColumn(startcell);
        this.rowFirst = CellReferenceHelper.getRow(startcell);
        this.columnLast = CellReferenceHelper.getColumn(endcell);
        this.rowLast = CellReferenceHelper.getRow(endcell);
        this.columnFirstRelative = CellReferenceHelper.isColumnRelative(startcell);
        this.rowFirstRelative = CellReferenceHelper.isRowRelative(startcell);
        this.columnLastRelative = CellReferenceHelper.isColumnRelative(endcell);
        this.rowLastRelative = CellReferenceHelper.isRowRelative(endcell);
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
        this.rowFirst = IntegerHelper.getInt(data[pos], data[pos + 1]);
        this.rowLast = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
        int columnMask = IntegerHelper.getInt(data[pos + 4], data[pos + 5]);
        this.columnFirst = columnMask & Light.MAIN_KEY_MAX;
        this.columnFirstRelative = (columnMask & 16384) != 0;
        if ((columnMask & 32768) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.rowFirstRelative = z;
        columnMask = IntegerHelper.getInt(data[pos + 6], data[pos + 7]);
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
        return 8;
    }

    public void getString(StringBuffer buf) {
        CellReferenceHelper.getCellReference(this.columnFirst, this.rowFirst, buf);
        buf.append(':');
        CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
    }

    byte[] getBytes() {
        int code2;
        byte[] data = new byte[9];
        if (useAlternateCode()) {
            code2 = Token.AREA.getCode2();
        } else {
            code2 = Token.AREA.getCode();
        }
        data[0] = (byte) code2;
        IntegerHelper.getTwoBytes(this.rowFirst, data, 1);
        IntegerHelper.getTwoBytes(this.rowLast, data, 3);
        int grcol = this.columnFirst;
        if (this.rowFirstRelative) {
            grcol |= 32768;
        }
        if (this.columnFirstRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 5);
        grcol = this.columnLast;
        if (this.rowLastRelative) {
            grcol |= 32768;
        }
        if (this.columnLastRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 7);
        return data;
    }

    protected void setRangeData(int colFirst, int colLast, int rwFirst, int rwLast, boolean colFirstRel, boolean colLastRel, boolean rowFirstRel, boolean rowLastRel) {
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
