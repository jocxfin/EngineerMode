package jxl.biff;

import jxl.common.Logger;

public class DValParser {
    private static int PROMPT_BOX_AT_CELL_MASK = 2;
    private static int PROMPT_BOX_VISIBLE_MASK = 1;
    private static int VALIDITY_DATA_CACHED_MASK = 4;
    private static Logger logger = Logger.getLogger(DValParser.class);
    private int numDVRecords;
    private int objectId;
    private boolean promptBoxAtCell;
    private boolean promptBoxVisible;
    private boolean validityDataCached;

    public DValParser(byte[] data) {
        boolean z;
        boolean z2 = false;
        int options = IntegerHelper.getInt(data[0], data[1]);
        this.promptBoxVisible = (PROMPT_BOX_VISIBLE_MASK & options) != 0;
        if ((PROMPT_BOX_AT_CELL_MASK & options) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.promptBoxAtCell = z;
        if ((VALIDITY_DATA_CACHED_MASK & options) != 0) {
            z2 = true;
        }
        this.validityDataCached = z2;
        this.objectId = IntegerHelper.getInt(data[10], data[11], data[12], data[13]);
        this.numDVRecords = IntegerHelper.getInt(data[14], data[15], data[16], data[17]);
    }

    public DValParser(int objid, int num) {
        this.objectId = objid;
        this.numDVRecords = num;
        this.validityDataCached = true;
    }

    public byte[] getData() {
        byte[] data = new byte[18];
        int options = 0;
        if (this.promptBoxVisible) {
            options = PROMPT_BOX_VISIBLE_MASK | 0;
        }
        if (this.promptBoxAtCell) {
            options |= PROMPT_BOX_AT_CELL_MASK;
        }
        if (this.validityDataCached) {
            options |= VALIDITY_DATA_CACHED_MASK;
        }
        IntegerHelper.getTwoBytes(options, data, 0);
        IntegerHelper.getFourBytes(this.objectId, data, 10);
        IntegerHelper.getFourBytes(this.numDVRecords, data, 14);
        return data;
    }

    public void dvRemoved() {
        this.numDVRecords--;
    }

    public int getNumberOfDVRecords() {
        return this.numDVRecords;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void dvAdded() {
        this.numDVRecords++;
    }
}
