package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class RefreshAllRecord extends RecordData {
    private static Logger logger = Logger.getLogger(RefreshAllRecord.class);
    private boolean refreshAll;

    public RefreshAllRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = t.getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 1) {
            z = true;
        }
        this.refreshAll = z;
    }

    public boolean getRefreshAll() {
        return this.refreshAll;
    }
}
