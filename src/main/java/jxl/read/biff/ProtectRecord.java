package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;

class ProtectRecord extends RecordData {
    private boolean prot;

    ProtectRecord(Record t) {
        boolean z = false;
        super(t);
        byte[] data = getRecord().getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 1) {
            z = true;
        }
        this.prot = z;
    }

    boolean isProtected() {
        return this.prot;
    }
}
