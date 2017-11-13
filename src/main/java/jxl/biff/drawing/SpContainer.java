package jxl.biff.drawing;

class SpContainer extends EscherContainer {
    public SpContainer() {
        super(EscherRecordType.SP_CONTAINER);
    }

    public SpContainer(EscherRecordData erd) {
        super(erd);
    }
}
