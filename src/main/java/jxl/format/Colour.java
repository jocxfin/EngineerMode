package jxl.format;

import com.android.engineeringmode.functions.Light;

public class Colour {
    public static final Colour AQUA = new Colour(49, "aqua", 51, 204, 204);
    public static final Colour AUTOMATIC = new Colour(64, "automatic", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour BLACK = new Colour(32767, "black", 0, 0, 0);
    public static final Colour BLUE = new Colour(12, "blue", 0, 0, Light.MAIN_KEY_MAX);
    public static final Colour BLUE2 = new Colour(39, "blue", 0, 0, Light.MAIN_KEY_MAX);
    public static final Colour BLUE_GREY = new Colour(54, "blue grey", 102, 102, 204);
    public static final Colour BRIGHT_GREEN = new Colour(11, "bright green", 0, Light.MAIN_KEY_MAX, 0);
    public static final Colour BROWN = new Colour(60, "brown", 153, 51, 0);
    public static final Colour CORAL = new Colour(29, "coral", Light.MAIN_KEY_MAX, 128, 128);
    public static final Colour DARK_BLUE = new Colour(18, "dark blue", 0, 0, 128);
    public static final Colour DARK_BLUE2 = new Colour(32, "dark blue", 0, 0, 128);
    public static final Colour DARK_GREEN = new Colour(58, "dark green", 0, 51, 0);
    public static final Colour DARK_PURPLE = new Colour(28, "dark purple", 102, 0, 102);
    public static final Colour DARK_RED = new Colour(16, "dark red", 128, 0, 0);
    public static final Colour DARK_RED2 = new Colour(37, "dark red", 128, 0, 0);
    public static final Colour DARK_TEAL = new Colour(56, "dark teal", 0, 51, 102);
    public static final Colour DARK_YELLOW = new Colour(19, "dark yellow", 128, 128, 0);
    public static final Colour DEFAULT_BACKGROUND = new Colour(192, "default background", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour DEFAULT_BACKGROUND1 = new Colour(0, "default background", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour GOLD = new Colour(51, "gold", Light.MAIN_KEY_MAX, 204, 0);
    public static final Colour GRAY_25 = GREY_25_PERCENT;
    public static final Colour GRAY_50 = GREY_50_PERCENT;
    public static final Colour GRAY_80 = GREY_80_PERCENT;
    public static final Colour GREEN = new Colour(17, "green", 0, 128, 0);
    public static final Colour GREY_25_PERCENT = new Colour(22, "grey 25%", 192, 192, 192);
    public static final Colour GREY_40_PERCENT = new Colour(55, "grey 40%", 150, 150, 150);
    public static final Colour GREY_50_PERCENT = new Colour(23, "grey 50%", 128, 128, 128);
    public static final Colour GREY_80_PERCENT = new Colour(63, "grey 80%", 51, 51, 51);
    public static final Colour ICE_BLUE = new Colour(31, "ice blue", 204, 204, Light.MAIN_KEY_MAX);
    public static final Colour INDIGO = new Colour(62, "indigo", 51, 51, 153);
    public static final Colour IVORY = new Colour(26, "ivory", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 204);
    public static final Colour LAVENDER = new Colour(46, "lavender", 204, 153, Light.MAIN_KEY_MAX);
    public static final Colour LIGHT_BLUE = new Colour(48, "light blue", 51, 102, Light.MAIN_KEY_MAX);
    public static final Colour LIGHT_GREEN = new Colour(42, "light green", 204, Light.MAIN_KEY_MAX, 204);
    public static final Colour LIGHT_ORANGE = new Colour(52, "light orange", Light.MAIN_KEY_MAX, 153, 0);
    public static final Colour LIGHT_TURQUOISE = new Colour(41, "light turquoise", 204, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour LIGHT_TURQUOISE2 = new Colour(27, "light turquoise", 204, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour LIME = new Colour(50, "lime", 153, 204, 0);
    public static final Colour OCEAN_BLUE = new Colour(30, "ocean blue", 0, 102, 204);
    public static final Colour OLIVE_GREEN = new Colour(59, "olive green", 51, 51, 0);
    public static final Colour ORANGE = new Colour(53, "orange", Light.MAIN_KEY_MAX, 102, 0);
    public static final Colour PALETTE_BLACK = new Colour(8, "black", 1, 0, 0);
    public static final Colour PALE_BLUE = new Colour(44, "pale blue", 153, 204, Light.MAIN_KEY_MAX);
    public static final Colour PERIWINKLE = new Colour(24, "periwinkle%", 153, 153, Light.MAIN_KEY_MAX);
    public static final Colour PINK = new Colour(14, "pink", Light.MAIN_KEY_MAX, 0, Light.MAIN_KEY_MAX);
    public static final Colour PINK2 = new Colour(33, "pink", Light.MAIN_KEY_MAX, 0, Light.MAIN_KEY_MAX);
    public static final Colour PLUM = new Colour(61, "plum", 153, 51, 102);
    public static final Colour PLUM2 = new Colour(25, "plum", 153, 51, 102);
    public static final Colour RED = new Colour(10, "red", Light.MAIN_KEY_MAX, 0, 0);
    public static final Colour ROSE = new Colour(45, "rose", Light.MAIN_KEY_MAX, 153, 204);
    public static final Colour SEA_GREEN = new Colour(57, "sea green", 51, 153, 102);
    public static final Colour SKY_BLUE = new Colour(40, "sky blue", 0, 204, Light.MAIN_KEY_MAX);
    public static final Colour TAN = new Colour(47, "tan", Light.MAIN_KEY_MAX, 204, 153);
    public static final Colour TEAL = new Colour(21, "teal", 0, 128, 128);
    public static final Colour TEAL2 = new Colour(38, "teal", 0, 128, 128);
    public static final Colour TURQOISE2 = new Colour(35, "turqoise", 0, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour TURQUOISE = new Colour(15, "turquoise", 0, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour UNKNOWN = new Colour(32750, "unknown", 0, 0, 0);
    public static final Colour VERY_LIGHT_YELLOW = new Colour(43, "very light yellow", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 153);
    public static final Colour VIOLET = new Colour(20, "violet", 128, 128, 0);
    public static final Colour VIOLET2 = new Colour(36, "violet", 128, 0, 128);
    public static final Colour WHITE = new Colour(9, "white", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
    public static final Colour YELLOW = new Colour(13, "yellow", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 0);
    public static final Colour YELLOW2 = new Colour(34, "yellow", Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 0);
    private static Colour[] colours = new Colour[0];
    private RGB rgb;
    private String string;
    private int value;

    protected Colour(int val, String s, int r, int g, int b) {
        this.value = val;
        this.string = s;
        this.rgb = new RGB(r, g, b);
        Colour[] oldcols = colours;
        colours = new Colour[(oldcols.length + 1)];
        System.arraycopy(oldcols, 0, colours, 0, oldcols.length);
        colours[oldcols.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public RGB getDefaultRGB() {
        return this.rgb;
    }

    public static Colour getInternalColour(int val) {
        for (int i = 0; i < colours.length; i++) {
            if (colours[i].getValue() == val) {
                return colours[i];
            }
        }
        return UNKNOWN;
    }

    public static Colour[] getAllColours() {
        return colours;
    }
}
