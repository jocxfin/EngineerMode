package jxl.format;

public class Pattern {
    public static final Pattern GRAY_25 = new Pattern(4, "Gray 25%");
    public static final Pattern GRAY_50 = new Pattern(2, "Gray 50%");
    public static final Pattern GRAY_75 = new Pattern(3, "Gray 75%");
    public static final Pattern NONE = new Pattern(0, "None");
    public static final Pattern PATTERN1 = new Pattern(5, "Pattern 1");
    public static final Pattern PATTERN10 = new Pattern(14, "Pattern 10");
    public static final Pattern PATTERN11 = new Pattern(15, "Pattern 11");
    public static final Pattern PATTERN12 = new Pattern(16, "Pattern 12");
    public static final Pattern PATTERN13 = new Pattern(17, "Pattern 13");
    public static final Pattern PATTERN14 = new Pattern(18, "Pattern 14");
    public static final Pattern PATTERN2 = new Pattern(6, "Pattern 2");
    public static final Pattern PATTERN3 = new Pattern(7, "Pattern 3");
    public static final Pattern PATTERN4 = new Pattern(8, "Pattern 4");
    public static final Pattern PATTERN5 = new Pattern(9, "Pattern 5");
    public static final Pattern PATTERN6 = new Pattern(10, "Pattern 6");
    public static final Pattern PATTERN7 = new Pattern(11, "Pattern 7");
    public static final Pattern PATTERN8 = new Pattern(12, "Pattern 8");
    public static final Pattern PATTERN9 = new Pattern(13, "Pattern 9");
    public static final Pattern SOLID = new Pattern(1, "Solid");
    private static Pattern[] patterns = new Pattern[0];
    private String string;
    private int value;

    protected Pattern(int val, String s) {
        this.value = val;
        this.string = s;
        Pattern[] oldcols = patterns;
        patterns = new Pattern[(oldcols.length + 1)];
        System.arraycopy(oldcols, 0, patterns, 0, oldcols.length);
        patterns[oldcols.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public static Pattern getPattern(int val) {
        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i].getValue() == val) {
                return patterns[i];
            }
        }
        return NONE;
    }
}
