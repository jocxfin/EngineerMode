package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class ConditionalFormatRangeRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(ConditionalFormatRangeRecord.class);
    private byte[] data = getRecord().getData();
    private Range enclosingRange;
    private boolean initialized = false;
    private boolean modified = false;
    private int numRanges;
    private Range[] ranges;

    private static class Range {
        public int firstColumn;
        public int firstRow;
        public int lastColumn;
        public int lastRow;
        public boolean modified = false;
    }

    public ConditionalFormatRangeRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        if (!this.modified) {
            return this.data;
        }
        byte[] d = new byte[((this.ranges.length * 8) + 14)];
        System.arraycopy(this.data, 0, d, 0, 4);
        IntegerHelper.getTwoBytes(this.enclosingRange.firstRow, d, 4);
        IntegerHelper.getTwoBytes(this.enclosingRange.lastRow, d, 6);
        IntegerHelper.getTwoBytes(this.enclosingRange.firstColumn, d, 8);
        IntegerHelper.getTwoBytes(this.enclosingRange.lastColumn, d, 10);
        IntegerHelper.getTwoBytes(this.numRanges, d, 12);
        int pos = 14;
        for (int i = 0; i < this.ranges.length; i++) {
            IntegerHelper.getTwoBytes(this.ranges[i].firstRow, d, pos);
            IntegerHelper.getTwoBytes(this.ranges[i].lastRow, d, pos + 2);
            IntegerHelper.getTwoBytes(this.ranges[i].firstColumn, d, pos + 4);
            IntegerHelper.getTwoBytes(this.ranges[i].lastColumn, d, pos + 6);
            pos += 8;
        }
        return d;
    }
}
