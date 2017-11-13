package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class CalcModeRecord extends RecordData {
    private static Logger logger = Logger.getLogger(CalcModeRecord.class);
    private boolean automatic;

    public CalcModeRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = t.getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 1) {
            z = true;
        }
        this.automatic = z;
    }

    public boolean isAutomatic() {
        return this.automatic;
    }
}
