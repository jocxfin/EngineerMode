package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Window1Record extends WritableRecordData {
    private byte[] data = new byte[]{(byte) 104, (byte) 1, (byte) 14, (byte) 1, (byte) 92, (byte) 58, (byte) -66, (byte) 35, (byte) 56, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 88, (byte) 2};
    private int selectedSheet;

    public Window1Record(int selSheet) {
        super(Type.WINDOW1);
        this.selectedSheet = selSheet;
        IntegerHelper.getTwoBytes(this.selectedSheet, this.data, 10);
    }

    public byte[] getData() {
        return this.data;
    }
}
