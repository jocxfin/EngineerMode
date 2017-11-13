package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class MulBlankRecord extends RecordData {
    private static Logger logger = Logger.getLogger(MulBlankRecord.class);
    private int colFirst;
    private int colLast;
    private int numblanks = ((this.colLast - this.colFirst) + 1);
    private int row;
    private int[] xfIndices = new int[this.numblanks];

    public MulBlankRecord(Record t) {
        super(t);
        byte[] data = getRecord().getData();
        int length = getRecord().getLength();
        this.row = IntegerHelper.getInt(data[0], data[1]);
        this.colFirst = IntegerHelper.getInt(data[2], data[3]);
        this.colLast = IntegerHelper.getInt(data[length - 2], data[length - 1]);
        readBlanks(data);
    }

    private void readBlanks(byte[] data) {
        int pos = 4;
        for (int i = 0; i < this.numblanks; i++) {
            this.xfIndices[i] = IntegerHelper.getInt(data[pos], data[pos + 1]);
            pos += 2;
        }
    }

    public int getRow() {
        return this.row;
    }

    public int getFirstColumn() {
        return this.colFirst;
    }

    public int getNumberOfColumns() {
        return this.numblanks;
    }

    public int getXFIndex(int index) {
        return this.xfIndices[index];
    }
}
