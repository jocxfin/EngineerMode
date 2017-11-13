package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.Cell;
import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;

class SharedFormulaArea extends Operand {
    private int columnFirst;
    private boolean columnFirstRelative;
    private int columnLast;
    private boolean columnLastRelative;
    private Cell relativeTo;
    private int rowFirst;
    private boolean rowFirstRelative;
    private int rowLast;
    private boolean rowLastRelative;

    public SharedFormulaArea(Cell rt) {
        this.relativeTo = rt;
    }

    public int read(byte[] data, int pos) {
        boolean z;
        boolean z2 = false;
        this.rowFirst = IntegerHelper.getShort(data[pos], data[pos + 1]);
        this.rowLast = IntegerHelper.getShort(data[pos + 2], data[pos + 3]);
        int columnMask = IntegerHelper.getInt(data[pos + 4], data[pos + 5]);
        this.columnFirst = columnMask & Light.MAIN_KEY_MAX;
        this.columnFirstRelative = (columnMask & 16384) != 0;
        if ((columnMask & 32768) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.rowFirstRelative = z;
        if (this.columnFirstRelative) {
            this.columnFirst = this.relativeTo.getColumn() + this.columnFirst;
        }
        if (this.rowFirstRelative) {
            this.rowFirst = this.relativeTo.getRow() + this.rowFirst;
        }
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
        if (this.columnLastRelative) {
            this.columnLast = this.relativeTo.getColumn() + this.columnLast;
        }
        if (this.rowLastRelative) {
            this.rowLast = this.relativeTo.getRow() + this.rowLast;
        }
        return 8;
    }

    public void getString(StringBuffer buf) {
        CellReferenceHelper.getCellReference(this.columnFirst, this.rowFirst, buf);
        buf.append(':');
        CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
    }

    byte[] getBytes() {
        byte[] data = new byte[9];
        data[0] = (byte) Token.AREA.getCode();
        IntegerHelper.getTwoBytes(this.rowFirst, data, 1);
        IntegerHelper.getTwoBytes(this.rowLast, data, 3);
        IntegerHelper.getTwoBytes(this.columnFirst, data, 5);
        IntegerHelper.getTwoBytes(this.columnLast, data, 7);
        return data;
    }
}
