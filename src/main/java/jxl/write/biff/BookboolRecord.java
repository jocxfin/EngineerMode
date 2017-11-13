package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class BookboolRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private boolean externalLink;

    public BookboolRecord(boolean extlink) {
        super(Type.BOOKBOOL);
        this.externalLink = extlink;
        if (!this.externalLink) {
            IntegerHelper.getTwoBytes(1, this.data, 0);
        }
    }

    public byte[] getData() {
        return this.data;
    }
}
