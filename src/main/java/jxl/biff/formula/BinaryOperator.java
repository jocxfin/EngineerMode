package jxl.biff.formula;

import java.util.Stack;

import jxl.common.Logger;

abstract class BinaryOperator extends Operator {
    private static final Logger logger = Logger.getLogger(BinaryOperator.class);

    abstract String getSymbol();

    abstract Token getToken();

    public int read(byte[] data, int pos) {
        return 0;
    }

    public void getOperands(Stack s) {
        ParseItem o2 = (ParseItem) s.pop();
        add((ParseItem) s.pop());
        add(o2);
    }

    public void getString(StringBuffer buf) {
        ParseItem[] operands = getOperands();
        operands[1].getString(buf);
        buf.append(getSymbol());
        operands[0].getString(buf);
    }

    byte[] getBytes() {
        byte[] newdata;
        ParseItem[] operands = getOperands();
        byte[] data = new byte[0];
        for (int i = operands.length - 1; i >= 0; i--) {
            byte[] opdata = operands[i].getBytes();
            newdata = new byte[(data.length + opdata.length)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
            data = newdata;
        }
        newdata = new byte[(data.length + 1)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        newdata[data.length] = (byte) getToken().getCode();
        return newdata;
    }
}
