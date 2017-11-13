package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class FooterRecord extends WritableRecordData {
    private byte[] data;
    private String footer;

    public FooterRecord(String s) {
        super(Type.FOOTER);
        this.footer = s;
    }

    public byte[] getData() {
        if (this.footer == null || this.footer.length() == 0) {
            this.data = new byte[0];
            return this.data;
        }
        this.data = new byte[((this.footer.length() * 2) + 3)];
        IntegerHelper.getTwoBytes(this.footer.length(), this.data, 0);
        this.data[2] = (byte) 1;
        StringHelper.getUnicodeBytes(this.footer, this.data, 3);
        return this.data;
    }
}
