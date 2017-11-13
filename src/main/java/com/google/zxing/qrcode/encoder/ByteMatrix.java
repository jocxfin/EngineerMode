package com.google.zxing.qrcode.encoder;

import com.android.engineeringmode.functions.Light;

import java.lang.reflect.Array;

public final class ByteMatrix {
    private final byte[][] bytes;
    private final int height;
    private final int width;

    public ByteMatrix(int width, int height) {
        this.bytes = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{height, width});
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public byte get(int x, int y) {
        return this.bytes[y][x];
    }

    public byte[][] getArray() {
        return this.bytes;
    }

    public void set(int x, int y, int value) {
        this.bytes[y][x] = (byte) ((byte) value);
    }

    public void set(int x, int y, boolean value) {
        int i = 0;
        byte[] bArr = this.bytes[y];
        if (value) {
            i = 1;
        }
        bArr[x] = (byte) ((byte) i);
    }

    public void clear(byte value) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.bytes[y][x] = (byte) value;
            }
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder(((this.width * 2) * this.height) + 2);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                switch (this.bytes[y][x]) {
                    case (byte) 0:
                        result.append(" 0");
                        break;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        result.append(" 1");
                        break;
                    default:
                        result.append("  ");
                        break;
                }
            }
            result.append('\n');
        }
        return result.toString();
    }
}
