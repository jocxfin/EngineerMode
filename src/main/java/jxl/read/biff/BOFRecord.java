package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

public class BOFRecord extends RecordData {
    private static Logger logger = Logger.getLogger(BOFRecord.class);
    private int substreamType;
    private int version;

    BOFRecord(Record t) {
        super(t);
        byte[] data = getRecord().getData();
        this.version = IntegerHelper.getInt(data[0], data[1]);
        this.substreamType = IntegerHelper.getInt(data[2], data[3]);
    }

    public boolean isBiff8() {
        return this.version == 1536;
    }

    public boolean isBiff7() {
        return this.version == 1280;
    }

    boolean isWorkbookGlobals() {
        return this.substreamType == 5;
    }

    public boolean isWorksheet() {
        return this.substreamType == 16;
    }

    public boolean isChart() {
        return this.substreamType == 32;
    }

    int getLength() {
        return getRecord().getLength();
    }
}
