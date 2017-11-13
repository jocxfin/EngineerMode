package jxl.biff.drawing;

import jxl.common.Logger;

class SpgrContainer extends EscherContainer {
    private static final Logger logger = Logger.getLogger(SpgrContainer.class);

    public SpgrContainer() {
        super(EscherRecordType.SPGR_CONTAINER);
    }

    public SpgrContainer(EscherRecordData erd) {
        super(erd);
    }
}
