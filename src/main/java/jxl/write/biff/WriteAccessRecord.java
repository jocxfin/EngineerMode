package jxl.write.biff;

import jxl.Workbook;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class WriteAccessRecord extends WritableRecordData {
    private byte[] data = new byte[112];

    public WriteAccessRecord(String userName) {
        String astring;
        super(Type.WRITEACCESS);
        if (userName == null) {
            astring = "Java Excel API v" + Workbook.getVersion();
        } else {
            astring = userName;
        }
        StringHelper.getBytes(astring, this.data, 0);
        for (int i = astring.length(); i < this.data.length; i++) {
            this.data[i] = (byte) 32;
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
