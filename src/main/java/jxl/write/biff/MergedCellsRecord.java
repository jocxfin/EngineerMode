package jxl.write.biff;

import java.util.ArrayList;

import jxl.Cell;
import jxl.Range;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

public class MergedCellsRecord extends WritableRecordData {
    private ArrayList ranges;

    protected MergedCellsRecord(ArrayList mc) {
        super(Type.MERGEDCELLS);
        this.ranges = mc;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.ranges.size() * 8) + 2)];
        IntegerHelper.getTwoBytes(this.ranges.size(), data, 0);
        int pos = 2;
        for (int i = 0; i < this.ranges.size(); i++) {
            Range range = (Range) this.ranges.get(i);
            Cell tl = range.getTopLeft();
            Cell br = range.getBottomRight();
            IntegerHelper.getTwoBytes(tl.getRow(), data, pos);
            IntegerHelper.getTwoBytes(br.getRow(), data, pos + 2);
            IntegerHelper.getTwoBytes(tl.getColumn(), data, pos + 4);
            IntegerHelper.getTwoBytes(br.getColumn(), data, pos + 6);
            pos += 8;
        }
        return data;
    }
}
