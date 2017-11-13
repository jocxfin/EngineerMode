package jxl.biff.formula;

class Power extends BinaryOperator {
    public String getSymbol() {
        return "^";
    }

    Token getToken() {
        return Token.POWER;
    }

    int getPrecedence() {
        return 1;
    }
}
