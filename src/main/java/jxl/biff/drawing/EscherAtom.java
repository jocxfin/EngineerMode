package jxl.biff.drawing;

import jxl.common.Logger;

class EscherAtom extends EscherRecord {
    private static Logger logger = Logger.getLogger(EscherAtom.class);

    public EscherAtom(EscherRecordData erd) {
        super(erd);
    }

    protected EscherAtom(EscherRecordType type) {
        super(type);
    }

    byte[] getData() {
        logger.warn("escher atom getData called on object of type " + getClass().getName() + " code " + Integer.toString(getType().getValue(), 16));
        return null;
    }
}
