package jxl.read.biff;

import jxl.JXLException;

public class BiffException extends JXLException {
    static final BiffMessage corruptFileFormat = new BiffMessage("The file format is corrupt");
    static final BiffMessage excelFileNotFound = new BiffMessage("The input file was not found");
    static final BiffMessage excelFileTooBig = new BiffMessage("Not all of the excel file could be read");
    static final BiffMessage expectedGlobals = new BiffMessage("Expected globals");
    static final BiffMessage passwordProtected = new BiffMessage("The workbook is password protected");
    static final BiffMessage streamNotFound = new BiffMessage("Compound file does not contain the specified stream");
    static final BiffMessage unrecognizedBiffVersion = new BiffMessage("Unrecognized biff version");
    static final BiffMessage unrecognizedOLEFile = new BiffMessage("Unable to recognize OLE stream");

    private static class BiffMessage {
        public String message;

        BiffMessage(String m) {
            this.message = m;
        }
    }

    public BiffException(BiffMessage m) {
        super(m.message);
    }
}
