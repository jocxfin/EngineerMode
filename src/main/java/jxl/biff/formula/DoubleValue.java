package jxl.biff.formula;

import jxl.biff.DoubleHelper;
import jxl.common.Logger;

class DoubleValue extends NumberValue {
    private static Logger logger = Logger.getLogger(DoubleValue.class);
    private double value;

    DoubleValue(double v) {
        this.value = v;
    }

    public DoubleValue(String s) {
        try {
            this.value = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            logger.warn(e, e);
            this.value = 0.0d;
        }
    }

    public int read(byte[] data, int pos) {
        this.value = DoubleHelper.getIEEEDouble(data, pos);
        return 8;
    }

    byte[] getBytes() {
        byte[] data = new byte[9];
        data[0] = (byte) Token.DOUBLE.getCode();
        DoubleHelper.getIEEEBytes(this.value, data, 1);
        return data;
    }

    public double getValue() {
        return this.value;
    }
}
