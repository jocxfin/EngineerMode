package jxl.biff.drawing;

import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class ClientAnchor extends EscherAtom {
    private static final Logger logger = Logger.getLogger(ClientAnchor.class);
    private byte[] data;
    private int properties;
    private double x1;
    private double x2;
    private double y1;
    private double y2;

    public ClientAnchor(EscherRecordData erd) {
        super(erd);
        byte[] bytes = getBytes();
        this.properties = IntegerHelper.getInt(bytes[0], bytes[1]);
        this.x1 = ((double) IntegerHelper.getInt(bytes[2], bytes[3])) + (((double) IntegerHelper.getInt(bytes[4], bytes[5])) / 1024.0d);
        this.y1 = ((double) IntegerHelper.getInt(bytes[6], bytes[7])) + (((double) IntegerHelper.getInt(bytes[8], bytes[9])) / 256.0d);
        this.x2 = ((double) IntegerHelper.getInt(bytes[10], bytes[11])) + (((double) IntegerHelper.getInt(bytes[12], bytes[13])) / 1024.0d);
        this.y2 = ((double) IntegerHelper.getInt(bytes[14], bytes[15])) + (((double) IntegerHelper.getInt(bytes[16], bytes[17])) / 256.0d);
    }

    public ClientAnchor(double x1, double y1, double x2, double y2, int props) {
        super(EscherRecordType.CLIENT_ANCHOR);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.properties = props;
    }

    byte[] getData() {
        this.data = new byte[18];
        IntegerHelper.getTwoBytes(this.properties, this.data, 0);
        IntegerHelper.getTwoBytes((int) this.x1, this.data, 2);
        IntegerHelper.getTwoBytes((int) ((this.x1 - ((double) ((int) this.x1))) * 1024.0d), this.data, 4);
        IntegerHelper.getTwoBytes((int) this.y1, this.data, 6);
        IntegerHelper.getTwoBytes((int) ((this.y1 - ((double) ((int) this.y1))) * 256.0d), this.data, 8);
        IntegerHelper.getTwoBytes((int) this.x2, this.data, 10);
        IntegerHelper.getTwoBytes((int) ((this.x2 - ((double) ((int) this.x2))) * 1024.0d), this.data, 12);
        IntegerHelper.getTwoBytes((int) this.y2, this.data, 14);
        IntegerHelper.getTwoBytes((int) ((this.y2 - ((double) ((int) this.y2))) * 256.0d), this.data, 16);
        return setHeaderData(this.data);
    }

    double getX1() {
        return this.x1;
    }

    double getY1() {
        return this.y1;
    }

    double getX2() {
        return this.x2;
    }

    double getY2() {
        return this.y2;
    }

    int getProperties() {
        return this.properties;
    }
}
