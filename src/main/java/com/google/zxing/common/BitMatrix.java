package com.google.zxing.common;

public final class BitMatrix {
    private final int[] bits;
    private final int height;
    private final int rowSize;
    private final int width;

    public BitMatrix(int width, int height) {
        if (width >= 1 && height >= 1) {
            this.width = width;
            this.height = height;
            this.rowSize = (width + 31) >> 5;
            this.bits = new int[(this.rowSize * height)];
            return;
        }
        throw new IllegalArgumentException("Both dimensions must be greater than 0");
    }

    public boolean get(int x, int y) {
        if (((this.bits[(this.rowSize * y) + (x >> 5)] >>> (x & 31)) & 1) == 0) {
            return false;
        }
        return true;
    }

    public void setRegion(int left, int top, int width, int height) {
        if (top < 0 || left < 0) {
            throw new IllegalArgumentException("Left and top must be nonnegative");
        } else if (height >= 1 && width >= 1) {
            int right = left + width;
            int bottom = top + height;
            if (bottom <= this.height && right <= this.width) {
                for (int y = top; y < bottom; y++) {
                    int offset = y * this.rowSize;
                    for (int x = left; x < right; x++) {
                        int[] iArr = this.bits;
                        int i = (x >> 5) + offset;
                        iArr[i] = iArr[i] | (1 << (x & 31));
                    }
                }
                return;
            }
            throw new IllegalArgumentException("The region must fit inside the matrix");
        } else {
            throw new IllegalArgumentException("Height and width must be at least 1");
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof BitMatrix)) {
            return false;
        }
        BitMatrix other = (BitMatrix) o;
        if (this.width != other.width || this.height != other.height || this.rowSize != other.rowSize || this.bits.length != other.bits.length) {
            return false;
        }
        for (int i = 0; i < this.bits.length; i++) {
            if (this.bits[i] != other.bits[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = (((((this.width * 31) + this.width) * 31) + this.height) * 31) + this.rowSize;
        for (int bit : this.bits) {
            hash = (hash * 31) + bit;
        }
        return hash;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(this.height * (this.width + 1));
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                String str;
                if (get(x, y)) {
                    str = "X ";
                } else {
                    str = "  ";
                }
                result.append(str);
            }
            result.append('\n');
        }
        return result.toString();
    }
}
