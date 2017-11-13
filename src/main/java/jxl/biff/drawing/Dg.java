package jxl.biff.drawing;

import jxl.biff.IntegerHelper;

class Dg extends EscherAtom {
    private byte[] data;
    private int drawingId = 1;
    private int seed;
    private int shapeCount;

    public Dg(EscherRecordData erd) {
        super(erd);
        byte[] bytes = getBytes();
        this.shapeCount = IntegerHelper.getInt(bytes[0], bytes[1], bytes[2], bytes[3]);
        this.seed = IntegerHelper.getInt(bytes[4], bytes[5], bytes[6], bytes[7]);
    }

    public Dg(int numDrawings) {
        super(EscherRecordType.DG);
        this.shapeCount = numDrawings + 1;
        this.seed = (this.shapeCount + 1024) + 1;
        setInstance(this.drawingId);
    }

    byte[] getData() {
        this.data = new byte[8];
        IntegerHelper.getFourBytes(this.shapeCount, this.data, 0);
        IntegerHelper.getFourBytes(this.seed, this.data, 4);
        return setHeaderData(this.data);
    }
}
