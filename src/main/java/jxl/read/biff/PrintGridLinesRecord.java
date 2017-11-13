package jxl.read.biff;

import jxl.biff.RecordData;

class PrintGridLinesRecord extends RecordData {
    private boolean printGridLines;

    public PrintGridLinesRecord(Record pgl) {
        boolean z = false;
        super(pgl);
        if (pgl.getData()[0] == (byte) 1) {
            z = true;
        }
        this.printGridLines = z;
    }

    public boolean getPrintGridLines() {
        return this.printGridLines;
    }
}
