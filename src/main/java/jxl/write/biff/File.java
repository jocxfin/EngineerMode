package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;

import jxl.WorkbookSettings;
import jxl.biff.ByteData;
import jxl.common.Logger;
import jxl.read.biff.CompoundFile;

public final class File {
    private static Logger logger = Logger.getLogger(File.class);
    private int arrayGrowSize;
    private ExcelDataOutput data;
    private int initialFileSize;
    private OutputStream outputStream;
    CompoundFile readCompoundFile;
    private WorkbookSettings workbookSettings;

    File(OutputStream os, WorkbookSettings ws, CompoundFile rcf) throws IOException {
        this.outputStream = os;
        this.workbookSettings = ws;
        this.readCompoundFile = rcf;
        createDataOutput();
    }

    private void createDataOutput() throws IOException {
        if (this.workbookSettings.getUseTemporaryFileDuringWrite()) {
            this.data = new FileDataOutput(this.workbookSettings.getTemporaryFileDuringWriteDirectory());
            return;
        }
        this.initialFileSize = this.workbookSettings.getInitialFileSize();
        this.arrayGrowSize = this.workbookSettings.getArrayGrowSize();
        this.data = new MemoryDataOutput(this.initialFileSize, this.arrayGrowSize);
    }

    void close(boolean cs) throws IOException, JxlWriteException {
        new CompoundFile(this.data, this.data.getPosition(), this.outputStream, this.readCompoundFile).write();
        this.outputStream.flush();
        this.data.close();
        if (cs) {
            this.outputStream.close();
        }
        this.data = null;
        if (!this.workbookSettings.getGCDisabled()) {
            System.gc();
        }
    }

    public void write(ByteData record) throws IOException {
        this.data.write(record.getBytes());
    }

    int getPos() throws IOException {
        return this.data.getPosition();
    }

    void setData(byte[] newdata, int pos) throws IOException {
        this.data.setData(newdata, pos);
    }
}
