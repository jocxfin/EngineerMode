package com.google.zxing.qrcode.decoder;

public enum Mode {
    TERMINATOR(new int[]{0, 0, 0}, 0),
    NUMERIC(new int[]{10, 12, 14}, 1),
    ALPHANUMERIC(new int[]{9, 11, 13}, 2),
    STRUCTURED_APPEND(new int[]{0, 0, 0}, 3),
    BYTE(new int[]{8, 16, 16}, 4),
    ECI(new int[]{0, 0, 0}, 7),
    KANJI(new int[]{8, 10, 12}, 8),
    FNC1_FIRST_POSITION(new int[]{0, 0, 0}, 5),
    FNC1_SECOND_POSITION(new int[]{0, 0, 0}, 9),
    HANZI(new int[]{8, 10, 12}, 13);

    private final int bits;
    private final int[] characterCountBitsForVersions;

    private Mode(int[] characterCountBitsForVersions, int bits) {
        this.characterCountBitsForVersions = characterCountBitsForVersions;
        this.bits = bits;
    }

    public int getCharacterCountBits(Version version) {
        int offset;
        int number = version.getVersionNumber();
        if (number <= 9) {
            offset = 0;
        } else if (number > 26) {
            offset = 2;
        } else {
            offset = 1;
        }
        return this.characterCountBitsForVersions[offset];
    }

    public int getBits() {
        return this.bits;
    }
}
