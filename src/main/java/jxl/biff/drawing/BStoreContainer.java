package jxl.biff.drawing;

import jxl.common.Logger;

class BStoreContainer extends EscherContainer {
    private static Logger logger = Logger.getLogger(BStoreContainer.class);
    private int numBlips;

    public BStoreContainer(EscherRecordData erd) {
        super(erd);
        this.numBlips = getInstance();
    }

    public BStoreContainer() {
        super(EscherRecordType.BSTORE_CONTAINER);
    }

    void setNumBlips(int count) {
        this.numBlips = count;
        setInstance(this.numBlips);
    }

    public int getNumBlips() {
        return this.numBlips;
    }
}
