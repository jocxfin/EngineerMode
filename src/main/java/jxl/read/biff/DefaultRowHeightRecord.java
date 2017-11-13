package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;

class DefaultRowHeightRecord extends RecordData {
    private int height;

    public DefaultRowHeightRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        if (data.length > 2) {
            this.height = IntegerHelper.getInt(data[2], data[3]);
        }
    }

    public int getHeight() {
        return this.height;
    }
}
