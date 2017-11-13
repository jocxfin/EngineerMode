package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;
import jxl.common.Logger;

public class ExternalNameRecord extends RecordData {
    private static Logger logger = Logger.getLogger(ExternalNameRecord.class);
    private boolean addInFunction;
    private String name;

    ExternalNameRecord(Record t, WorkbookSettings ws) {
        boolean unicode = false;
        super(t);
        byte[] data = getRecord().getData();
        if (IntegerHelper.getInt(data[0], data[1]) == 0) {
            this.addInFunction = true;
        }
        if (this.addInFunction) {
            int length = data[6];
            if (data[7] != (byte) 0) {
                unicode = true;
            }
            if (unicode) {
                this.name = StringHelper.getUnicodeString(data, length, 8);
            } else {
                this.name = StringHelper.getString(data, length, 8, ws);
            }
        }
    }

    public boolean isAddInFunction() {
        return this.addInFunction;
    }

    public String getName() {
        return this.name;
    }
}
