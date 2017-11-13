package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.Type;

public class ColumnInfoRecord extends RecordData {
    private boolean collapsed;
    private byte[] data;
    private int endColumn = IntegerHelper.getInt(this.data[2], this.data[3]);
    private boolean hidden;
    private int outlineLevel;
    private int startColumn = IntegerHelper.getInt(this.data[0], this.data[1]);
    private int width = IntegerHelper.getInt(this.data[4], this.data[5]);
    private int xfIndex = IntegerHelper.getInt(this.data[6], this.data[7]);

    ColumnInfoRecord(Record t) {
        boolean z;
        boolean z2 = false;
        super(Type.COLINFO);
        this.data = t.getData();
        int options = IntegerHelper.getInt(this.data[8], this.data[9]);
        if ((options & 1) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.hidden = z;
        this.outlineLevel = (options & 1792) >> 8;
        if ((options & 4096) != 0) {
            z2 = true;
        }
        this.collapsed = z2;
    }

    public int getStartColumn() {
        return this.startColumn;
    }

    public int getEndColumn() {
        return this.endColumn;
    }

    public int getXFIndex() {
        return this.xfIndex;
    }

    public int getOutlineLevel() {
        return this.outlineLevel;
    }

    public boolean getCollapsed() {
        return this.collapsed;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean getHidden() {
        return this.hidden;
    }
}
