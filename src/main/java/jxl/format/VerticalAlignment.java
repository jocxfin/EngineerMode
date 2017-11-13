package jxl.format;

public class VerticalAlignment {
    public static VerticalAlignment BOTTOM = new VerticalAlignment(2, "bottom");
    public static VerticalAlignment CENTRE = new VerticalAlignment(1, "centre");
    public static VerticalAlignment JUSTIFY = new VerticalAlignment(3, "Justify");
    public static VerticalAlignment TOP = new VerticalAlignment(0, "top");
    private static VerticalAlignment[] alignments = new VerticalAlignment[0];
    private String string;
    private int value;

    protected VerticalAlignment(int val, String s) {
        this.value = val;
        this.string = s;
        VerticalAlignment[] oldaligns = alignments;
        alignments = new VerticalAlignment[(oldaligns.length + 1)];
        System.arraycopy(oldaligns, 0, alignments, 0, oldaligns.length);
        alignments[oldaligns.length] = this;
    }

    public int getValue() {
        return this.value;
    }

    public static VerticalAlignment getAlignment(int val) {
        for (int i = 0; i < alignments.length; i++) {
            if (alignments[i].getValue() == val) {
                return alignments[i];
            }
        }
        return BOTTOM;
    }
}
