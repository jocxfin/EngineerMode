package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class CodepageRecord extends RecordData {
    private static Logger logger = Logger.getLogger(CodepageRecord.class);
    private int characterSet;

    public CodepageRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        this.characterSet = IntegerHelper.getInt(data[0], data[1]);
    }

    public int getCharacterSet() {
        return this.characterSet;
    }
}
