package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;

class CentreRecord extends RecordData {
    private boolean centre;

    public CentreRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = getRecord().getData();
        if (IntegerHelper.getInt(data[0], data[1]) != 0) {
            z = true;
        }
        this.centre = z;
    }

    public boolean isCentre() {
        return this.centre;
    }
}
