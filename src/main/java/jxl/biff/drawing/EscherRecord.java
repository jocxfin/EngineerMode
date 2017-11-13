package jxl.biff.drawing;

import jxl.common.Logger;

abstract class EscherRecord {
    private static Logger logger = Logger.getLogger(EscherRecord.class);
    private EscherRecordData data;

    abstract byte[] getData();

    protected EscherRecord(EscherRecordData erd) {
        this.data = erd;
    }

    protected EscherRecord(EscherRecordType type) {
        this.data = new EscherRecordData(type);
    }

    protected void setContainer(boolean cont) {
        this.data.setContainer(cont);
    }

    public int getLength() {
        return this.data.getLength() + 8;
    }

    protected final EscherStream getEscherStream() {
        return this.data.getEscherStream();
    }

    protected final int getPos() {
        return this.data.getPos();
    }

    protected final int getInstance() {
        return this.data.getInstance();
    }

    protected final void setInstance(int i) {
        this.data.setInstance(i);
    }

    protected final void setVersion(int v) {
        this.data.setVersion(v);
    }

    public EscherRecordType getType() {
        return this.data.getType();
    }

    final byte[] setHeaderData(byte[] d) {
        return this.data.setHeaderData(d);
    }

    byte[] getBytes() {
        return this.data.getBytes();
    }

    protected int getStreamLength() {
        return this.data.getStreamLength();
    }
}
