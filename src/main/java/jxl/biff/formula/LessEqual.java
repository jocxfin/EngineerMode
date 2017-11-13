package jxl.biff.formula;

class LessEqual extends BinaryOperator {
    public String getSymbol() {
        return "<=";
    }

    Token getToken() {
        return Token.LESS_EQUAL;
    }

    int getPrecedence() {
        return 5;
    }
}
