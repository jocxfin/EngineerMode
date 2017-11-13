package jxl.biff.drawing;

import jxl.common.Logger;

class ClientData extends EscherAtom {
    private static Logger logger = Logger.getLogger(ClientData.class);
    private byte[] data;

    public ClientData(EscherRecordData erd) {
        super(erd);
    }

    public ClientData() {
        super(EscherRecordType.CLIENT_DATA);
    }

    byte[] getData() {
        this.data = new byte[0];
        return setHeaderData(this.data);
    }
}
