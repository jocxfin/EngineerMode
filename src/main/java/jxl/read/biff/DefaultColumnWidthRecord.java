package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;

class DefaultColumnWidthRecord extends RecordData {
    private int width;

    public DefaultColumnWidthRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        this.width = IntegerHelper.getInt(data[0], data[1]);
    }

    public int getWidth() {
        return this.width;
    }
}
