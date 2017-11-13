package jxl.biff.formula;

import jxl.common.Logger;

class CellReferenceError extends Operand {
    private static Logger logger = Logger.getLogger(CellReferenceError.class);

    public int read(byte[] data, int pos) {
        return 4;
    }

    public void getString(StringBuffer buf) {
        buf.append(FormulaErrorCode.REF.getDescription());
    }

    byte[] getBytes() {
        byte[] data = new byte[5];
        data[0] = (byte) Token.REFERR.getCode();
        return data;
    }
}
