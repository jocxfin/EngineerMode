package jxl.biff.formula;

import jxl.biff.IntegerHelper;
import jxl.biff.NameRangeException;
import jxl.biff.WorkbookMethods;
import jxl.common.Assert;
import jxl.common.Logger;

class NameRange extends Operand {
    private static Logger logger = Logger.getLogger(NameRange.class);
    private int index;
    private String name;
    private WorkbookMethods nameTable;

    public NameRange(WorkbookMethods nt) {
        boolean z;
        this.nameTable = nt;
        if (this.nameTable == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
    }

    public NameRange(String nm, WorkbookMethods nt) throws FormulaException {
        this.name = nm;
        this.nameTable = nt;
        this.index = this.nameTable.getNameIndex(this.name);
        if (this.index >= 0) {
            this.index++;
            return;
        }
        throw new FormulaException(FormulaException.CELL_NAME_NOT_FOUND, this.name);
    }

    public int read(byte[] data, int pos) throws FormulaException {
        try {
            this.index = IntegerHelper.getInt(data[pos], data[pos + 1]);
            this.name = this.nameTable.getName(this.index - 1);
            return 4;
        } catch (NameRangeException e) {
            throw new FormulaException(FormulaException.CELL_NAME_NOT_FOUND, "");
        }
    }

    byte[] getBytes() {
        byte[] data = new byte[5];
        data[0] = (byte) Token.NAMED_RANGE.getValueCode();
        if (getParseContext() == ParseContext.DATA_VALIDATION) {
            data[0] = (byte) Token.NAMED_RANGE.getReferenceCode();
        }
        IntegerHelper.getTwoBytes(this.index, data, 1);
        return data;
    }

    public void getString(StringBuffer buf) {
        buf.append(this.name);
    }
}
