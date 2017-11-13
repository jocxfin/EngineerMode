package jxl.biff.drawing;

import jxl.common.Logger;

class ClientTextBox extends EscherAtom {
    private static Logger logger = Logger.getLogger(ClientTextBox.class);
    private byte[] data;

    public ClientTextBox(EscherRecordData erd) {
        super(erd);
    }

    public ClientTextBox() {
        super(EscherRecordType.CLIENT_TEXT_BOX);
    }

    byte[] getData() {
        this.data = new byte[0];
        return setHeaderData(this.data);
    }
}
