package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.Cell;
import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class SharedFormulaCellReference extends Operand {
    private static Logger logger = Logger.getLogger(SharedFormulaCellReference.class);
    private int column;
    private boolean columnRelative;
    private Cell relativeTo;
    private int row;
    private boolean rowRelative;

    public SharedFormulaCellReference(Cell rt) {
        this.relativeTo = rt;
    }

    public int read(byte[] data, int pos) {
        boolean z = false;
        this.row = IntegerHelper.getShort(data[pos], data[pos + 1]);
        int columnMask = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
        this.column = (byte) (columnMask & Light.MAIN_KEY_MAX);
        this.columnRelative = (columnMask & 16384) != 0;
        if ((32768 & columnMask) != 0) {
            z = true;
        }
        this.rowRelative = z;
        if (this.columnRelative && this.relativeTo != null) {
            this.column = this.relativeTo.getColumn() + this.column;
        }
        if (this.rowRelative && this.relativeTo != null) {
            this.row = this.relativeTo.getRow() + this.row;
        }
        return 4;
    }

    public void getString(StringBuffer buf) {
        CellReferenceHelper.getCellReference(this.column, this.row, buf);
    }

    byte[] getBytes() {
        byte[] data = new byte[5];
        data[0] = (byte) Token.REF.getCode();
        IntegerHelper.getTwoBytes(this.row, data, 1);
        int columnMask = this.column;
        if (this.columnRelative) {
            columnMask |= 16384;
        }
        if (this.rowRelative) {
            columnMask |= 32768;
        }
        IntegerHelper.getTwoBytes(columnMask, data, 3);
        return data;
    }
}
