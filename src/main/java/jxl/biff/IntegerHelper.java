package jxl.biff;

import com.android.engineeringmode.functions.Light;

public final class IntegerHelper {
    private IntegerHelper() {
    }

    public static int getInt(byte b1, byte b2) {
        return ((b2 & Light.MAIN_KEY_MAX) << 8) | (b1 & Light.MAIN_KEY_MAX);
    }

    public static short getShort(byte b1, byte b2) {
        return (short) ((((short) (b2 & Light.MAIN_KEY_MAX)) << 8) | ((short) (b1 & Light.MAIN_KEY_MAX)));
    }

    public static int getInt(byte b1, byte b2, byte b3, byte b4) {
        return (getInt(b3, b4) << 16) | getInt(b1, b2);
    }

    public static byte[] getFourBytes(int i) {
        byte[] bytes = new byte[4];
        int i2 = (-65536 & i) >> 16;
        getTwoBytes(i & 65535, bytes, 0);
        getTwoBytes(i2, bytes, 2);
        return bytes;
    }

    public static void getTwoBytes(int i, byte[] target, int pos) {
        target[pos] = (byte) ((byte) (i & Light.MAIN_KEY_MAX));
        target[pos + 1] = (byte) ((byte) ((65280 & i) >> 8));
    }

    public static void getFourBytes(int i, byte[] target, int pos) {
        byte[] bytes = getFourBytes(i);
        target[pos] = (byte) bytes[0];
        target[pos + 1] = (byte) bytes[1];
        target[pos + 2] = (byte) bytes[2];
        target[pos + 3] = (byte) bytes[3];
    }
}
