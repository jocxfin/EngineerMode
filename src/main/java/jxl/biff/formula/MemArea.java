package jxl.biff.formula;

import jxl.biff.IntegerHelper;

class MemArea extends SubExpression {
    public void getString(StringBuffer buf) {
        ParseItem[] subExpression = getSubExpression();
        if (subExpression.length == 1) {
            subExpression[0].getString(buf);
        } else if (subExpression.length == 2) {
            subExpression[1].getString(buf);
            buf.append(':');
            subExpression[0].getString(buf);
        }
    }

    public int read(byte[] data, int pos) {
        setLength(IntegerHelper.getInt(data[pos + 4], data[pos + 5]));
        return 6;
    }
}
