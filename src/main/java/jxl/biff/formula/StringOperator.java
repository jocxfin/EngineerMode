package jxl.biff.formula;

import java.util.Stack;

import jxl.common.Assert;

abstract class StringOperator extends Operator {
    abstract Operator getBinaryOperator();

    abstract Operator getUnaryOperator();

    protected StringOperator() {
    }

    public void getOperands(Stack s) {
        Assert.verify(false);
    }

    int getPrecedence() {
        Assert.verify(false);
        return 0;
    }

    byte[] getBytes() {
        Assert.verify(false);
        return null;
    }

    void getString(StringBuffer buf) {
        Assert.verify(false);
    }
}
