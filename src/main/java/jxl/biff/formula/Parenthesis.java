package jxl.biff.formula;

import java.util.Stack;

class Parenthesis extends Operator {
    public int read(byte[] data, int pos) {
        return 0;
    }

    public void getOperands(Stack s) {
        add((ParseItem) s.pop());
    }

    public void getString(StringBuffer buf) {
        ParseItem[] operands = getOperands();
        buf.append('(');
        operands[0].getString(buf);
        buf.append(')');
    }

    Token getToken() {
        return Token.PARENTHESIS;
    }

    byte[] getBytes() {
        byte[] data = getOperands()[0].getBytes();
        byte[] newdata = new byte[(data.length + 1)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        newdata[data.length] = (byte) getToken().getCode();
        return newdata;
    }

    int getPrecedence() {
        return 4;
    }
}
