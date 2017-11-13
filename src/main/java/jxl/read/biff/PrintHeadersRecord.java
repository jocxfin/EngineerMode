package jxl.read.biff;

import jxl.biff.RecordData;

class PrintHeadersRecord extends RecordData {
    private boolean printHeaders;

    public PrintHeadersRecord(Record ph) {
        boolean z = false;
        super(ph);
        if (ph.getData()[0] == (byte) 1) {
            z = true;
        }
        this.printHeaders = z;
    }

    public boolean getPrintHeaders() {
        return this.printHeaders;
    }
}
