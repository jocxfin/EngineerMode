package jxl.biff.formula;

import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class IntegerValue extends NumberValue {
    private static Logger logger = Logger.getLogger(IntegerValue.class);
    private boolean outOfRange;
    private double value;

    public IntegerValue() {
        this.outOfRange = false;
    }

    public IntegerValue(String s) {
        boolean z;
        try {
            this.value = (double) Integer.parseInt(s);
        } catch (NumberFormatException e) {
            logger.warn(e, e);
            this.value = 0.0d;
        }
        if (this.value != ((double) ((short) ((int) this.value)))) {
            z = true;
        } else {
            z = false;
        }
        this.outOfRange = z;
    }

    public int read(byte[] data, int pos) {
        this.value = (double) IntegerHelper.getInt(data[pos], data[pos + 1]);
        return 2;
    }

    byte[] getBytes() {
        byte[] data = new byte[3];
        data[0] = (byte) Token.INTEGER.getCode();
        IntegerHelper.getTwoBytes((int) this.value, data, 1);
        return data;
    }

    public double getValue() {
        return this.value;
    }

    boolean isOutOfRange() {
        return this.outOfRange;
    }
}
