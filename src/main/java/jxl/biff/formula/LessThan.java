package jxl.biff.formula;

class LessThan extends BinaryOperator {
    public String getSymbol() {
        return "<";
    }

    Token getToken() {
        return Token.LESS_THAN;
    }

    int getPrecedence() {
        return 5;
    }
}
