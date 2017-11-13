package jxl.write.biff;

import com.android.engineeringmode.functions.Light;

import jxl.biff.BuiltInName;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;

class NameRecord extends WritableRecordData {
    private static final NameRange EMPTY_RANGE = new NameRange(0, 0, 0, 0, 0);
    private static Logger logger = Logger.getLogger(NameRecord.class);
    private BuiltInName builtInName;
    private byte[] data;
    private int index;
    private boolean modified;
    private String name;
    private NameRange[] ranges;
    private int sheetRef = 0;

    static class NameRange {
        private int columnFirst;
        private int columnLast;
        private int externalSheet;
        private int rowFirst;
        private int rowLast;

        NameRange(jxl.read.biff.NameRecord.NameRange nr) {
            this.columnFirst = nr.getFirstColumn();
            this.rowFirst = nr.getFirstRow();
            this.columnLast = nr.getLastColumn();
            this.rowLast = nr.getLastRow();
            this.externalSheet = nr.getExternalSheet();
        }

        NameRange(int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol) {
            this.columnFirst = theStartCol;
            this.rowFirst = theStartRow;
            this.columnLast = theEndCol;
            this.rowLast = theEndRow;
            this.externalSheet = extSheet;
        }

        byte[] getData() {
            byte[] d = new byte[10];
            IntegerHelper.getTwoBytes(this.externalSheet, d, 0);
            IntegerHelper.getTwoBytes(this.rowFirst, d, 2);
            IntegerHelper.getTwoBytes(this.rowLast, d, 4);
            IntegerHelper.getTwoBytes(this.columnFirst & Light.MAIN_KEY_MAX, d, 6);
            IntegerHelper.getTwoBytes(this.columnLast & Light.MAIN_KEY_MAX, d, 8);
            return d;
        }
    }

    public NameRecord(jxl.read.biff.NameRecord sr, int ind) {
        super(Type.NAME);
        this.data = sr.getData();
        this.name = sr.getName();
        this.sheetRef = sr.getSheetRef();
        this.index = ind;
        this.modified = false;
        jxl.read.biff.NameRecord.NameRange[] r = sr.getRanges();
        this.ranges = new NameRange[r.length];
        for (int i = 0; i < this.ranges.length; i++) {
            this.ranges[i] = new NameRange(r[i]);
        }
    }

    NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, boolean global) {
        super(Type.NAME);
        this.builtInName = theName;
        this.index = theIndex;
        this.sheetRef = !global ? this.index + 1 : 0;
        this.ranges = new NameRange[1];
        this.ranges[0] = new NameRange(extSheet, theStartRow, theEndRow, theStartCol, theEndCol);
    }

    NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, int theStartRow2, int theEndRow2, int theStartCol2, int theEndCol2, boolean global) {
        super(Type.NAME);
        this.builtInName = theName;
        this.index = theIndex;
        this.sheetRef = !global ? this.index + 1 : 0;
        this.ranges = new NameRange[2];
        this.ranges[0] = new NameRange(extSheet, theStartRow, theEndRow, theStartCol, theEndCol);
        this.ranges[1] = new NameRange(extSheet, theStartRow2, theEndRow2, theStartCol2, theEndCol2);
    }

    public byte[] getData() {
        if (this.data != null && !this.modified) {
            return this.data;
        }
        int detailLength;
        int length;
        int pos;
        if (this.ranges.length <= 1) {
            detailLength = 11;
        } else {
            detailLength = (this.ranges.length * 11) + 4;
        }
        int length2 = detailLength + 15;
        if (this.builtInName == null) {
            length = this.name.length();
        } else {
            length = 1;
        }
        this.data = new byte[(length2 + length)];
        int options = 0;
        if (this.builtInName != null) {
            options = 32;
        }
        IntegerHelper.getTwoBytes(options, this.data, 0);
        this.data[2] = (byte) 0;
        if (this.builtInName == null) {
            this.data[3] = (byte) ((byte) this.name.length());
        } else {
            this.data[3] = (byte) 1;
        }
        IntegerHelper.getTwoBytes(detailLength, this.data, 4);
        IntegerHelper.getTwoBytes(this.sheetRef, this.data, 6);
        IntegerHelper.getTwoBytes(this.sheetRef, this.data, 8);
        if (this.builtInName == null) {
            StringHelper.getBytes(this.name, this.data, 15);
        } else {
            this.data[15] = (byte) ((byte) this.builtInName.getValue());
        }
        if (this.builtInName == null) {
            pos = this.name.length() + 15;
        } else {
            pos = 16;
        }
        byte[] rd;
        if (this.ranges.length <= 1) {
            this.data[pos] = (byte) 59;
            rd = this.ranges[0].getData();
            System.arraycopy(rd, 0, this.data, pos + 1, rd.length);
        } else {
            int pos2 = pos + 1;
            this.data[pos] = (byte) 41;
            IntegerHelper.getTwoBytes(detailLength - 3, this.data, pos2);
            pos = pos2 + 2;
            for (NameRange data : this.ranges) {
                pos2 = pos + 1;
                this.data[pos] = (byte) 59;
                rd = data.getData();
                System.arraycopy(rd, 0, this.data, pos2, rd.length);
                pos = pos2 + rd.length;
            }
            this.data[pos] = (byte) 16;
        }
        return this.data;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }
}
