package jxl.write.biff;

import jxl.BooleanCell;
import jxl.CellType;
import jxl.biff.Type;

public abstract class BooleanRecord extends CellValue {
    private boolean value;

    protected BooleanRecord(BooleanCell nc) {
        super(Type.BOOLERR, nc);
        this.value = nc.getValue();
    }

    public boolean getValue() {
        return this.value;
    }

    public String getContents() {
        return new Boolean(this.value).toString();
    }

    public CellType getType() {
        return CellType.BOOLEAN;
    }

    public byte[] getData() {
        byte[] celldata = super.getData();
        byte[] data = new byte[(celldata.length + 2)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        if (this.value) {
            data[celldata.length] = (byte) 1;
        }
        return data;
    }
}
