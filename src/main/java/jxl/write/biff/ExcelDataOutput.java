package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;

interface ExcelDataOutput {
    void close() throws IOException;

    int getPosition() throws IOException;

    void setData(byte[] bArr, int i) throws IOException;

    void write(byte[] bArr) throws IOException;

    void writeData(OutputStream outputStream) throws IOException;
}
