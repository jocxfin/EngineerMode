package com.google.zxing.common;

public final class BitArray {
    private int[] bits = new int[1];
    private int size = 0;

    public int getSize() {
        return this.size;
    }

    public int getSizeInBytes() {
        return (this.size + 7) >> 3;
    }

    private void ensureCapacity(int size) {
        if (size > (this.bits.length << 5)) {
            int[] newBits = makeArray(size);
            System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
            this.bits = newBits;
        }
    }

    public boolean get(int i) {
        return (this.bits[i >> 5] & (1 << (i & 31))) != 0;
    }

    public void appendBit(boolean bit) {
        ensureCapacity(this.size + 1);
        if (bit) {
            int[] iArr = this.bits;
            int i = this.size >> 5;
            iArr[i] = iArr[i] | (1 << (this.size & 31));
        }
        this.size++;
    }

    public void appendBits(int value, int numBits) {
        if (numBits >= 0 && numBits <= 32) {
            ensureCapacity(this.size + numBits);
            for (int numBitsLeft = numBits; numBitsLeft > 0; numBitsLeft--) {
                boolean z;
                if (((value >> (numBitsLeft - 1)) & 1) != 1) {
                    z = false;
                } else {
                    z = true;
                }
                appendBit(z);
            }
            return;
        }
        throw new IllegalArgumentException("Num bits must be between 0 and 32");
    }

    public void appendBitArray(BitArray other) {
        int otherSize = other.size;
        ensureCapacity(this.size + otherSize);
        for (int i = 0; i < otherSize; i++) {
            appendBit(other.get(i));
        }
    }

    public void xor(BitArray other) {
        if (this.bits.length == other.bits.length) {
            for (int i = 0; i < this.bits.length; i++) {
                int[] iArr = this.bits;
                iArr[i] = iArr[i] ^ other.bits[i];
            }
            return;
        }
        throw new IllegalArgumentException("Sizes don't match");
    }

    public void toBytes(int bitOffset, byte[] array, int offset, int numBytes) {
        for (int i = 0; i < numBytes; i++) {
            int theByte = 0;
            for (int j = 0; j < 8; j++) {
                if (get(bitOffset)) {
                    theByte |= 1 << (7 - j);
                }
                bitOffset++;
            }
            array[offset + i] = (byte) ((byte) theByte);
        }
    }

    private static int[] makeArray(int size) {
        return new int[((size + 31) >> 5)];
    }

    public String toString() {
        StringBuilder result = new StringBuilder(this.size);
        for (int i = 0; i < this.size; i++) {
            char c;
            if ((i & 7) == 0) {
                result.append(' ');
            }
            if (get(i)) {
                c = 'X';
            } else {
                c = '.';
            }
            result.append(c);
        }
        return result.toString();
    }
}
