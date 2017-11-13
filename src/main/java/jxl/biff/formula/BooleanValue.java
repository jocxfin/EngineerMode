package jxl.biff.formula;

class BooleanValue extends Operand {
    private boolean value;

    public BooleanValue(String s) {
        this.value = Boolean.valueOf(s).booleanValue();
    }

    public int read(byte[] data, int pos) {
        boolean z;
        if (data[pos] != (byte) 1) {
            z = false;
        } else {
            z = true;
        }
        this.value = z;
        return 1;
    }

    byte[] getBytes() {
        int i = 0;
        byte[] data = new byte[2];
        data[0] = (byte) Token.BOOL.getCode();
        if (this.value) {
            i = 1;
        }
        data[1] = (byte) ((byte) i);
        return data;
    }

    public void getString(StringBuffer buf) {
        buf.append(new Boolean(this.value).toString());
    }
}
