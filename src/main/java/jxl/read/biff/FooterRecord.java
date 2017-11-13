package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;

public class FooterRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private String footer;

    private static class Biff7 {
        private Biff7() {
        }
    }

    FooterRecord(Record t, WorkbookSettings ws) {
        boolean unicode = false;
        super(t);
        byte[] data = getRecord().getData();
        if (data.length != 0) {
            int chars = IntegerHelper.getInt(data[0], data[1]);
            if (data[2] == (byte) 1) {
                unicode = true;
            }
            if (unicode) {
                this.footer = StringHelper.getUnicodeString(data, chars, 3);
            } else {
                this.footer = StringHelper.getString(data, chars, 3, ws);
            }
        }
    }

    FooterRecord(Record t, WorkbookSettings ws, Biff7 dummy) {
        super(t);
        byte[] data = getRecord().getData();
        if (data.length != 0) {
            this.footer = StringHelper.getString(data, data[0], 1, ws);
        }
    }

    String getFooter() {
        return this.footer;
    }
}
