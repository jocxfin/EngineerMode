package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class HideobjRecord extends RecordData {
    private static Logger logger = Logger.getLogger(HideobjRecord.class);
    private int hidemode;

    public HideobjRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        this.hidemode = IntegerHelper.getInt(data[0], data[1]);
    }

    public int getHideMode() {
        return this.hidemode;
    }
}
