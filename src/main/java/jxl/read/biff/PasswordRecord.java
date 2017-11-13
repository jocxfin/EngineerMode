package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.Type;

class PasswordRecord extends RecordData {
    private int passwordHash;

    public PasswordRecord(Record t) {
        super(Type.PASSWORD);
        byte[] data = t.getData();
        this.passwordHash = IntegerHelper.getInt(data[0], data[1]);
    }

    public int getPasswordHash() {
        return this.passwordHash;
    }
}
