package jxl.biff.formula;

class Divide extends BinaryOperator {
    public String getSymbol() {
        return "/";
    }

    Token getToken() {
        return Token.DIVIDE;
    }

    int getPrecedence() {
        return 3;
    }
}
