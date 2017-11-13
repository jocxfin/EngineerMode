package jxl.biff;

public class ByteArray {
    private byte[] bytes;
    private int growSize;
    private int pos;

    public ByteArray() {
        this(1024);
    }

    public ByteArray(int gs) {
        this.growSize = gs;
        this.bytes = new byte[1024];
        this.pos = 0;
    }

    public void add(byte b) {
        checkSize(1);
        this.bytes[this.pos] = (byte) b;
        this.pos++;
    }

    public void add(byte[] b) {
        checkSize(b.length);
        System.arraycopy(b, 0, this.bytes, this.pos, b.length);
        this.pos += b.length;
    }

    public byte[] getBytes() {
        byte[] returnArray = new byte[this.pos];
        System.arraycopy(this.bytes, 0, returnArray, 0, this.pos);
        return returnArray;
    }

    private void checkSize(int sz) {
        while (this.pos + sz >= this.bytes.length) {
            byte[] newArray = new byte[(this.bytes.length + this.growSize)];
            System.arraycopy(this.bytes, 0, newArray, 0, this.pos);
            this.bytes = newArray;
        }
    }
}
