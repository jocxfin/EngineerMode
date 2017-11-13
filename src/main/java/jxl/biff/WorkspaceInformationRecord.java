package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

public class WorkspaceInformationRecord extends WritableRecordData {
    private static Logger logger = Logger.getLogger(WorkspaceInformationRecord.class);
    private boolean columnOutlines;
    private boolean fitToPages;
    private boolean rowOutlines;
    private int wsoptions;

    public WorkspaceInformationRecord(Record t) {
        boolean z;
        boolean z2 = false;
        super(t);
        byte[] data = getRecord().getData();
        this.wsoptions = IntegerHelper.getInt(data[0], data[1]);
        if ((this.wsoptions | 256) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.fitToPages = z;
        if ((this.wsoptions | 1024) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.rowOutlines = z;
        if ((this.wsoptions | 2048) != 0) {
            z2 = true;
        }
        this.columnOutlines = z2;
    }

    public WorkspaceInformationRecord() {
        super(Type.WSBOOL);
        this.wsoptions = 1217;
    }

    public boolean getFitToPages() {
        return this.fitToPages;
    }

    public void setFitToPages(boolean b) {
        this.fitToPages = b;
    }

    public void setRowOutlines(boolean ro) {
        this.rowOutlines = true;
    }

    public void setColumnOutlines(boolean ro) {
        this.rowOutlines = true;
    }

    public byte[] getData() {
        byte[] data = new byte[2];
        if (this.fitToPages) {
            this.wsoptions |= 256;
        }
        if (this.rowOutlines) {
            this.wsoptions |= 1024;
        }
        if (this.columnOutlines) {
            this.wsoptions |= 2048;
        }
        IntegerHelper.getTwoBytes(this.wsoptions, data, 0);
        return data;
    }
}
