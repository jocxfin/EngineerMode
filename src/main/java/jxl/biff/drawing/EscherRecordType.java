package jxl.biff.drawing;

final class EscherRecordType {
    public static final EscherRecordType BSE = new EscherRecordType(61447);
    public static final EscherRecordType BSTORE_CONTAINER = new EscherRecordType(61441);
    public static final EscherRecordType CLIENT_ANCHOR = new EscherRecordType(61456);
    public static final EscherRecordType CLIENT_DATA = new EscherRecordType(61457);
    public static final EscherRecordType CLIENT_TEXT_BOX = new EscherRecordType(61453);
    public static final EscherRecordType DG = new EscherRecordType(61448);
    public static final EscherRecordType DGG = new EscherRecordType(61446);
    public static final EscherRecordType DGG_CONTAINER = new EscherRecordType(61440);
    public static final EscherRecordType DG_CONTAINER = new EscherRecordType(61442);
    public static final EscherRecordType OPT = new EscherRecordType(61451);
    public static final EscherRecordType SP = new EscherRecordType(61450);
    public static final EscherRecordType SPGR = new EscherRecordType(61449);
    public static final EscherRecordType SPGR_CONTAINER = new EscherRecordType(61443);
    public static final EscherRecordType SPLIT_MENU_COLORS = new EscherRecordType(61726);
    public static final EscherRecordType SP_CONTAINER = new EscherRecordType(61444);
    public static final EscherRecordType UNKNOWN = new EscherRecordType(0);
    private static EscherRecordType[] types = new EscherRecordType[0];
    private int value;

    private EscherRecordType(int val) {
        this.value = val;
        EscherRecordType[] newtypes = new EscherRecordType[(types.length + 1)];
        System.arraycopy(types, 0, newtypes, 0, types.length);
        newtypes[types.length] = this;
        types = newtypes;
    }

    public int getValue() {
        return this.value;
    }

    public static EscherRecordType getType(int val) {
        EscherRecordType type = UNKNOWN;
        for (int i = 0; i < types.length; i++) {
            if (val == types[i].value) {
                return types[i];
            }
        }
        return type;
    }
}
