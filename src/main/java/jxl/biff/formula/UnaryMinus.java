package jxl.biff.formula;

class UnaryMinus extends UnaryOperator {
    public String getSymbol() {
        return "-";
    }

    Token getToken() {
        return Token.UNARY_MINUS;
    }

    int getPrecedence() {
        return 2;
    }
}
