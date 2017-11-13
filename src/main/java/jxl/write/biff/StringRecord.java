package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class StringRecord extends WritableRecordData {
    private String value;

    public StringRecord(String val) {
        super(Type.STRING);
        this.value = val;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.value.length() * 2) + 3)];
        IntegerHelper.getTwoBytes(this.value.length(), data, 0);
        data[2] = (byte) 1;
        StringHelper.getUnicodeBytes(this.value, data, 3);
        return data;
    }
}
