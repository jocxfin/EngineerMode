package jxl.write.biff;

import com.android.engineeringmode.functions.Light;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class HorizontalPageBreaksRecord extends WritableRecordData {
    private int[] rowBreaks;

    public HorizontalPageBreaksRecord(int[] breaks) {
        super(Type.HORIZONTALPAGEBREAKS);
        this.rowBreaks = breaks;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.rowBreaks.length * 6) + 2)];
        IntegerHelper.getTwoBytes(this.rowBreaks.length, data, 0);
        int pos = 2;
        for (int twoBytes : this.rowBreaks) {
            IntegerHelper.getTwoBytes(twoBytes, data, pos);
            IntegerHelper.getTwoBytes(Light.MAIN_KEY_MAX, data, pos + 4);
            pos += 6;
        }
        return data;
    }
}
