package jxl.write.biff;

import jxl.biff.FormattingRecords;
import jxl.biff.IndexMapping;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.biff.XFRecord;

class ColumnInfoRecord extends WritableRecordData {
    private boolean collapsed;
    private int column;
    private byte[] data;
    private boolean hidden;
    private int outlineLevel;
    private XFRecord style;
    private int width;
    private int xfIndex;

    public ColumnInfoRecord(jxl.read.biff.ColumnInfoRecord cir, int col, FormattingRecords fr) {
        super(Type.COLINFO);
        this.column = col;
        this.width = cir.getWidth();
        this.xfIndex = cir.getXFIndex();
        this.style = fr.getXFRecord(this.xfIndex);
        this.outlineLevel = cir.getOutlineLevel();
        this.collapsed = cir.getCollapsed();
    }

    public int getColumn() {
        return this.column;
    }

    void setWidth(int w) {
        this.width = w;
    }

    public byte[] getData() {
        this.data = new byte[12];
        IntegerHelper.getTwoBytes(this.column, this.data, 0);
        IntegerHelper.getTwoBytes(this.column, this.data, 2);
        IntegerHelper.getTwoBytes(this.width, this.data, 4);
        IntegerHelper.getTwoBytes(this.xfIndex, this.data, 6);
        int options = (this.outlineLevel << 8) | 6;
        if (this.hidden) {
            options |= 1;
        }
        this.outlineLevel = (options & 1792) / 256;
        if (this.collapsed) {
            options |= 4096;
        }
        IntegerHelper.getTwoBytes(options, this.data, 8);
        return this.data;
    }

    public XFRecord getCellFormat() {
        return this.style;
    }

    void rationalize(IndexMapping xfmapping) {
        this.xfIndex = xfmapping.getNewIndex(this.xfIndex);
    }

    void setHidden(boolean h) {
        this.hidden = h;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ColumnInfoRecord)) {
            return false;
        }
        ColumnInfoRecord cir = (ColumnInfoRecord) o;
        if (this.column != cir.column || this.xfIndex != cir.xfIndex || this.width != cir.width || this.hidden != cir.hidden || this.outlineLevel != cir.outlineLevel || this.collapsed != cir.collapsed) {
            return false;
        }
        if (this.style != null || cir.style == null) {
            if (this.style != null) {
                if (cir.style != null) {
                }
            }
            return this.style.equals(cir.style);
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int i2 = (((((this.column + 10823) * 79) + this.xfIndex) * 79) + this.width) * 79;
        if (this.hidden) {
            i = 1;
        }
        int hashValue = i2 + i;
        if (this.style == null) {
            return hashValue;
        }
        return hashValue ^ this.style.hashCode();
    }
}
