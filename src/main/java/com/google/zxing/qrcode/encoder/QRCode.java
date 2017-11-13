package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;

public final class QRCode {
    private ErrorCorrectionLevel ecLevel = null;
    private int maskPattern = -1;
    private ByteMatrix matrix = null;
    private int matrixWidth = -1;
    private Mode mode = null;
    private int numDataBytes = -1;
    private int numECBytes = -1;
    private int numRSBlocks = -1;
    private int numTotalBytes = -1;
    private int version = -1;

    public int getVersion() {
        return this.version;
    }

    public int getMatrixWidth() {
        return this.matrixWidth;
    }

    public int getMaskPattern() {
        return this.maskPattern;
    }

    public int getNumTotalBytes() {
        return this.numTotalBytes;
    }

    public int getNumDataBytes() {
        return this.numDataBytes;
    }

    public int getNumRSBlocks() {
        return this.numRSBlocks;
    }

    public ByteMatrix getMatrix() {
        return this.matrix;
    }

    public boolean isValid() {
        return (this.mode == null || this.ecLevel == null || this.version == -1 || this.matrixWidth == -1 || this.maskPattern == -1 || this.numTotalBytes == -1 || this.numDataBytes == -1 || this.numECBytes == -1 || this.numRSBlocks == -1 || !isValidMaskPattern(this.maskPattern) || this.numTotalBytes != this.numDataBytes + this.numECBytes || this.matrix == null || this.matrixWidth != this.matrix.getWidth() || this.matrix.getWidth() != this.matrix.getHeight()) ? false : true;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(200);
        result.append("<<\n");
        result.append(" mode: ");
        result.append(this.mode);
        result.append("\n ecLevel: ");
        result.append(this.ecLevel);
        result.append("\n version: ");
        result.append(this.version);
        result.append("\n matrixWidth: ");
        result.append(this.matrixWidth);
        result.append("\n maskPattern: ");
        result.append(this.maskPattern);
        result.append("\n numTotalBytes: ");
        result.append(this.numTotalBytes);
        result.append("\n numDataBytes: ");
        result.append(this.numDataBytes);
        result.append("\n numECBytes: ");
        result.append(this.numECBytes);
        result.append("\n numRSBlocks: ");
        result.append(this.numRSBlocks);
        if (this.matrix != null) {
            result.append("\n matrix:\n");
            result.append(this.matrix.toString());
        } else {
            result.append("\n matrix: null\n");
        }
        result.append(">>\n");
        return result.toString();
    }

    public void setMode(Mode value) {
        this.mode = value;
    }

    public void setECLevel(ErrorCorrectionLevel value) {
        this.ecLevel = value;
    }

    public void setVersion(int value) {
        this.version = value;
    }

    public void setMatrixWidth(int value) {
        this.matrixWidth = value;
    }

    public void setMaskPattern(int value) {
        this.maskPattern = value;
    }

    public void setNumTotalBytes(int value) {
        this.numTotalBytes = value;
    }

    public void setNumDataBytes(int value) {
        this.numDataBytes = value;
    }

    public void setNumECBytes(int value) {
        this.numECBytes = value;
    }

    public void setNumRSBlocks(int value) {
        this.numRSBlocks = value;
    }

    public void setMatrix(ByteMatrix value) {
        this.matrix = value;
    }

    public static boolean isValidMaskPattern(int maskPattern) {
        return maskPattern >= 0 && maskPattern < 8;
    }
}
