package jxl.read.biff;

import jxl.Range;
import jxl.Sheet;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.SheetRangeImpl;

public class MergedCellsRecord extends RecordData {
    private Range[] ranges;

    MergedCellsRecord(Record t, Sheet s) {
        super(t);
        byte[] data = getRecord().getData();
        int numRanges = IntegerHelper.getInt(data[0], data[1]);
        this.ranges = new Range[numRanges];
        int pos = 2;
        for (int i = 0; i < numRanges; i++) {
            int firstRow = IntegerHelper.getInt(data[pos], data[pos + 1]);
            int lastRow = IntegerHelper.getInt(data[pos + 2], data[pos + 3]);
            this.ranges[i] = new SheetRangeImpl(s, IntegerHelper.getInt(data[pos + 4], data[pos + 5]), firstRow, IntegerHelper.getInt(data[pos + 6], data[pos + 7]), lastRow);
            pos += 8;
        }
    }

    public Range[] getRanges() {
        return this.ranges;
    }
}
