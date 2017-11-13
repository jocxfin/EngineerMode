package jxl.biff.formula;

class Percent extends UnaryOperator {
    public String getSymbol() {
        return "%";
    }

    public void getString(StringBuffer buf) {
        getOperands()[0].getString(buf);
        buf.append(getSymbol());
    }

    Token getToken() {
        return Token.PERCENT;
    }

    int getPrecedence() {
        return 5;
    }
}
