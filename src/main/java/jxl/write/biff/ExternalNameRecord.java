package jxl.write.biff;

import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;

class ExternalNameRecord extends WritableRecordData {
    Logger logger = Logger.getLogger(ExternalNameRecord.class);
    private String name;

    public ExternalNameRecord(String n) {
        super(Type.EXTERNNAME);
        this.name = n;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.name.length() * 2) + 12)];
        data[6] = (byte) ((byte) this.name.length());
        data[7] = (byte) 1;
        StringHelper.getUnicodeBytes(this.name, data, 8);
        int pos = (this.name.length() * 2) + 8;
        data[pos] = (byte) 2;
        data[pos + 1] = (byte) 0;
        data[pos + 2] = (byte) 28;
        data[pos + 3] = (byte) 23;
        return data;
    }
}
