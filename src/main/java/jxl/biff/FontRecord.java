package jxl.biff;

import jxl.WorkbookSettings;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.read.biff.Record;

public class FontRecord extends WritableRecordData implements Font {
    public static final Biff7 biff7 = new Biff7();
    private static Logger logger = Logger.getLogger(FontRecord.class);
    private int boldWeight;
    private byte characterSet;
    private int colourIndex;
    private byte fontFamily;
    private int fontIndex;
    private boolean initialized;
    private boolean italic;
    private String name;
    private int pointHeight;
    private int scriptStyle;
    private boolean struckout;
    private int underlineStyle;

    private static class Biff7 {
        private Biff7() {
        }
    }

    protected FontRecord(String fn, int ps, int bold, boolean it, int us, int ci, int ss) {
        super(Type.FONT);
        this.boldWeight = bold;
        this.underlineStyle = us;
        this.name = fn;
        this.pointHeight = ps;
        this.italic = it;
        this.scriptStyle = ss;
        this.colourIndex = ci;
        this.initialized = false;
        this.struckout = false;
    }

    public FontRecord(Record t, WorkbookSettings ws) {
        super(t);
        byte[] data = getRecord().getData();
        this.pointHeight = IntegerHelper.getInt(data[0], data[1]) / 20;
        this.colourIndex = IntegerHelper.getInt(data[4], data[5]);
        this.boldWeight = IntegerHelper.getInt(data[6], data[7]);
        this.scriptStyle = IntegerHelper.getInt(data[8], data[9]);
        this.underlineStyle = data[10];
        this.fontFamily = (byte) data[11];
        this.characterSet = (byte) data[12];
        this.initialized = false;
        if ((data[2] & 2) != 0) {
            this.italic = true;
        }
        if ((data[2] & 8) != 0) {
            this.struckout = true;
        }
        int numChars = data[14];
        if (data[15] == (byte) 0) {
            this.name = StringHelper.getString(data, numChars, 16, ws);
        } else if (data[15] != (byte) 1) {
            this.name = StringHelper.getString(data, numChars, 15, ws);
        } else {
            this.name = StringHelper.getUnicodeString(data, numChars, 16);
        }
    }

    public FontRecord(Record t, WorkbookSettings ws, Biff7 dummy) {
        super(t);
        byte[] data = getRecord().getData();
        this.pointHeight = IntegerHelper.getInt(data[0], data[1]) / 20;
        this.colourIndex = IntegerHelper.getInt(data[4], data[5]);
        this.boldWeight = IntegerHelper.getInt(data[6], data[7]);
        this.scriptStyle = IntegerHelper.getInt(data[8], data[9]);
        this.underlineStyle = data[10];
        this.fontFamily = (byte) data[11];
        this.initialized = false;
        if ((data[2] & 2) != 0) {
            this.italic = true;
        }
        if ((data[2] & 8) != 0) {
            this.struckout = true;
        }
        this.name = StringHelper.getString(data, data[14], 15, ws);
    }

    protected FontRecord(Font f) {
        super(Type.FONT);
        Assert.verify(f != null);
        this.pointHeight = f.getPointSize();
        this.colourIndex = f.getColour().getValue();
        this.boldWeight = f.getBoldWeight();
        this.scriptStyle = f.getScriptStyle().getValue();
        this.underlineStyle = f.getUnderlineStyle().getValue();
        this.italic = f.isItalic();
        this.name = f.getName();
        this.struckout = f.isStruckout();
        this.initialized = false;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.name.length() * 2) + 16)];
        IntegerHelper.getTwoBytes(this.pointHeight * 20, data, 0);
        if (this.italic) {
            data[2] = (byte) ((byte) (data[2] | 2));
        }
        if (this.struckout) {
            data[2] = (byte) ((byte) (data[2] | 8));
        }
        IntegerHelper.getTwoBytes(this.colourIndex, data, 4);
        IntegerHelper.getTwoBytes(this.boldWeight, data, 6);
        IntegerHelper.getTwoBytes(this.scriptStyle, data, 8);
        data[10] = (byte) ((byte) this.underlineStyle);
        data[11] = (byte) this.fontFamily;
        data[12] = (byte) this.characterSet;
        data[13] = (byte) 0;
        data[14] = (byte) ((byte) this.name.length());
        data[15] = (byte) 1;
        StringHelper.getUnicodeBytes(this.name, data, 16);
        return data;
    }

    public final boolean isInitialized() {
        return this.initialized;
    }

    public final void initialize(int pos) {
        this.fontIndex = pos;
        this.initialized = true;
    }

    public final void uninitialize() {
        this.initialized = false;
    }

    public final int getFontIndex() {
        return this.fontIndex;
    }

    public int getPointSize() {
        return this.pointHeight;
    }

    public int getBoldWeight() {
        return this.boldWeight;
    }

    public boolean isItalic() {
        return this.italic;
    }

    public UnderlineStyle getUnderlineStyle() {
        return UnderlineStyle.getStyle(this.underlineStyle);
    }

    public Colour getColour() {
        return Colour.getInternalColour(this.colourIndex);
    }

    public ScriptStyle getScriptStyle() {
        return ScriptStyle.getStyle(this.scriptStyle);
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FontRecord)) {
            return false;
        }
        FontRecord font = (FontRecord) o;
        return this.pointHeight == font.pointHeight && this.colourIndex == font.colourIndex && this.boldWeight == font.boldWeight && this.scriptStyle == font.scriptStyle && this.underlineStyle == font.underlineStyle && this.italic == font.italic && this.struckout == font.struckout && this.fontFamily == font.fontFamily && this.characterSet == font.characterSet && this.name.equals(font.name);
    }

    public boolean isStruckout() {
        return this.struckout;
    }
}
