package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class SaveRecalcRecord extends RecordData {
    private static Logger logger = Logger.getLogger(SaveRecalcRecord.class);
    private boolean recalculateOnSave;

    public SaveRecalcRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = t.getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 1) {
            z = true;
        }
        this.recalculateOnSave = z;
    }

    public boolean getRecalculateOnSave() {
        return this.recalculateOnSave;
    }
}
