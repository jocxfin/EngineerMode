package jxl.biff.formula;

abstract class NumberValue extends Operand {
    public abstract double getValue();

    protected NumberValue() {
    }

    public void getString(StringBuffer buf) {
        buf.append(Double.toString(getValue()));
    }
}
