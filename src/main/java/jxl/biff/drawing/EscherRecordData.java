package jxl.biff.drawing;

import jxl.biff.IntegerHelper;
import jxl.common.Logger;

final class EscherRecordData {
    private static Logger logger = Logger.getLogger(EscherRecordData.class);
    private boolean container;
    private EscherStream escherStream;
    private int instance;
    private int length;
    private int pos;
    private int recordId;
    private int streamLength;
    private EscherRecordType type;
    private int version;

    public EscherRecordData(EscherStream dg, int p) {
        this.escherStream = dg;
        this.pos = p;
        byte[] data = this.escherStream.getData();
        this.streamLength = data.length;
        int value = IntegerHelper.getInt(data[this.pos], data[this.pos + 1]);
        this.instance = (65520 & value) >> 4;
        this.version = value & 15;
        this.recordId = IntegerHelper.getInt(data[this.pos + 2], data[this.pos + 3]);
        this.length = IntegerHelper.getInt(data[this.pos + 4], data[this.pos + 5], data[this.pos + 6], data[this.pos + 7]);
        if (this.version != 15) {
            this.container = false;
        } else {
            this.container = true;
        }
    }

    public EscherRecordData(EscherRecordType t) {
        this.type = t;
        this.recordId = this.type.getValue();
    }

    public boolean isContainer() {
        return this.container;
    }

    public int getLength() {
        return this.length;
    }

    int getPos() {
        return this.pos;
    }

    EscherRecordType getType() {
        if (this.type == null) {
            this.type = EscherRecordType.getType(this.recordId);
        }
        return this.type;
    }

    int getInstance() {
        return this.instance;
    }

    void setContainer(boolean c) {
        this.container = c;
    }

    void setInstance(int inst) {
        this.instance = inst;
    }

    void setVersion(int v) {
        this.version = v;
    }

    byte[] setHeaderData(byte[] d) {
        byte[] data = new byte[(d.length + 8)];
        System.arraycopy(d, 0, data, 8, d.length);
        if (this.container) {
            this.version = 15;
        }
        IntegerHelper.getTwoBytes((this.instance << 4) | this.version, data, 0);
        IntegerHelper.getTwoBytes(this.recordId, data, 2);
        IntegerHelper.getFourBytes(d.length, data, 4);
        return data;
    }

    EscherStream getEscherStream() {
        return this.escherStream;
    }

    byte[] getBytes() {
        byte[] d = new byte[this.length];
        System.arraycopy(this.escherStream.getData(), this.pos + 8, d, 0, this.length);
        return d;
    }

    int getStreamLength() {
        return this.streamLength;
    }
}
