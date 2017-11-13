package jxl.write.biff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import jxl.common.Logger;

class FileDataOutput implements ExcelDataOutput {
    private static Logger logger = Logger.getLogger(FileDataOutput.class);
    private RandomAccessFile data = new RandomAccessFile(this.temporaryFile, "rw");
    private File temporaryFile;

    public FileDataOutput(File tmpdir) throws IOException {
        this.temporaryFile = File.createTempFile("jxl", ".tmp", tmpdir);
        this.temporaryFile.deleteOnExit();
    }

    public void write(byte[] bytes) throws IOException {
        this.data.write(bytes);
    }

    public int getPosition() throws IOException {
        return (int) this.data.getFilePointer();
    }

    public void setData(byte[] newdata, int pos) throws IOException {
        long curpos = this.data.getFilePointer();
        this.data.seek((long) pos);
        this.data.write(newdata);
        this.data.seek(curpos);
    }

    public void writeData(OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        this.data.seek(0);
        while (true) {
            int length = this.data.read(buffer);
            if (length != -1) {
                out.write(buffer, 0, length);
            } else {
                return;
            }
        }
    }

    public void close() throws IOException {
        this.data.close();
        this.temporaryFile.delete();
    }
}
