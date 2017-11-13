package jxl.read.biff;

final class RKHelper {
    private RKHelper() {
    }

    public static double getDouble(int rk) {
        double value;
        if ((rk & 2) == 0) {
            value = Double.longBitsToDouble(((long) (rk & -4)) << 32);
            if ((rk & 1) != 0) {
                value /= 100.0d;
            }
            return value;
        }
        value = (double) (rk >> 2);
        if ((rk & 1) != 0) {
            value /= 100.0d;
        }
        return value;
    }
}
