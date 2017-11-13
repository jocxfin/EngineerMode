package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class MMSRecord extends WritableRecordData {
    private byte[] data = new byte[2];
    private byte numMenuItemsAdded;
    private byte numMenuItemsDeleted;

    public MMSRecord(int menuItemsAdded, int menuItemsDeleted) {
        super(Type.MMS);
        this.numMenuItemsAdded = (byte) ((byte) menuItemsAdded);
        this.numMenuItemsDeleted = (byte) ((byte) menuItemsDeleted);
        this.data[0] = (byte) this.numMenuItemsAdded;
        this.data[1] = (byte) this.numMenuItemsDeleted;
    }

    public byte[] getData() {
        return this.data;
    }
}
