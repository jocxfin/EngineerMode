package jxl.read.biff;

import com.android.engineeringmode.functions.Light;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

public class RowRecord extends RecordData {
    private static Logger logger = Logger.getLogger(RowRecord.class);
    private boolean collapsed;
    private boolean defaultFormat;
    private boolean groupStart;
    private boolean matchesDefFontHeight;
    private int outlineLevel;
    private int rowHeight;
    private int rowNumber;
    private int xfIndex;

    RowRecord(Record t) {
        boolean z;
        boolean z2 = false;
        super(t);
        byte[] data = getRecord().getData();
        this.rowNumber = IntegerHelper.getInt(data[0], data[1]);
        this.rowHeight = IntegerHelper.getInt(data[6], data[7]);
        int options = IntegerHelper.getInt(data[12], data[13], data[14], data[15]);
        this.outlineLevel = options & 7;
        this.groupStart = (options & 16) != 0;
        if ((options & 32) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.collapsed = z;
        if ((options & 64) != 0) {
            z = false;
        } else {
            z = true;
        }
        this.matchesDefFontHeight = z;
        if ((options & 128) != 0) {
            z2 = true;
        }
        this.defaultFormat = z2;
        this.xfIndex = (268369920 & options) >> 16;
    }

    boolean isDefaultHeight() {
        return this.rowHeight == Light.MAIN_KEY_MAX;
    }

    public boolean matchesDefaultFontHeight() {
        return this.matchesDefFontHeight;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public int getOutlineLevel() {
        return this.outlineLevel;
    }

    public boolean getGroupStart() {
        return this.groupStart;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    public int getXFIndex() {
        return this.xfIndex;
    }

    public boolean hasDefaultFormat() {
        return this.defaultFormat;
    }
}
