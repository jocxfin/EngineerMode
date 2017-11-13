package jxl.format;

public final class ScriptStyle {
    public static final ScriptStyle NORMAL_SCRIPT = new ScriptStyle(0, "normal");
    public static final ScriptStyle SUBSCRIPT = new ScriptStyle(2, "sub");
    public static final ScriptStyle SUPERSCRIPT = new ScriptStyle(1, "super");
    private static ScriptStyle[] styles = new ScriptStyle[0];
    private String string;
    private int value;

    protected ScriptStyle(int val, String s) {
        this.value = val;
        this.string = s;
        ScriptStyle[] oldstyles = styles;
        styles = new ScriptStyle[(oldstyles.length + 1)];
        System.arraycopy(oldstyles, 0, styles, 0, oldstyles.length);
        styles[oldstyles.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public static ScriptStyle getStyle(int val) {
        for (int i = 0; i < styles.length; i++) {
            if (styles[i].getValue() == val) {
                return styles[i];
            }
        }
        return NORMAL_SCRIPT;
    }
}
