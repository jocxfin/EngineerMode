package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class PaneRecord extends WritableRecordData {
    private int columnsVisible;
    private int rowsVisible;

    public PaneRecord(int cols, int rows) {
        super(Type.PANE);
        this.rowsVisible = rows;
        this.columnsVisible = cols;
    }

    public byte[] getData() {
        byte[] data = new byte[10];
        IntegerHelper.getTwoBytes(this.columnsVisible, data, 0);
        IntegerHelper.getTwoBytes(this.rowsVisible, data, 2);
        if (this.rowsVisible > 0) {
            IntegerHelper.getTwoBytes(this.rowsVisible, data, 4);
        }
        if (this.columnsVisible > 0) {
            IntegerHelper.getTwoBytes(this.columnsVisible, data, 6);
        }
        int activePane = 3;
        if (this.rowsVisible > 0 && this.columnsVisible == 0) {
            activePane = 2;
        } else if (this.rowsVisible == 0 && this.columnsVisible > 0) {
            activePane = 1;
        } else if (this.rowsVisible > 0 && this.columnsVisible > 0) {
            activePane = 0;
        }
        IntegerHelper.getTwoBytes(activePane, data, 8);
        return data;
    }
}
