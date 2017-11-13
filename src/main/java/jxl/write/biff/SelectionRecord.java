package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class SelectionRecord extends WritableRecordData {
    public static final PaneType lowerLeft = new PaneType(2);
    public static final PaneType lowerRight = new PaneType(0);
    public static final PaneType upperLeft = new PaneType(3);
    public static final PaneType upperRight = new PaneType(1);
    private int column;
    private PaneType pane;
    private int row;

    private static class PaneType {
        int val;

        PaneType(int v) {
            this.val = v;
        }
    }

    public SelectionRecord(PaneType pt, int col, int r) {
        super(Type.SELECTION);
        this.column = col;
        this.row = r;
        this.pane = pt;
    }

    public byte[] getData() {
        byte[] data = new byte[15];
        data[0] = (byte) ((byte) this.pane.val);
        IntegerHelper.getTwoBytes(this.row, data, 1);
        IntegerHelper.getTwoBytes(this.column, data, 3);
        data[7] = (byte) 1;
        IntegerHelper.getTwoBytes(this.row, data, 9);
        IntegerHelper.getTwoBytes(this.row, data, 11);
        data[13] = (byte) ((byte) this.column);
        data[14] = (byte) ((byte) this.column);
        return data;
    }
}
