package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.Cell;
import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class CellReference extends Operand {
    private static Logger logger = Logger.getLogger(CellReference.class);
    private int column;
    private boolean columnRelative;
    private Cell relativeTo;
    private int row;
    private boolean rowRelative;

    public CellReference(Cell rt) {
        this.relativeTo = rt;
    }

    public CellReference(String s) {
        this.column = CellReferenceHelper.getColumn(s);
        this.row = CellReferenceHelper.getRow(s);
        this.columnRelative = CellReferenceHelper.isColumnRelative(s);
        this.rowRelative = CellReferenceHelper.isRowRelative(s);
    }

    public int read(byte[] data, int pos) {
        boolean z;
        boolean z2 = false;
        this.row = IntegerHelper.getInt(data[pos], data[pos + 1]);
        int columnMask = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
        this.column = columnMask & Light.MAIN_KEY_MAX;
        if ((columnMask & 16384) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.columnRelative = z;
        if ((32768 & columnMask) != 0) {
            z2 = true;
        }
        this.rowRelative = z2;
        return 4;
    }

    public void getString(StringBuffer buf) {
        boolean z;
        boolean z2 = false;
        int i = this.column;
        if (this.columnRelative) {
            z = false;
        } else {
            z = true;
        }
        int i2 = this.row;
        if (!this.rowRelative) {
            z2 = true;
        }
        CellReferenceHelper.getCellReference(i, z, i2, z2, buf);
    }

    byte[] getBytes() {
        byte[] data = new byte[5];
        data[0] = (byte) (useAlternateCode() ? Token.REF.getCode2() : Token.REF.getCode());
        IntegerHelper.getTwoBytes(this.row, data, 1);
        int grcol = this.column;
        if (this.rowRelative) {
            grcol |= 32768;
        }
        if (this.columnRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 3);
        return data;
    }
}
