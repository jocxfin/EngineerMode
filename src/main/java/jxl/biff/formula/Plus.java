package jxl.biff.formula;

class Plus extends StringOperator {
    Operator getBinaryOperator() {
        return new Add();
    }

    Operator getUnaryOperator() {
        return new UnaryPlus();
    }
}
