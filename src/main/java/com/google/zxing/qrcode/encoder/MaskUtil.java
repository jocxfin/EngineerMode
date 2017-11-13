package com.google.zxing.qrcode.encoder;

import com.android.engineeringmode.functions.Light;

final class MaskUtil {
    private MaskUtil() {
    }

    static int applyMaskPenaltyRule1(ByteMatrix matrix) {
        return applyMaskPenaltyRule1Internal(matrix, true) + applyMaskPenaltyRule1Internal(matrix, false);
    }

    static int applyMaskPenaltyRule2(ByteMatrix matrix) {
        int penalty = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int y = 0;
        while (y < height - 1) {
            int x = 0;
            while (x < width - 1) {
                byte value = array[y][x];
                if (value == array[y][x + 1] && value == array[y + 1][x] && value == array[y + 1][x + 1]) {
                    penalty += 3;
                }
                x++;
            }
            y++;
        }
        return penalty;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int applyMaskPenaltyRule3(com.google.zxing.qrcode.encoder.ByteMatrix r9) {
        /*
        r8 = 1;
        r2 = 0;
        r0 = r9.getArray();
        r3 = r9.getWidth();
        r1 = r9.getHeight();
        r5 = 0;
    L_0x000f:
        if (r5 < r1) goto L_0x0012;
    L_0x0011:
        return r2;
    L_0x0012:
        r4 = 0;
    L_0x0013:
        if (r4 < r3) goto L_0x0018;
    L_0x0015:
        r5 = r5 + 1;
        goto L_0x000f;
    L_0x0018:
        r6 = r4 + 6;
        if (r6 < r3) goto L_0x0023;
    L_0x001c:
        r6 = r5 + 6;
        if (r6 < r1) goto L_0x00a5;
    L_0x0020:
        r4 = r4 + 1;
        goto L_0x0013;
    L_0x0023:
        r6 = r0[r5];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x001c;
    L_0x0029:
        r6 = r0[r5];
        r7 = r4 + 1;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0031:
        r6 = r0[r5];
        r7 = r4 + 2;
        r6 = r6[r7];
        if (r6 != r8) goto L_0x001c;
    L_0x0039:
        r6 = r0[r5];
        r7 = r4 + 3;
        r6 = r6[r7];
        if (r6 != r8) goto L_0x001c;
    L_0x0041:
        r6 = r0[r5];
        r7 = r4 + 4;
        r6 = r6[r7];
        if (r6 != r8) goto L_0x001c;
    L_0x0049:
        r6 = r0[r5];
        r7 = r4 + 5;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0051:
        r6 = r0[r5];
        r7 = r4 + 6;
        r6 = r6[r7];
        if (r6 != r8) goto L_0x001c;
    L_0x0059:
        r6 = r4 + 10;
        if (r6 < r3) goto L_0x0084;
    L_0x005d:
        r6 = r4 + -4;
        if (r6 < 0) goto L_0x001c;
    L_0x0061:
        r6 = r0[r5];
        r7 = r4 + -1;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0069:
        r6 = r0[r5];
        r7 = r4 + -2;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0071:
        r6 = r0[r5];
        r7 = r4 + -3;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0079:
        r6 = r0[r5];
        r7 = r4 + -4;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x001c;
    L_0x0081:
        r2 = r2 + 40;
        goto L_0x001c;
    L_0x0084:
        r6 = r0[r5];
        r7 = r4 + 7;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x005d;
    L_0x008c:
        r6 = r0[r5];
        r7 = r4 + 8;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x005d;
    L_0x0094:
        r6 = r0[r5];
        r7 = r4 + 9;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x005d;
    L_0x009c:
        r6 = r0[r5];
        r7 = r4 + 10;
        r6 = r6[r7];
        if (r6 != 0) goto L_0x005d;
    L_0x00a4:
        goto L_0x0081;
    L_0x00a5:
        r6 = r0[r5];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x0020;
    L_0x00ab:
        r6 = r5 + 1;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x00b3:
        r6 = r5 + 2;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x0020;
    L_0x00bb:
        r6 = r5 + 3;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x0020;
    L_0x00c3:
        r6 = r5 + 4;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x0020;
    L_0x00cb:
        r6 = r5 + 5;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x00d3:
        r6 = r5 + 6;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != r8) goto L_0x0020;
    L_0x00db:
        r6 = r5 + 10;
        if (r6 < r1) goto L_0x0107;
    L_0x00df:
        r6 = r5 + -4;
        if (r6 < 0) goto L_0x0020;
    L_0x00e3:
        r6 = r5 + -1;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x00eb:
        r6 = r5 + -2;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x00f3:
        r6 = r5 + -3;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x00fb:
        r6 = r5 + -4;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x0020;
    L_0x0103:
        r2 = r2 + 40;
        goto L_0x0020;
    L_0x0107:
        r6 = r5 + 7;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x00df;
    L_0x010f:
        r6 = r5 + 8;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x00df;
    L_0x0117:
        r6 = r5 + 9;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x00df;
    L_0x011f:
        r6 = r5 + 10;
        r6 = r0[r6];
        r6 = r6[r4];
        if (r6 != 0) goto L_0x00df;
    L_0x0127:
        goto L_0x0103;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.MaskUtil.applyMaskPenaltyRule3(com.google.zxing.qrcode.encoder.ByteMatrix):int");
    }

    static int applyMaskPenaltyRule4(ByteMatrix matrix) {
        int numDarkCells = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (array[y][x] == (byte) 1) {
                    numDarkCells++;
                }
            }
        }
        return (Math.abs((int) ((100.0d * (((double) numDarkCells) / ((double) (matrix.getHeight() * matrix.getWidth())))) - 50.0d)) / 5) * 10;
    }

    static boolean getDataMaskBit(int maskPattern, int x, int y) {
        if (QRCode.isValidMaskPattern(maskPattern)) {
            int intermediate;
            int temp;
            switch (maskPattern) {
                case 0:
                    intermediate = (y + x) & 1;
                    break;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    intermediate = y & 1;
                    break;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    intermediate = x % 3;
                    break;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    intermediate = (y + x) % 3;
                    break;
                case 4:
                    intermediate = ((y >>> 1) + (x / 3)) & 1;
                    break;
                case 5:
                    temp = y * x;
                    intermediate = (temp & 1) + (temp % 3);
                    break;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    temp = y * x;
                    intermediate = ((temp & 1) + (temp % 3)) & 1;
                    break;
                case 7:
                    intermediate = (((y * x) % 3) + ((y + x) & 1)) & 1;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid mask pattern: " + maskPattern);
            }
            if (intermediate != 0) {
                return false;
            }
            return true;
        }
        throw new IllegalArgumentException("Invalid mask pattern");
    }

    private static int applyMaskPenaltyRule1Internal(ByteMatrix matrix, boolean isHorizontal) {
        int penalty = 0;
        int numSameBitCells = 0;
        int prevBit = -1;
        int iLimit = !isHorizontal ? matrix.getWidth() : matrix.getHeight();
        int jLimit = !isHorizontal ? matrix.getHeight() : matrix.getWidth();
        byte[][] array = matrix.getArray();
        int i = 0;
        while (i < iLimit) {
            int j = 0;
            while (j < jLimit) {
                int bit = !isHorizontal ? array[j][i] : array[i][j];
                if (bit != prevBit) {
                    numSameBitCells = 1;
                    prevBit = bit;
                } else {
                    numSameBitCells++;
                    if (numSameBitCells == 5) {
                        penalty += 3;
                    } else if (numSameBitCells > 5) {
                        penalty++;
                    }
                }
                j++;
            }
            numSameBitCells = 0;
            i++;
        }
        return penalty;
    }
}
