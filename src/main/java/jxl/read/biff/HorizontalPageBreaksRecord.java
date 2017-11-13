package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class HorizontalPageBreaksRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private final Logger logger = Logger.getLogger(HorizontalPageBreaksRecord.class);
    private int[] rowBreaks;

    private static class Biff7 {
        private Biff7() {
        }
    }

    public HorizontalPageBreaksRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        int numbreaks = IntegerHelper.getInt(data[0], data[1]);
        int pos = 2;
        this.rowBreaks = new int[numbreaks];
        for (int i = 0; i < numbreaks; i++) {
            this.rowBreaks[i] = IntegerHelper.getInt(data[pos], data[pos + 1]);
            pos += 6;
        }
    }

    public HorizontalPageBreaksRecord(Record t, Biff7 biff7) {
        super(t);
        byte[] data = t.getData();
        int numbreaks = IntegerHelper.getInt(data[0], data[1]);
        int pos = 2;
        this.rowBreaks = new int[numbreaks];
        for (int i = 0; i < numbreaks; i++) {
            this.rowBreaks[i] = IntegerHelper.getInt(data[pos], data[pos + 1]);
            pos += 2;
        }
    }

    public int[] getRowBreaks() {
        return this.rowBreaks;
    }
}
