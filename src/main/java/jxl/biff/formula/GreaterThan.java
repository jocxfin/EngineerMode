package jxl.biff.formula;

class GreaterThan extends BinaryOperator {
    public String getSymbol() {
        return ">";
    }

    Token getToken() {
        return Token.GREATER_THAN;
    }

    int getPrecedence() {
        return 5;
    }
}
