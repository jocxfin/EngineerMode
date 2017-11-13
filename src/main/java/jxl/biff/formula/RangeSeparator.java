package jxl.biff.formula;

import jxl.biff.IntegerHelper;

class RangeSeparator extends BinaryOperator {
    public String getSymbol() {
        return ":";
    }

    Token getToken() {
        return Token.RANGE;
    }

    int getPrecedence() {
        return 1;
    }

    byte[] getBytes() {
        setVolatile();
        setOperandAlternateCode();
        byte[] funcBytes = super.getBytes();
        byte[] bytes = new byte[(funcBytes.length + 3)];
        System.arraycopy(funcBytes, 0, bytes, 3, funcBytes.length);
        bytes[0] = (byte) Token.MEM_FUNC.getCode();
        IntegerHelper.getTwoBytes(funcBytes.length, bytes, 1);
        return bytes;
    }
}
