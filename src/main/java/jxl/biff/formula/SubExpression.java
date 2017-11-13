package jxl.biff.formula;

import jxl.biff.IntegerHelper;

abstract class SubExpression extends Operand {
    private int length;
    private ParseItem[] subExpression;

    protected SubExpression() {
    }

    public int read(byte[] data, int pos) {
        this.length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        return 2;
    }

    byte[] getBytes() {
        return null;
    }

    public int getLength() {
        return this.length;
    }

    protected final void setLength(int l) {
        this.length = l;
    }

    public void setSubExpression(ParseItem[] pi) {
        this.subExpression = pi;
    }

    protected ParseItem[] getSubExpression() {
        return this.subExpression;
    }
}
