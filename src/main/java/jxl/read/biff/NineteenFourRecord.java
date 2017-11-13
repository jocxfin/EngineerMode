package jxl.read.biff;

import jxl.biff.RecordData;

class NineteenFourRecord extends RecordData {
    private boolean nineteenFour;

    public NineteenFourRecord(Record t) {
        boolean z = false;
        super(t);
        if (getRecord().getData()[0] == (byte) 1) {
            z = true;
        }
        this.nineteenFour = z;
    }

    public boolean is1904() {
        return this.nineteenFour;
    }
}
