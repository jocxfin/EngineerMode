package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Excel9FileRecord extends WritableRecordData {
    public Excel9FileRecord() {
        super(Type.EXCEL9FILE);
    }

    public byte[] getData() {
        return new byte[0];
    }
}
