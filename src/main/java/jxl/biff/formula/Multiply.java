package jxl.biff.formula;

class Multiply extends BinaryOperator {
    public String getSymbol() {
        return "*";
    }

    Token getToken() {
        return Token.MULTIPLY;
    }

    int getPrecedence() {
        return 3;
    }
}
