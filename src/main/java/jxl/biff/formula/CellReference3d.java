package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.Cell;
import jxl.biff.CellReferenceHelper;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class CellReference3d extends Operand {
    private static Logger logger = Logger.getLogger(CellReference3d.class);
    private int column;
    private boolean columnRelative;
    private Cell relativeTo;
    private int row;
    private boolean rowRelative;
    private int sheet;
    private ExternalSheet workbook;

    public CellReference3d(Cell rt, ExternalSheet w) {
        this.relativeTo = rt;
        this.workbook = w;
    }

    public CellReference3d(String s, ExternalSheet w) throws FormulaException {
        this.workbook = w;
        this.columnRelative = true;
        this.rowRelative = true;
        int sep = s.indexOf(33);
        String cellString = s.substring(sep + 1);
        this.column = CellReferenceHelper.getColumn(cellString);
        this.row = CellReferenceHelper.getRow(cellString);
        String sheetName = s.substring(0, sep);
        if (sheetName.charAt(0) == '\'' && sheetName.charAt(sheetName.length() - 1) == '\'') {
            sheetName = sheetName.substring(1, sheetName.length() - 1);
        }
        this.sheet = w.getExternalSheetIndex(sheetName);
        if (this.sheet < 0) {
            throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, sheetName);
        }
    }

    public int read(byte[] data, int pos) {
        boolean z;
        boolean z2 = false;
        this.sheet = IntegerHelper.getInt(data[pos], data[pos + 1]);
        this.row = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
        int columnMask = IntegerHelper.getInt(data[pos + 4], data[pos + 5]);
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
        return 6;
    }

    public void getString(StringBuffer buf) {
        boolean z;
        boolean z2 = false;
        int i = this.sheet;
        int i2 = this.column;
        if (this.columnRelative) {
            z = false;
        } else {
            z = true;
        }
        int i3 = this.row;
        if (!this.rowRelative) {
            z2 = true;
        }
        CellReferenceHelper.getCellReference(i, i2, z, i3, z2, this.workbook, buf);
    }

    byte[] getBytes() {
        byte[] data = new byte[7];
        data[0] = (byte) Token.REF3D.getCode();
        IntegerHelper.getTwoBytes(this.sheet, data, 1);
        IntegerHelper.getTwoBytes(this.row, data, 3);
        int grcol = this.column;
        if (this.rowRelative) {
            grcol |= 32768;
        }
        if (this.columnRelative) {
            grcol |= 16384;
        }
        IntegerHelper.getTwoBytes(grcol, data, 5);
        return data;
    }
}
