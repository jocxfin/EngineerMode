package jxl.biff.formula;

class Subtract extends BinaryOperator {
    public String getSymbol() {
        return "-";
    }

    Token getToken() {
        return Token.SUBTRACT;
    }

    int getPrecedence() {
        return 4;
    }
}
