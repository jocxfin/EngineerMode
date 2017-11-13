package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class MulRKRecord extends RecordData {
    private static Logger logger = Logger.getLogger(MulRKRecord.class);
    private int colFirst;
    private int colLast;
    private int numrks = ((this.colLast - this.colFirst) + 1);
    private int[] rknumbers = new int[this.numrks];
    private int row;
    private int[] xfIndices = new int[this.numrks];

    public MulRKRecord(Record t) {
        super(t);
        byte[] data = getRecord().getData();
        int length = getRecord().getLength();
        this.row = IntegerHelper.getInt(data[0], data[1]);
        this.colFirst = IntegerHelper.getInt(data[2], data[3]);
        this.colLast = IntegerHelper.getInt(data[length - 2], data[length - 1]);
        readRks(data);
    }

    private void readRks(byte[] data) {
        int pos = 4;
        for (int i = 0; i < this.numrks; i++) {
            this.xfIndices[i] = IntegerHelper.getInt(data[pos], data[pos + 1]);
            this.rknumbers[i] = IntegerHelper.getInt(data[pos + 2], data[pos + 3], data[pos + 4], data[pos + 5]);
            pos += 6;
        }
    }

    public int getRow() {
        return this.row;
    }

    public int getFirstColumn() {
        return this.colFirst;
    }

    public int getNumberOfColumns() {
        return this.numrks;
    }

    public int getRKNumber(int index) {
        return this.rknumbers[index];
    }

    public int getXFIndex(int index) {
        return this.xfIndices[index];
    }
}
