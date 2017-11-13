package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class DataValidityListRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(DataValidityListRecord.class);
    private byte[] data;
    private DValParser dvalParser;
    private int numSettings;
    private int objectId;

    public DataValidityListRecord(Record t) {
        super(t);
        this.data = getRecord().getData();
        this.objectId = IntegerHelper.getInt(this.data[10], this.data[11], this.data[12], this.data[13]);
        this.numSettings = IntegerHelper.getInt(this.data[14], this.data[15], this.data[16], this.data[17]);
    }

    public DataValidityListRecord(DValParser dval) {
        super(Type.DVAL);
        this.dvalParser = dval;
    }

    DataValidityListRecord(DataValidityListRecord dvlr) {
        super(Type.DVAL);
        this.data = dvlr.getData();
    }

    int getNumberOfSettings() {
        return this.numSettings;
    }

    public byte[] getData() {
        if (this.dvalParser != null) {
            return this.dvalParser.getData();
        }
        return this.data;
    }

    void dvRemoved() {
        if (this.dvalParser == null) {
            this.dvalParser = new DValParser(this.data);
        }
        this.dvalParser.dvRemoved();
    }

    void dvAdded() {
        if (this.dvalParser == null) {
            this.dvalParser = new DValParser(this.data);
        }
        this.dvalParser.dvAdded();
    }

    public boolean hasDVRecords() {
        boolean z = false;
        if (this.dvalParser == null) {
            return true;
        }
        if (this.dvalParser.getNumberOfDVRecords() > 0) {
            z = true;
        }
        return z;
    }

    public int getObjectId() {
        if (this.dvalParser != null) {
            return this.dvalParser.getObjectId();
        }
        return this.objectId;
    }
}
