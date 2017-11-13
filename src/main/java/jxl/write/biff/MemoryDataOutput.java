package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;

import jxl.common.Logger;

class MemoryDataOutput implements ExcelDataOutput {
    private static Logger logger = Logger.getLogger(MemoryDataOutput.class);
    private byte[] data;
    private int growSize;
    private int pos = 0;

    public MemoryDataOutput(int initialSize, int gs) {
        this.data = new byte[initialSize];
        this.growSize = gs;
    }

    public void write(byte[] bytes) {
        while (this.pos + bytes.length > this.data.length) {
            byte[] newdata = new byte[(this.data.length + this.growSize)];
            System.arraycopy(this.data, 0, newdata, 0, this.pos);
            this.data = newdata;
        }
        System.arraycopy(bytes, 0, this.data, this.pos, bytes.length);
        this.pos += bytes.length;
    }

    public int getPosition() {
        return this.pos;
    }

    public void setData(byte[] newdata, int pos) {
        System.arraycopy(newdata, 0, this.data, pos, newdata.length);
    }

    public void writeData(OutputStream out) throws IOException {
        out.write(this.data, 0, this.pos);
    }

    public void close() throws IOException {
    }
}
