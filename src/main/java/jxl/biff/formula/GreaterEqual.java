package jxl.biff.formula;

class GreaterEqual extends BinaryOperator {
    public String getSymbol() {
        return ">=";
    }

    Token getToken() {
        return Token.GREATER_EQUAL;
    }

    int getPrecedence() {
        return 5;
    }
}
