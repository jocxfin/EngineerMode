package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class BOFRecord extends WritableRecordData {
    public static final SheetBOF sheet = new SheetBOF();
    public static final WorkbookGlobalsBOF workbookGlobals = new WorkbookGlobalsBOF();
    private byte[] data = new byte[]{(byte) 0, (byte) 6, (byte) 16, (byte) 0, (byte) -14, (byte) 21, (byte) -52, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0};

    private static class SheetBOF {
        private SheetBOF() {
        }
    }

    private static class WorkbookGlobalsBOF {
        private WorkbookGlobalsBOF() {
        }
    }

    public BOFRecord(WorkbookGlobalsBOF dummy) {
        super(Type.BOF);
    }

    public BOFRecord(SheetBOF dummy) {
        super(Type.BOF);
    }

    public byte[] getData() {
        return this.data;
    }
}
