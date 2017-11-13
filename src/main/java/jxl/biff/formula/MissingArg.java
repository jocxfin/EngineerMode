package jxl.biff.formula;

class MissingArg extends Operand {
    public int read(byte[] data, int pos) {
        return 0;
    }

    byte[] getBytes() {
        return new byte[]{(byte) Token.MISSING_ARG.getCode()};
    }

    public void getString(StringBuffer buf) {
    }
}
