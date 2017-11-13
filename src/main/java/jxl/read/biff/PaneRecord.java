package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class PaneRecord extends RecordData {
    private static Logger logger = Logger.getLogger(PaneRecord.class);
    private int columnsVisible;
    private int rowsVisible;

    public PaneRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        this.columnsVisible = IntegerHelper.getInt(data[0], data[1]);
        this.rowsVisible = IntegerHelper.getInt(data[2], data[3]);
    }

    public final int getRowsVisible() {
        return this.rowsVisible;
    }

    public final int getColumnsVisible() {
        return this.columnsVisible;
    }
}
