package jxl.biff.formula;

class UnaryPlus extends UnaryOperator {
    public String getSymbol() {
        return "+";
    }

    Token getToken() {
        return Token.UNARY_PLUS;
    }

    int getPrecedence() {
        return 2;
    }
}
