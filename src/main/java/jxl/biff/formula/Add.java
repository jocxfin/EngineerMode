package jxl.biff.formula;

class Add extends BinaryOperator {
    public String getSymbol() {
        return "+";
    }

    Token getToken() {
        return Token.ADD;
    }

    int getPrecedence() {
        return 4;
    }
}
