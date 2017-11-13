package jxl.format;

public final class UnderlineStyle {
    public static final UnderlineStyle DOUBLE = new UnderlineStyle(2, "double");
    public static final UnderlineStyle DOUBLE_ACCOUNTING = new UnderlineStyle(34, "double accounting");
    public static final UnderlineStyle NO_UNDERLINE = new UnderlineStyle(0, "none");
    public static final UnderlineStyle SINGLE = new UnderlineStyle(1, "single");
    public static final UnderlineStyle SINGLE_ACCOUNTING = new UnderlineStyle(33, "single accounting");
    private static UnderlineStyle[] styles = new UnderlineStyle[0];
    private String string;
    private int value;

    protected UnderlineStyle(int val, String s) {
        this.value = val;
        this.string = s;
        UnderlineStyle[] oldstyles = styles;
        styles = new UnderlineStyle[(oldstyles.length + 1)];
        System.arraycopy(oldstyles, 0, styles, 0, oldstyles.length);
        styles[oldstyles.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public static UnderlineStyle getStyle(int val) {
        for (int i = 0; i < styles.length; i++) {
            if (styles[i].getValue() == val) {
                return styles[i];
            }
        }
        return NO_UNDERLINE;
    }
}
