package jxl.format;

public class Alignment {
    public static Alignment CENTRE = new Alignment(2, "centre");
    public static Alignment FILL = new Alignment(4, "fill");
    public static Alignment GENERAL = new Alignment(0, "general");
    public static Alignment JUSTIFY = new Alignment(5, "justify");
    public static Alignment LEFT = new Alignment(1, "left");
    public static Alignment RIGHT = new Alignment(3, "right");
    private static Alignment[] alignments = new Alignment[0];
    private String string;
    private int value;

    protected Alignment(int val, String s) {
        this.value = val;
        this.string = s;
        Alignment[] oldaligns = alignments;
        alignments = new Alignment[(oldaligns.length + 1)];
        System.arraycopy(oldaligns, 0, alignments, 0, oldaligns.length);
        alignments[oldaligns.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public static Alignment getAlignment(int val) {
        for (int i = 0; i < alignments.length; i++) {
            if (alignments[i].getValue() == val) {
                return alignments[i];
            }
        }
        return GENERAL;
    }
}
