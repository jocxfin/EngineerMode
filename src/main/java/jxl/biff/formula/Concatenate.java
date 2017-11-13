package jxl.biff.formula;

class Concatenate extends BinaryOperator {
    public String getSymbol() {
        return "&";
    }

    Token getToken() {
        return Token.CONCAT;
    }

    int getPrecedence() {
        return 3;
    }
}
