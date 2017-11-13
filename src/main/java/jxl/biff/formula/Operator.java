package jxl.biff.formula;

import java.util.Stack;

abstract class Operator extends ParseItem {
    private ParseItem[] operands = new ParseItem[0];

    public abstract void getOperands(Stack stack);

    abstract int getPrecedence();

    protected void setOperandAlternateCode() {
        for (ParseItem alternateCode : this.operands) {
            alternateCode.setAlternateCode();
        }
    }

    protected void add(ParseItem n) {
        n.setParent(this);
        ParseItem[] newOperands = new ParseItem[(this.operands.length + 1)];
        System.arraycopy(this.operands, 0, newOperands, 0, this.operands.length);
        newOperands[this.operands.length] = n;
        this.operands = newOperands;
    }

    protected ParseItem[] getOperands() {
        return this.operands;
    }
}
