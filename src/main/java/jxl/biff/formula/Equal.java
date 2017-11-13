package jxl.biff.formula;

class Equal extends BinaryOperator {
    public String getSymbol() {
        return "=";
    }

    Token getToken() {
        return Token.EQUAL;
    }

    int getPrecedence() {
        return 5;
    }
}
