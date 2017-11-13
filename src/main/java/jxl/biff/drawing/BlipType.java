package jxl.biff.drawing;

import com.android.engineeringmode.functions.Light;

final class BlipType {
    public static final BlipType DIB = new BlipType(7, "DIB");
    public static final BlipType EMF = new BlipType(2, "EMF");
    public static final BlipType ERROR = new BlipType(0, "Error");
    public static final BlipType FIRST_CLIENT = new BlipType(32, "FIRST");
    public static final BlipType JPEG = new BlipType(5, "JPEG");
    public static final BlipType LAST_CLIENT = new BlipType(Light.MAIN_KEY_MAX, "LAST");
    public static final BlipType PICT = new BlipType(4, "PICT");
    public static final BlipType PNG = new BlipType(6, "PNG");
    public static final BlipType UNKNOWN = new BlipType(1, "Unknown");
    public static final BlipType WMF = new BlipType(3, "WMF");
    private static BlipType[] types = new BlipType[0];
    private String desc;
    private int value;

    private BlipType(int val, String d) {
        this.value = val;
        this.desc = d;
        BlipType[] newtypes = new BlipType[(types.length + 1)];
        System.arraycopy(types, 0, newtypes, 0, types.length);
        newtypes[types.length] = this;
        types = newtypes;
    }

    public int getValue() {
        return this.value;
    }

    public static BlipType getType(int val) {
        BlipType type = UNKNOWN;
        for (int i = 0; i < types.length; i++) {
            if (types[i].value == val) {
                return types[i];
            }
        }
        return type;
    }
}
