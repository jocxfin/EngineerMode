package jxl.biff.drawing;

final class ShapeType {
    public static final ShapeType HOST_CONTROL = new ShapeType(201);
    public static final ShapeType MIN = new ShapeType(0);
    public static final ShapeType PICTURE_FRAME = new ShapeType(75);
    public static final ShapeType TEXT_BOX = new ShapeType(202);
    public static final ShapeType UNKNOWN = new ShapeType(-1);
    private static ShapeType[] types = new ShapeType[0];
    private int value;

    ShapeType(int v) {
        this.value = v;
        ShapeType[] old = types;
        types = new ShapeType[(types.length + 1)];
        System.arraycopy(old, 0, types, 0, old.length);
        types[old.length] = this;
    }

    static ShapeType getType(int v) {
        ShapeType st = UNKNOWN;
        boolean found = false;
        for (int i = 0; i < types.length && !found; i++) {
            if (types[i].value == v) {
                found = true;
                st = types[i];
            }
        }
        return st;
    }

    public int getValue() {
        return this.value;
    }
}
