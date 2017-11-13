package jxl.write;

import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.write.biff.WritableFontRecord;

public class WritableFont extends WritableFontRecord {
    public static final FontName ARIAL = new FontName("Arial");
    public static final BoldStyle BOLD = new BoldStyle(700);
    public static final FontName COURIER = new FontName("Courier New");
    public static final BoldStyle NO_BOLD = new BoldStyle(400);
    public static final FontName TAHOMA = new FontName("Tahoma");
    public static final FontName TIMES = new FontName("Times New Roman");

    static class BoldStyle {
        public int value;

        BoldStyle(int val) {
            this.value = val;
        }
    }

    public static class FontName {
        String name;

        FontName(String s) {
            this.name = s;
        }
    }

    public WritableFont(FontName fn) {
        this(fn, 10, NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
    }

    public WritableFont(Font f) {
        super(f);
    }

    public WritableFont(FontName fn, int ps, BoldStyle bs, boolean it, UnderlineStyle us, Colour c) {
        this(fn, ps, bs, it, us, c, ScriptStyle.NORMAL_SCRIPT);
    }

    public WritableFont(FontName fn, int ps, BoldStyle bs, boolean it, UnderlineStyle us, Colour c, ScriptStyle ss) {
        super(fn.name, ps, bs.value, it, us.getValue(), c.getValue(), ss.getValue());
    }

    public boolean isStruckout() {
        return super.isStruckout();
    }
}
