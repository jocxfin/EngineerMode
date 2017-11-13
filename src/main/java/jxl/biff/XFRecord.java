package jxl.biff;

import com.android.engineeringmode.functions.Light;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import jxl.WorkbookSettings;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.Format;
import jxl.format.Orientation;
import jxl.format.Pattern;
import jxl.format.VerticalAlignment;
import jxl.read.biff.Record;

public class XFRecord extends WritableRecordData implements CellFormat {
    public static final BiffType biff7 = new BiffType();
    public static final BiffType biff8 = new BiffType();
    protected static final XFType cell = new XFType();
    private static final int[] dateFormats = new int[]{14, 15, 16, 17, 18, 19, 20, 21, 22, 45, 46, 47};
    private static final DateFormat[] javaDateFormats = new DateFormat[]{SimpleDateFormat.getDateInstance(3), SimpleDateFormat.getDateInstance(2), new SimpleDateFormat("d-MMM"), new SimpleDateFormat("MMM-yy"), new SimpleDateFormat("h:mm a"), new SimpleDateFormat("h:mm:ss a"), new SimpleDateFormat("H:mm"), new SimpleDateFormat("H:mm:ss"), new SimpleDateFormat("M/d/yy H:mm"), new SimpleDateFormat("mm:ss"), new SimpleDateFormat("H:mm:ss"), new SimpleDateFormat("mm:ss.S")};
    private static NumberFormat[] javaNumberFormats = new NumberFormat[]{new DecimalFormat("0"), new DecimalFormat("0.00"), new DecimalFormat("#,##0"), new DecimalFormat("#,##0.00"), new DecimalFormat("$#,##0;($#,##0)"), new DecimalFormat("$#,##0;($#,##0)"), new DecimalFormat("$#,##0.00;($#,##0.00)"), new DecimalFormat("$#,##0.00;($#,##0.00)"), new DecimalFormat("0%"), new DecimalFormat("0.00%"), new DecimalFormat("0.00E00"), new DecimalFormat("#,##0;(#,##0)"), new DecimalFormat("#,##0;(#,##0)"), new DecimalFormat("#,##0.00;(#,##0.00)"), new DecimalFormat("#,##0.00;(#,##0.00)"), new DecimalFormat("#,##0;(#,##0)"), new DecimalFormat("$#,##0;($#,##0)"), new DecimalFormat("#,##0.00;(#,##0.00)"), new DecimalFormat("$#,##0.00;($#,##0.00)"), new DecimalFormat("##0.0E0")};
    private static Logger logger = Logger.getLogger(XFRecord.class);
    private static int[] numberFormats = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 37, 38, 39, 40, 41, 42, 43, 44, 48};
    protected static final XFType style = new XFType();
    private Alignment align;
    private Colour backgroundColour;
    private BiffType biffType;
    private BorderLineStyle bottomBorder;
    private Colour bottomBorderColour;
    private boolean copied;
    private boolean date;
    private DateFormat dateFormat;
    private Format excelFormat;
    private FontRecord font;
    private int fontIndex;
    private DisplayFormat format;
    public int formatIndex;
    private boolean formatInfoInitialized;
    private FormattingRecords formattingRecords;
    private boolean hidden;
    private int indentation;
    private boolean initialized;
    private BorderLineStyle leftBorder;
    private Colour leftBorderColour;
    private boolean locked;
    private boolean number;
    private NumberFormat numberFormat;
    private int options;
    private Orientation orientation;
    private int parentFormat;
    private Pattern pattern;
    private boolean read;
    private BorderLineStyle rightBorder;
    private Colour rightBorderColour;
    private boolean shrinkToFit;
    private BorderLineStyle topBorder;
    private Colour topBorderColour;
    private byte usedAttributes;
    private VerticalAlignment valign;
    private boolean wrap;
    private XFType xfFormatType;
    private int xfIndex;

    private static class BiffType {
        private BiffType() {
        }
    }

    private static class XFType {
        private XFType() {
        }
    }

    public XFRecord(Record t, WorkbookSettings ws, BiffType bt) {
        int i;
        boolean z;
        super(t);
        this.biffType = bt;
        byte[] data = getRecord().getData();
        this.fontIndex = IntegerHelper.getInt(data[0], data[1]);
        this.formatIndex = IntegerHelper.getInt(data[2], data[3]);
        this.date = false;
        this.number = false;
        for (i = 0; i < dateFormats.length && !this.date; i++) {
            if (this.formatIndex == dateFormats[i]) {
                this.date = true;
                this.dateFormat = javaDateFormats[i];
            }
        }
        for (i = 0; i < numberFormats.length && !this.number; i++) {
            if (this.formatIndex == numberFormats[i]) {
                this.number = true;
                DecimalFormat df = (DecimalFormat) javaNumberFormats[i].clone();
                df.setDecimalFormatSymbols(new DecimalFormatSymbols(ws.getLocale()));
                this.numberFormat = df;
            }
        }
        int cellAttributes = IntegerHelper.getInt(data[4], data[5]);
        this.parentFormat = (65520 & cellAttributes) >> 4;
        this.xfFormatType = (cellAttributes & 4) != 0 ? style : cell;
        if ((cellAttributes & 1) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.locked = z;
        if ((cellAttributes & 2) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.hidden = z;
        if (this.xfFormatType == cell && (this.parentFormat & 4095) == 4095) {
            this.parentFormat = 0;
            logger.warn("Invalid parent format found - ignoring");
        }
        this.initialized = false;
        this.read = true;
        this.formatInfoInitialized = false;
        this.copied = false;
    }

    public XFRecord(FontRecord fnt, DisplayFormat form) {
        boolean z;
        boolean z2 = false;
        super(Type.XF);
        this.initialized = false;
        this.locked = true;
        this.hidden = false;
        this.align = Alignment.GENERAL;
        this.valign = VerticalAlignment.BOTTOM;
        this.orientation = Orientation.HORIZONTAL;
        this.wrap = false;
        this.leftBorder = BorderLineStyle.NONE;
        this.rightBorder = BorderLineStyle.NONE;
        this.topBorder = BorderLineStyle.NONE;
        this.bottomBorder = BorderLineStyle.NONE;
        this.leftBorderColour = Colour.AUTOMATIC;
        this.rightBorderColour = Colour.AUTOMATIC;
        this.topBorderColour = Colour.AUTOMATIC;
        this.bottomBorderColour = Colour.AUTOMATIC;
        this.pattern = Pattern.NONE;
        this.backgroundColour = Colour.DEFAULT_BACKGROUND;
        this.indentation = 0;
        this.shrinkToFit = false;
        this.usedAttributes = (byte) 124;
        this.parentFormat = 0;
        this.xfFormatType = null;
        this.font = fnt;
        this.format = form;
        this.biffType = biff8;
        this.read = false;
        this.copied = false;
        this.formatInfoInitialized = true;
        if (this.font == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        if (this.format != null) {
            z2 = true;
        }
        Assert.verify(z2);
    }

    protected XFRecord(XFRecord fmt) {
        super(Type.XF);
        this.initialized = false;
        this.locked = fmt.locked;
        this.hidden = fmt.hidden;
        this.align = fmt.align;
        this.valign = fmt.valign;
        this.orientation = fmt.orientation;
        this.wrap = fmt.wrap;
        this.leftBorder = fmt.leftBorder;
        this.rightBorder = fmt.rightBorder;
        this.topBorder = fmt.topBorder;
        this.bottomBorder = fmt.bottomBorder;
        this.leftBorderColour = fmt.leftBorderColour;
        this.rightBorderColour = fmt.rightBorderColour;
        this.topBorderColour = fmt.topBorderColour;
        this.bottomBorderColour = fmt.bottomBorderColour;
        this.pattern = fmt.pattern;
        this.xfFormatType = fmt.xfFormatType;
        this.indentation = fmt.indentation;
        this.shrinkToFit = fmt.shrinkToFit;
        this.parentFormat = fmt.parentFormat;
        this.backgroundColour = fmt.backgroundColour;
        this.font = fmt.font;
        this.format = fmt.format;
        this.fontIndex = fmt.fontIndex;
        this.formatIndex = fmt.formatIndex;
        this.formatInfoInitialized = fmt.formatInfoInitialized;
        this.biffType = biff8;
        this.read = false;
        this.copied = true;
    }

    public DateFormat getDateFormat() {
        return this.dateFormat;
    }

    public NumberFormat getNumberFormat() {
        return this.numberFormat;
    }

    public int getFormatRecord() {
        return this.formatIndex;
    }

    public boolean isDate() {
        return this.date;
    }

    public boolean isNumber() {
        return this.number;
    }

    public byte[] getData() {
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        byte[] data = new byte[20];
        IntegerHelper.getTwoBytes(this.fontIndex, data, 0);
        IntegerHelper.getTwoBytes(this.formatIndex, data, 2);
        int cellAttributes = 0;
        if (getLocked()) {
            cellAttributes = 1;
        }
        if (getHidden()) {
            cellAttributes |= 2;
        }
        if (this.xfFormatType == style) {
            cellAttributes |= 4;
            this.parentFormat = 65535;
        }
        IntegerHelper.getTwoBytes(cellAttributes | (this.parentFormat << 4), data, 4);
        int alignMask = this.align.getValue();
        if (this.wrap) {
            alignMask |= 8;
        }
        IntegerHelper.getTwoBytes((alignMask | (this.valign.getValue() << 4)) | (this.orientation.getValue() << 8), data, 6);
        data[9] = (byte) 16;
        int borderMask = ((this.leftBorder.getValue() | (this.rightBorder.getValue() << 4)) | (this.topBorder.getValue() << 8)) | (this.bottomBorder.getValue() << 12);
        IntegerHelper.getTwoBytes(borderMask, data, 10);
        if (borderMask != 0) {
            int topColourMask = (((byte) this.topBorderColour.getValue()) & 127) | ((((byte) this.bottomBorderColour.getValue()) & 127) << 7);
            IntegerHelper.getTwoBytes((((byte) this.leftBorderColour.getValue()) & 127) | ((((byte) this.rightBorderColour.getValue()) & 127) << 7), data, 12);
            IntegerHelper.getTwoBytes(topColourMask, data, 14);
        }
        IntegerHelper.getTwoBytes(this.pattern.getValue() << 10, data, 16);
        IntegerHelper.getTwoBytes(this.backgroundColour.getValue() | 8192, data, 18);
        this.options |= this.indentation & 15;
        if (this.shrinkToFit) {
            this.options |= 16;
        } else {
            this.options &= 239;
        }
        data[8] = (byte) ((byte) this.options);
        if (this.biffType == biff8) {
            data[9] = (byte) this.usedAttributes;
        }
        return data;
    }

    protected final boolean getLocked() {
        return this.locked;
    }

    protected final boolean getHidden() {
        return this.hidden;
    }

    protected final void setXFLocked(boolean l) {
        this.locked = l;
        this.usedAttributes = (byte) ((byte) (this.usedAttributes | 128));
    }

    protected final void setXFCellOptions(int opt) {
        this.options |= opt;
    }

    protected void setXFBorder(Border b, BorderLineStyle ls, Colour c) {
        boolean z = false;
        if (!this.initialized) {
            z = true;
        }
        Assert.verify(z);
        if (c == Colour.BLACK || c == Colour.UNKNOWN) {
            c = Colour.PALETTE_BLACK;
        }
        if (b == Border.LEFT) {
            this.leftBorder = ls;
            this.leftBorderColour = c;
        } else if (b == Border.RIGHT) {
            this.rightBorder = ls;
            this.rightBorderColour = c;
        } else if (b == Border.TOP) {
            this.topBorder = ls;
            this.topBorderColour = c;
        } else if (b == Border.BOTTOM) {
            this.bottomBorder = ls;
            this.bottomBorderColour = c;
        }
        this.usedAttributes = (byte) ((byte) (this.usedAttributes | 32));
    }

    public BorderLineStyle getBorderLine(Border border) {
        if (border == Border.NONE || border == Border.ALL) {
            return BorderLineStyle.NONE;
        }
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        if (border == Border.LEFT) {
            return this.leftBorder;
        }
        if (border == Border.RIGHT) {
            return this.rightBorder;
        }
        if (border == Border.TOP) {
            return this.topBorder;
        }
        if (border != Border.BOTTOM) {
            return BorderLineStyle.NONE;
        }
        return this.bottomBorder;
    }

    public Colour getBorderColour(Border border) {
        if (border == Border.NONE || border == Border.ALL) {
            return Colour.PALETTE_BLACK;
        }
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        if (border == Border.LEFT) {
            return this.leftBorderColour;
        }
        if (border == Border.RIGHT) {
            return this.rightBorderColour;
        }
        if (border == Border.TOP) {
            return this.topBorderColour;
        }
        if (border != Border.BOTTOM) {
            return Colour.BLACK;
        }
        return this.bottomBorderColour;
    }

    public final boolean hasBorders() {
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        if (this.leftBorder == BorderLineStyle.NONE && this.rightBorder == BorderLineStyle.NONE && this.topBorder == BorderLineStyle.NONE && this.bottomBorder == BorderLineStyle.NONE) {
            return false;
        }
        return true;
    }

    public final void initialize(int pos, FormattingRecords fr, Fonts fonts) throws NumFormatRecordsException {
        this.xfIndex = pos;
        this.formattingRecords = fr;
        if (this.read || this.copied) {
            this.initialized = true;
            return;
        }
        if (!this.font.isInitialized()) {
            fonts.addFont(this.font);
        }
        if (!this.format.isInitialized()) {
            fr.addFormat(this.format);
        }
        this.fontIndex = this.font.getFontIndex();
        this.formatIndex = this.format.getFormatIndex();
        this.initialized = true;
    }

    public final void uninitialize() {
        if (this.initialized) {
            logger.warn("A default format has been initialized");
        }
        this.initialized = false;
    }

    public final int getXFIndex() {
        return this.xfIndex;
    }

    public final boolean isInitialized() {
        return this.initialized;
    }

    public final boolean isRead() {
        return this.read;
    }

    public Font getFont() {
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        return this.font;
    }

    private void initializeFormatInformation() {
        boolean z;
        boolean z2 = false;
        if (this.formatIndex < BuiltInFormat.builtIns.length && BuiltInFormat.builtIns[this.formatIndex] != null) {
            this.excelFormat = BuiltInFormat.builtIns[this.formatIndex];
        } else {
            this.excelFormat = this.formattingRecords.getFormatRecord(this.formatIndex);
        }
        this.font = this.formattingRecords.getFonts().getFont(this.fontIndex);
        byte[] data = getRecord().getData();
        int cellAttributes = IntegerHelper.getInt(data[4], data[5]);
        this.parentFormat = (65520 & cellAttributes) >> 4;
        this.xfFormatType = (cellAttributes & 4) != 0 ? style : cell;
        if ((cellAttributes & 1) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.locked = z;
        if ((cellAttributes & 2) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.hidden = z;
        if (this.xfFormatType == cell && (this.parentFormat & 4095) == 4095) {
            this.parentFormat = 0;
            logger.warn("Invalid parent format found - ignoring");
        }
        int alignMask = IntegerHelper.getInt(data[6], data[7]);
        if ((alignMask & 8) != 0) {
            this.wrap = true;
        }
        this.align = Alignment.getAlignment(alignMask & 7);
        this.valign = VerticalAlignment.getAlignment((alignMask >> 4) & 7);
        this.orientation = Orientation.getOrientation((alignMask >> 8) & Light.MAIN_KEY_MAX);
        int attr = IntegerHelper.getInt(data[8], data[9]);
        this.indentation = attr & 15;
        if ((attr & 16) != 0) {
            z2 = true;
        }
        this.shrinkToFit = z2;
        if (this.biffType == biff8) {
            this.usedAttributes = (byte) data[9];
        }
        int borderMask = IntegerHelper.getInt(data[10], data[11]);
        this.leftBorder = BorderLineStyle.getStyle(borderMask & 7);
        this.rightBorder = BorderLineStyle.getStyle((borderMask >> 4) & 7);
        this.topBorder = BorderLineStyle.getStyle((borderMask >> 8) & 7);
        this.bottomBorder = BorderLineStyle.getStyle((borderMask >> 12) & 7);
        int borderColourMask = IntegerHelper.getInt(data[12], data[13]);
        this.leftBorderColour = Colour.getInternalColour(borderColourMask & 127);
        this.rightBorderColour = Colour.getInternalColour((borderColourMask & 16256) >> 7);
        borderColourMask = IntegerHelper.getInt(data[14], data[15]);
        this.topBorderColour = Colour.getInternalColour(borderColourMask & 127);
        this.bottomBorderColour = Colour.getInternalColour((borderColourMask & 16256) >> 7);
        if (this.biffType != biff8) {
            this.pattern = Pattern.NONE;
            this.backgroundColour = Colour.DEFAULT_BACKGROUND;
        } else {
            this.pattern = Pattern.getPattern((IntegerHelper.getInt(data[16], data[17]) & 64512) >> 10);
            this.backgroundColour = Colour.getInternalColour(IntegerHelper.getInt(data[18], data[19]) & 63);
            if (this.backgroundColour == Colour.UNKNOWN || this.backgroundColour == Colour.DEFAULT_BACKGROUND1) {
                this.backgroundColour = Colour.DEFAULT_BACKGROUND;
            }
        }
        this.formatInfoInitialized = true;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        if (!this.formatInfoInitialized) {
            initializeFormatInformation();
        }
        if (this.hidden) {
            i = 1;
        } else {
            i = 0;
        }
        int i3 = (i + 629) * 37;
        if (this.locked) {
            i = 1;
        } else {
            i = 0;
        }
        i3 = (i3 + i) * 37;
        if (this.wrap) {
            i = 1;
        } else {
            i = 0;
        }
        i = (i3 + i) * 37;
        if (this.shrinkToFit) {
            i2 = 1;
        }
        int hashValue = i + i2;
        if (this.xfFormatType == cell) {
            hashValue = (hashValue * 37) + 1;
        } else if (this.xfFormatType == style) {
            hashValue = (hashValue * 37) + 2;
        }
        return (((((((((((((((((((((((((((((((hashValue * 37) + (this.align.getValue() + 1)) * 37) + (this.valign.getValue() + 1)) * 37) + this.orientation.getValue()) ^ this.leftBorder.getDescription().hashCode()) ^ this.rightBorder.getDescription().hashCode()) ^ this.topBorder.getDescription().hashCode()) ^ this.bottomBorder.getDescription().hashCode()) * 37) + this.leftBorderColour.getValue()) * 37) + this.rightBorderColour.getValue()) * 37) + this.topBorderColour.getValue()) * 37) + this.bottomBorderColour.getValue()) * 37) + this.backgroundColour.getValue()) * 37) + (this.pattern.getValue() + 1)) * 37) + this.usedAttributes) * 37) + this.parentFormat) * 37) + this.fontIndex) * 37) + this.formatIndex) * 37) + this.indentation;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r4 = 1;
        r3 = 0;
        if (r6 == r5) goto L_0x001a;
    L_0x0004:
        r1 = r6 instanceof jxl.biff.XFRecord;
        if (r1 == 0) goto L_0x001b;
    L_0x0008:
        r0 = r6;
        r0 = (jxl.biff.XFRecord) r0;
        r1 = r5.formatInfoInitialized;
        if (r1 == 0) goto L_0x001c;
    L_0x000f:
        r1 = r0.formatInfoInitialized;
        if (r1 == 0) goto L_0x0020;
    L_0x0013:
        r1 = r5.xfFormatType;
        r2 = r0.xfFormatType;
        if (r1 == r2) goto L_0x0024;
    L_0x0019:
        return r3;
    L_0x001a:
        return r4;
    L_0x001b:
        return r3;
    L_0x001c:
        r5.initializeFormatInformation();
        goto L_0x000f;
    L_0x0020:
        r0.initializeFormatInformation();
        goto L_0x0013;
    L_0x0024:
        r1 = r5.parentFormat;
        r2 = r0.parentFormat;
        if (r1 != r2) goto L_0x0019;
    L_0x002a:
        r1 = r5.locked;
        r2 = r0.locked;
        if (r1 != r2) goto L_0x0019;
    L_0x0030:
        r1 = r5.hidden;
        r2 = r0.hidden;
        if (r1 != r2) goto L_0x0019;
    L_0x0036:
        r1 = r5.usedAttributes;
        r2 = r0.usedAttributes;
        if (r1 != r2) goto L_0x0019;
    L_0x003c:
        r1 = r5.align;
        r2 = r0.align;
        if (r1 == r2) goto L_0x0043;
    L_0x0042:
        return r3;
    L_0x0043:
        r1 = r5.valign;
        r2 = r0.valign;
        if (r1 != r2) goto L_0x0042;
    L_0x0049:
        r1 = r5.orientation;
        r2 = r0.orientation;
        if (r1 != r2) goto L_0x0042;
    L_0x004f:
        r1 = r5.wrap;
        r2 = r0.wrap;
        if (r1 != r2) goto L_0x0042;
    L_0x0055:
        r1 = r5.shrinkToFit;
        r2 = r0.shrinkToFit;
        if (r1 != r2) goto L_0x0042;
    L_0x005b:
        r1 = r5.indentation;
        r2 = r0.indentation;
        if (r1 != r2) goto L_0x0042;
    L_0x0061:
        r1 = r5.leftBorder;
        r2 = r0.leftBorder;
        if (r1 == r2) goto L_0x0068;
    L_0x0067:
        return r3;
    L_0x0068:
        r1 = r5.rightBorder;
        r2 = r0.rightBorder;
        if (r1 != r2) goto L_0x0067;
    L_0x006e:
        r1 = r5.topBorder;
        r2 = r0.topBorder;
        if (r1 != r2) goto L_0x0067;
    L_0x0074:
        r1 = r5.bottomBorder;
        r2 = r0.bottomBorder;
        if (r1 != r2) goto L_0x0067;
    L_0x007a:
        r1 = r5.leftBorderColour;
        r2 = r0.leftBorderColour;
        if (r1 == r2) goto L_0x0081;
    L_0x0080:
        return r3;
    L_0x0081:
        r1 = r5.rightBorderColour;
        r2 = r0.rightBorderColour;
        if (r1 != r2) goto L_0x0080;
    L_0x0087:
        r1 = r5.topBorderColour;
        r2 = r0.topBorderColour;
        if (r1 != r2) goto L_0x0080;
    L_0x008d:
        r1 = r5.bottomBorderColour;
        r2 = r0.bottomBorderColour;
        if (r1 != r2) goto L_0x0080;
    L_0x0093:
        r1 = r5.backgroundColour;
        r2 = r0.backgroundColour;
        if (r1 == r2) goto L_0x009a;
    L_0x0099:
        return r3;
    L_0x009a:
        r1 = r5.pattern;
        r2 = r0.pattern;
        if (r1 != r2) goto L_0x0099;
    L_0x00a0:
        r1 = r5.initialized;
        if (r1 != 0) goto L_0x00af;
    L_0x00a4:
        r1 = r5.font;
        r2 = r0.font;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00c1;
    L_0x00ae:
        return r3;
    L_0x00af:
        r1 = r0.initialized;
        if (r1 == 0) goto L_0x00a4;
    L_0x00b3:
        r1 = r5.fontIndex;
        r2 = r0.fontIndex;
        if (r1 == r2) goto L_0x00ba;
    L_0x00b9:
        return r3;
    L_0x00ba:
        r1 = r5.formatIndex;
        r2 = r0.formatIndex;
        if (r1 != r2) goto L_0x00b9;
    L_0x00c0:
        return r4;
    L_0x00c1:
        r1 = r5.format;
        r2 = r0.format;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x00ae;
    L_0x00cb:
        goto L_0x00c0;
        */
        throw new UnsupportedOperationException("Method not decompiled: jxl.biff.XFRecord.equals(java.lang.Object):boolean");
    }

    void setFormatIndex(int newindex) {
        this.formatIndex = newindex;
    }

    public int getFontIndex() {
        return this.fontIndex;
    }

    void setFontIndex(int newindex) {
        this.fontIndex = newindex;
    }

    protected void setXFDetails(XFType t, int pf) {
        this.xfFormatType = t;
        this.parentFormat = pf;
    }

    void rationalize(IndexMapping xfMapping) {
        this.xfIndex = xfMapping.getNewIndex(this.xfIndex);
        if (this.xfFormatType == cell) {
            this.parentFormat = xfMapping.getNewIndex(this.parentFormat);
        }
    }

    public void setFont(FontRecord f) {
        this.font = f;
    }
}
