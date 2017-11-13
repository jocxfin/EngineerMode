package jxl.biff.formula;

class Name extends Operand {
    public int read(byte[] data, int pos) {
        return 6;
    }

    byte[] getBytes() {
        return new byte[6];
    }

    public void getString(StringBuffer buf) {
        buf.append("[Name record not implemented]");
    }
}
