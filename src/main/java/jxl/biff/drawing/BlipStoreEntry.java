package jxl.biff.drawing;

import java.io.IOException;

import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class BlipStoreEntry extends EscherAtom {
    private static Logger logger = Logger.getLogger(BlipStoreEntry.class);
    private byte[] data;
    private int imageDataLength;
    private int referenceCount;
    private BlipType type;
    private boolean write;

    public BlipStoreEntry(EscherRecordData erd) {
        super(erd);
        this.type = BlipType.getType(getInstance());
        this.write = false;
        byte[] bytes = getBytes();
        this.referenceCount = IntegerHelper.getInt(bytes[24], bytes[25], bytes[26], bytes[27]);
    }

    public BlipStoreEntry(Drawing d) throws IOException {
        super(EscherRecordType.BSE);
        this.type = BlipType.PNG;
        setVersion(2);
        setInstance(this.type.getValue());
        byte[] imageData = d.getImageBytes();
        this.imageDataLength = imageData.length;
        this.data = new byte[(this.imageDataLength + 61)];
        System.arraycopy(imageData, 0, this.data, 61, this.imageDataLength);
        this.referenceCount = d.getReferenceCount();
        this.write = true;
    }

    public byte[] getData() {
        if (this.write) {
            this.data[0] = (byte) ((byte) this.type.getValue());
            this.data[1] = (byte) ((byte) this.type.getValue());
            IntegerHelper.getFourBytes((this.imageDataLength + 8) + 17, this.data, 20);
            IntegerHelper.getFourBytes(this.referenceCount, this.data, 24);
            IntegerHelper.getFourBytes(0, this.data, 28);
            this.data[32] = (byte) 0;
            this.data[33] = (byte) 0;
            this.data[34] = (byte) 126;
            this.data[35] = (byte) 1;
            this.data[36] = (byte) 0;
            this.data[37] = (byte) 110;
            IntegerHelper.getTwoBytes(61470, this.data, 38);
            IntegerHelper.getFourBytes(this.imageDataLength + 17, this.data, 40);
        } else {
            this.data = getBytes();
        }
        return setHeaderData(this.data);
    }

    byte[] getImageData() {
        byte[] allData = getBytes();
        byte[] imageData = new byte[(allData.length - 61)];
        System.arraycopy(allData, 61, imageData, 0, imageData.length);
        return imageData;
    }
}
