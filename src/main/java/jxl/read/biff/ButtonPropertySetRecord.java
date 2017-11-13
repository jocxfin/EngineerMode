package jxl.read.biff;

import jxl.biff.RecordData;
import jxl.common.Logger;

public class ButtonPropertySetRecord extends RecordData {
    private static Logger logger = Logger.getLogger(ButtonPropertySetRecord.class);

    ButtonPropertySetRecord(Record t) {
        super(t);
    }

    public byte[] getData() {
        return getRecord().getData();
    }
}
