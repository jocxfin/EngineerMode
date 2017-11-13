package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;
import jxl.common.Logger;

public class HeaderRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private static Logger logger = Logger.getLogger(HeaderRecord.class);
    private String header;

    private static class Biff7 {
        private Biff7() {
        }
    }

    HeaderRecord(Record t, WorkbookSettings ws) {
        boolean unicode = false;
        super(t);
        byte[] data = getRecord().getData();
        if (data.length != 0) {
            int chars = IntegerHelper.getInt(data[0], data[1]);
            if (data[2] == (byte) 1) {
                unicode = true;
            }
            if (unicode) {
                this.header = StringHelper.getUnicodeString(data, chars, 3);
            } else {
                this.header = StringHelper.getString(data, chars, 3, ws);
            }
        }
    }

    HeaderRecord(Record t, WorkbookSettings ws, Biff7 dummy) {
        super(t);
        byte[] data = getRecord().getData();
        if (data.length != 0) {
            this.header = StringHelper.getString(data, data[0], 1, ws);
        }
    }

    String getHeader() {
        return this.header;
    }
}
