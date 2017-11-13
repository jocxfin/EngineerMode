package jxl.biff.formula;

class ErrorConstant extends Operand {
    private FormulaErrorCode error;

    public ErrorConstant(String s) {
        this.error = FormulaErrorCode.getErrorCode(s);
    }

    public int read(byte[] data, int pos) {
        this.error = FormulaErrorCode.getErrorCode(data[pos]);
        return 1;
    }

    byte[] getBytes() {
        return new byte[]{(byte) Token.ERR.getCode(), (byte) ((byte) this.error.getCode())};
    }

    public void getString(StringBuffer buf) {
        buf.append(this.error.getDescription());
    }
}
