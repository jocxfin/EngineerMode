package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ScenarioProtectRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean protection;

    public ScenarioProtectRecord(boolean prot) {
        super(Type.SCENPROTECT);
        this.protection = prot;
        if (this.protection) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
