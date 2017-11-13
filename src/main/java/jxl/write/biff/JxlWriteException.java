package jxl.write.biff;

import jxl.write.WriteException;

public class JxlWriteException extends WriteException {
    static WriteMessage cellReferenced = new WriteMessage("Cell has already been added to a worksheet");
    static WriteMessage copyPropertySets = new WriteMessage("Error encounted when copying additional property sets");
    static WriteMessage formatInitialized = new WriteMessage("Attempt to modify a referenced format");
    static WriteMessage maxColumnsExceeded = new WriteMessage("The maximum number of columns permitted on a worksheet has been exceeded");
    static WriteMessage maxRowsExceeded = new WriteMessage("The maximum number of rows permitted on a worksheet been exceeded");

    private static class WriteMessage {
        public String message;

        WriteMessage(String m) {
            this.message = m;
        }
    }

    public JxlWriteException(WriteMessage m) {
        super(m.message);
    }
}
