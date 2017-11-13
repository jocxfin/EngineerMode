package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class WindowProtectedRecord extends RecordData {
    private static Logger logger = Logger.getLogger(WindowProtectedRecord.class);
    private boolean windowProtected;

    public WindowProtectedRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = t.getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 1) {
            z = true;
        }
        this.windowProtected = z;
    }

    public boolean getWindowProtected() {
        return this.windowProtected;
    }
}
