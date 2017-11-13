package jxl.biff.formula;

class NotEqual extends BinaryOperator {
    public String getSymbol() {
        return "<>";
    }

    Token getToken() {
        return Token.NOT_EQUAL;
    }

    int getPrecedence() {
        return 5;
    }
}
