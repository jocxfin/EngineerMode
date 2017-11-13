package jxl.biff.drawing;

class SplitMenuColors extends EscherAtom {
    private byte[] data;

    public SplitMenuColors(EscherRecordData erd) {
        super(erd);
    }

    public SplitMenuColors() {
        super(EscherRecordType.SPLIT_MENU_COLORS);
        setVersion(0);
        setInstance(4);
        this.data = new byte[]{(byte) 13, (byte) 0, (byte) 0, (byte) 8, (byte) 12, (byte) 0, (byte) 0, (byte) 8, (byte) 23, (byte) 0, (byte) 0, (byte) 8, (byte) -9, (byte) 0, (byte) 0, (byte) 16};
    }

    byte[] getData() {
        return setHeaderData(this.data);
    }
}
