package com.google.zxing.qrcode.encoder;

import com.android.engineeringmode.functions.Light;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.decoder.Version.ECBlocks;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class Encoder {
    private static final int[] ALPHANUMERIC_TABLE = null;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$qrcode$decoder$Mode = null;

        static {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.google.zxing.qrcode.encoder.Encoder.1.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
	at jadx.core.dex.nodes.MethodNode.addJump(MethodNode.java:370)
	at jadx.core.dex.nodes.MethodNode.initJumps(MethodNode.java:360)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:106)
	... 8 more
*/
            /*
            r0 = com.google.zxing.qrcode.decoder.Mode.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$google$zxing$qrcode$decoder$Mode = r0;
            r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode;	 Catch:{ NoSuchFieldError -> 0x0034 }
            r1 = com.google.zxing.qrcode.decoder.Mode.NUMERIC;	 Catch:{ NoSuchFieldError -> 0x0034 }
            r2 = 1;	 Catch:{ NoSuchFieldError -> 0x0034 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0034 }
            r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode;	 Catch:{ NoSuchFieldError -> 0x0032 }
            r1 = com.google.zxing.qrcode.decoder.Mode.ALPHANUMERIC;	 Catch:{ NoSuchFieldError -> 0x0032 }
            r2 = 2;	 Catch:{ NoSuchFieldError -> 0x0032 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0032 }
            r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode;	 Catch:{ NoSuchFieldError -> 0x0030 }
            r1 = com.google.zxing.qrcode.decoder.Mode.BYTE;	 Catch:{ NoSuchFieldError -> 0x0030 }
            r2 = 3;	 Catch:{ NoSuchFieldError -> 0x0030 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0030 }
            r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode;	 Catch:{ NoSuchFieldError -> 0x002e }
            r1 = com.google.zxing.qrcode.decoder.Mode.KANJI;	 Catch:{ NoSuchFieldError -> 0x002e }
            r2 = 4;	 Catch:{ NoSuchFieldError -> 0x002e }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002e }
        L_0x002e:
            r0 = move-exception;
            goto L_0x002d;
        L_0x0030:
            r0 = move-exception;
            goto L_0x0024;
        L_0x0032:
            r0 = move-exception;
            goto L_0x001b;
        L_0x0034:
            r0 = move-exception;
            goto L_0x0012;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.Encoder.1.<clinit>():void");
        }
    }

    static {
        ALPHANUMERIC_TABLE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
    }

    private Encoder() {
    }

    private static int calculateMaskPenalty(ByteMatrix matrix) {
        return (((MaskUtil.applyMaskPenaltyRule1(matrix) + 0) + MaskUtil.applyMaskPenaltyRule2(matrix)) + MaskUtil.applyMaskPenaltyRule3(matrix)) + MaskUtil.applyMaskPenaltyRule4(matrix);
    }

    public static void encode(String content, ErrorCorrectionLevel ecLevel, Map<EncodeHintType, ?> hints, QRCode qrCode) throws WriterException {
        String encoding = null;
        if (hints != null) {
            encoding = (String) hints.get(EncodeHintType.CHARACTER_SET);
        }
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        Mode mode = chooseMode(content, encoding);
        BitArray dataBits = new BitArray();
        appendBytes(content, mode, dataBits, encoding);
        initQRCode(dataBits.getSize(), ecLevel, mode, qrCode);
        BitArray headerAndDataBits = new BitArray();
        if (mode == Mode.BYTE && !"ISO-8859-1".equals(encoding)) {
            CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(encoding);
            if (eci != null) {
                appendECI(eci, headerAndDataBits);
            }
        }
        appendModeInfo(mode, headerAndDataBits);
        appendLengthInfo(mode != Mode.BYTE ? content.length() : dataBits.getSizeInBytes(), qrCode.getVersion(), mode, headerAndDataBits);
        headerAndDataBits.appendBitArray(dataBits);
        terminateBits(qrCode.getNumDataBytes(), headerAndDataBits);
        BitArray finalBits = new BitArray();
        interleaveWithECBytes(headerAndDataBits, qrCode.getNumTotalBytes(), qrCode.getNumDataBytes(), qrCode.getNumRSBlocks(), finalBits);
        ByteMatrix matrix = new ByteMatrix(qrCode.getMatrixWidth(), qrCode.getMatrixWidth());
        qrCode.setMaskPattern(chooseMaskPattern(finalBits, ecLevel, qrCode.getVersion(), matrix));
        MatrixUtil.buildMatrix(finalBits, ecLevel, qrCode.getVersion(), qrCode.getMaskPattern(), matrix);
        qrCode.setMatrix(matrix);
        if (!qrCode.isValid()) {
            throw new WriterException("Invalid QR code: " + qrCode.toString());
        }
    }

    static int getAlphanumericCode(int code) {
        if (code >= ALPHANUMERIC_TABLE.length) {
            return -1;
        }
        return ALPHANUMERIC_TABLE[code];
    }

    private static Mode chooseMode(String content, String encoding) {
        if ("Shift_JIS".equals(encoding)) {
            return !isOnlyDoubleByteKanji(content) ? Mode.BYTE : Mode.KANJI;
        }
        boolean hasNumeric = false;
        boolean hasAlphanumeric = false;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c >= '0' && c <= '9') {
                hasNumeric = true;
            } else if (getAlphanumericCode(c) == -1) {
                return Mode.BYTE;
            } else {
                hasAlphanumeric = true;
            }
        }
        if (hasAlphanumeric) {
            return Mode.ALPHANUMERIC;
        }
        if (hasNumeric) {
            return Mode.NUMERIC;
        }
        return Mode.BYTE;
    }

    private static boolean isOnlyDoubleByteKanji(String content) {
        try {
            byte[] bytes = content.getBytes("Shift_JIS");
            int length = bytes.length;
            if (length % 2 != 0) {
                return false;
            }
            for (int i = 0; i < length; i += 2) {
                int byte1 = bytes[i] & Light.MAIN_KEY_MAX;
                if (byte1 < 129 || byte1 > 159) {
                    if (byte1 < 224 || byte1 > 235) {
                        return false;
                    }
                }
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    private static int chooseMaskPattern(BitArray bits, ErrorCorrectionLevel ecLevel, int version, ByteMatrix matrix) throws WriterException {
        int minPenalty = Integer.MAX_VALUE;
        int bestMaskPattern = -1;
        for (int maskPattern = 0; maskPattern < 8; maskPattern++) {
            MatrixUtil.buildMatrix(bits, ecLevel, version, maskPattern, matrix);
            int penalty = calculateMaskPenalty(matrix);
            if (penalty < minPenalty) {
                minPenalty = penalty;
                bestMaskPattern = maskPattern;
            }
        }
        return bestMaskPattern;
    }

    private static void initQRCode(int numInputBits, ErrorCorrectionLevel ecLevel, Mode mode, QRCode qrCode) throws WriterException {
        qrCode.setECLevel(ecLevel);
        qrCode.setMode(mode);
        int versionNum = 1;
        while (versionNum <= 40) {
            Version version = Version.getVersionForNumber(versionNum);
            int numBytes = version.getTotalCodewords();
            ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
            int numEcBytes = ecBlocks.getTotalECCodewords();
            int numRSBlocks = ecBlocks.getNumBlocks();
            int numDataBytes = numBytes - numEcBytes;
            if (numDataBytes < getTotalInputBytes(numInputBits, version, mode)) {
                versionNum++;
            } else {
                qrCode.setVersion(versionNum);
                qrCode.setNumTotalBytes(numBytes);
                qrCode.setNumDataBytes(numDataBytes);
                qrCode.setNumRSBlocks(numRSBlocks);
                qrCode.setNumECBytes(numEcBytes);
                qrCode.setMatrixWidth(version.getDimensionForVersion());
                return;
            }
        }
        throw new WriterException("Cannot find proper rs block info (input data too big?)");
    }

    private static int getTotalInputBytes(int numInputBits, Version version, Mode mode) {
        return ((numInputBits + (mode.getCharacterCountBits(version) + 4)) + 7) / 8;
    }

    static void terminateBits(int numDataBytes, BitArray bits) throws WriterException {
        int capacity = numDataBytes << 3;
        if (bits.getSize() <= capacity) {
            int i;
            for (i = 0; i < 4 && bits.getSize() < capacity; i++) {
                bits.appendBit(false);
            }
            int numBitsInLastByte = bits.getSize() & 7;
            if (numBitsInLastByte > 0) {
                for (i = numBitsInLastByte; i < 8; i++) {
                    bits.appendBit(false);
                }
            }
            int numPaddingBytes = numDataBytes - bits.getSizeInBytes();
            for (i = 0; i < numPaddingBytes; i++) {
                int i2;
                if ((i & 1) != 0) {
                    i2 = 17;
                } else {
                    i2 = 236;
                }
                bits.appendBits(i2, 8);
            }
            if (bits.getSize() != capacity) {
                throw new WriterException("Bits size does not equal capacity");
            }
            return;
        }
        throw new WriterException("data bits cannot fit in the QR Code" + bits.getSize() + " > " + capacity);
    }

    static void getNumDataBytesAndNumECBytesForBlockID(int numTotalBytes, int numDataBytes, int numRSBlocks, int blockID, int[] numDataBytesInBlock, int[] numECBytesInBlock) throws WriterException {
        if (blockID < numRSBlocks) {
            int numRsBlocksInGroup2 = numTotalBytes % numRSBlocks;
            int numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2;
            int numTotalBytesInGroup1 = numTotalBytes / numRSBlocks;
            int numDataBytesInGroup1 = numDataBytes / numRSBlocks;
            int numDataBytesInGroup2 = numDataBytesInGroup1 + 1;
            int numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1;
            int numEcBytesInGroup2 = (numTotalBytesInGroup1 + 1) - numDataBytesInGroup2;
            if (numEcBytesInGroup1 != numEcBytesInGroup2) {
                throw new WriterException("EC bytes mismatch");
            } else if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
                throw new WriterException("RS blocks mismatch");
            } else if (numTotalBytes != ((numDataBytesInGroup1 + numEcBytesInGroup1) * numRsBlocksInGroup1) + ((numDataBytesInGroup2 + numEcBytesInGroup2) * numRsBlocksInGroup2)) {
                throw new WriterException("Total bytes mismatch");
            } else if (blockID >= numRsBlocksInGroup1) {
                numDataBytesInBlock[0] = numDataBytesInGroup2;
                numECBytesInBlock[0] = numEcBytesInGroup2;
                return;
            } else {
                numDataBytesInBlock[0] = numDataBytesInGroup1;
                numECBytesInBlock[0] = numEcBytesInGroup1;
                return;
            }
        }
        throw new WriterException("Block ID too large");
    }

    static void interleaveWithECBytes(BitArray bits, int numTotalBytes, int numDataBytes, int numRSBlocks, BitArray result) throws WriterException {
        if (bits.getSizeInBytes() == numDataBytes) {
            int i;
            byte[] dataBytes;
            byte[] ecBytes;
            int dataBytesOffset = 0;
            int maxNumDataBytes = 0;
            int maxNumEcBytes = 0;
            Collection<BlockPair> blocks = new ArrayList(numRSBlocks);
            for (i = 0; i < numRSBlocks; i++) {
                int[] numDataBytesInBlock = new int[1];
                int[] numEcBytesInBlock = new int[1];
                getNumDataBytesAndNumECBytesForBlockID(numTotalBytes, numDataBytes, numRSBlocks, i, numDataBytesInBlock, numEcBytesInBlock);
                int size = numDataBytesInBlock[0];
                dataBytes = new byte[size];
                bits.toBytes(dataBytesOffset * 8, dataBytes, 0, size);
                ecBytes = generateECBytes(dataBytes, numEcBytesInBlock[0]);
                blocks.add(new BlockPair(dataBytes, ecBytes));
                maxNumDataBytes = Math.max(maxNumDataBytes, size);
                maxNumEcBytes = Math.max(maxNumEcBytes, ecBytes.length);
                dataBytesOffset += numDataBytesInBlock[0];
            }
            if (numDataBytes == dataBytesOffset) {
                for (i = 0; i < maxNumDataBytes; i++) {
                    for (BlockPair block : blocks) {
                        dataBytes = block.getDataBytes();
                        if (i < dataBytes.length) {
                            result.appendBits(dataBytes[i], 8);
                        }
                    }
                }
                for (i = 0; i < maxNumEcBytes; i++) {
                    for (BlockPair block2 : blocks) {
                        ecBytes = block2.getErrorCorrectionBytes();
                        if (i < ecBytes.length) {
                            result.appendBits(ecBytes[i], 8);
                        }
                    }
                }
                if (numTotalBytes != result.getSizeInBytes()) {
                    throw new WriterException("Interleaving error: " + numTotalBytes + " and " + result.getSizeInBytes() + " differ.");
                }
                return;
            }
            throw new WriterException("Data bytes does not match offset");
        }
        throw new WriterException("Number of bits and data bytes does not match");
    }

    static byte[] generateECBytes(byte[] dataBytes, int numEcBytesInBlock) {
        int i;
        int numDataBytes = dataBytes.length;
        int[] toEncode = new int[(numDataBytes + numEcBytesInBlock)];
        for (i = 0; i < numDataBytes; i++) {
            toEncode[i] = dataBytes[i] & Light.MAIN_KEY_MAX;
        }
        new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(toEncode, numEcBytesInBlock);
        byte[] ecBytes = new byte[numEcBytesInBlock];
        for (i = 0; i < numEcBytesInBlock; i++) {
            ecBytes[i] = (byte) ((byte) toEncode[numDataBytes + i]);
        }
        return ecBytes;
    }

    static void appendModeInfo(Mode mode, BitArray bits) {
        bits.appendBits(mode.getBits(), 4);
    }

    static void appendLengthInfo(int numLetters, int version, Mode mode, BitArray bits) throws WriterException {
        int numBits = mode.getCharacterCountBits(Version.getVersionForNumber(version));
        if (numLetters <= (1 << numBits) - 1) {
            bits.appendBits(numLetters, numBits);
            return;
        }
        throw new WriterException(numLetters + "is bigger than" + ((1 << numBits) - 1));
    }

    static void appendBytes(String content, Mode mode, BitArray bits, String encoding) throws WriterException {
        switch (AnonymousClass1.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[mode.ordinal()]) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                appendNumericBytes(content, bits);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                appendAlphanumericBytes(content, bits);
                return;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                append8BitBytes(content, bits, encoding);
                return;
            case 4:
                appendKanjiBytes(content, bits);
                return;
            default:
                throw new WriterException("Invalid mode: " + mode);
        }
    }

    static void appendNumericBytes(CharSequence content, BitArray bits) {
        int length = content.length();
        int i = 0;
        while (i < length) {
            int num1 = content.charAt(i) - 48;
            if (i + 2 < length) {
                int num3 = content.charAt(i + 2) - 48;
                bits.appendBits(((num1 * 100) + ((content.charAt(i + 1) - 48) * 10)) + num3, 10);
                i += 3;
            } else if (i + 1 >= length) {
                bits.appendBits(num1, 4);
                i++;
            } else {
                bits.appendBits((num1 * 10) + (content.charAt(i + 1) - 48), 7);
                i += 2;
            }
        }
    }

    static void appendAlphanumericBytes(CharSequence content, BitArray bits) throws WriterException {
        int length = content.length();
        int i = 0;
        while (i < length) {
            int code1 = getAlphanumericCode(content.charAt(i));
            if (code1 == -1) {
                throw new WriterException();
            } else if (i + 1 >= length) {
                bits.appendBits(code1, 6);
                i++;
            } else {
                int code2 = getAlphanumericCode(content.charAt(i + 1));
                if (code2 != -1) {
                    bits.appendBits((code1 * 45) + code2, 11);
                    i += 2;
                } else {
                    throw new WriterException();
                }
            }
        }
    }

    static void append8BitBytes(String content, BitArray bits, String encoding) throws WriterException {
        try {
            for (byte b : content.getBytes(encoding)) {
                bits.appendBits(b, 8);
            }
        } catch (UnsupportedEncodingException uee) {
            throw new WriterException(uee.toString());
        }
    }

    static void appendKanjiBytes(String content, BitArray bits) throws WriterException {
        try {
            byte[] bytes = content.getBytes("Shift_JIS");
            int length = bytes.length;
            int i = 0;
            while (i < length) {
                int byte2 = bytes[i + 1] & Light.MAIN_KEY_MAX;
                int code = ((bytes[i] & Light.MAIN_KEY_MAX) << 8) | byte2;
                int subtracted = -1;
                if (code >= 33088 && code <= 40956) {
                    subtracted = code - 33088;
                } else if (code >= 57408 && code <= 60351) {
                    subtracted = code - 49472;
                }
                if (subtracted != -1) {
                    bits.appendBits(((subtracted >> 8) * 192) + (subtracted & Light.MAIN_KEY_MAX), 13);
                    i += 2;
                } else {
                    throw new WriterException("Invalid byte sequence");
                }
            }
        } catch (UnsupportedEncodingException uee) {
            throw new WriterException(uee.toString());
        }
    }

    private static void appendECI(CharacterSetECI eci, BitArray bits) {
        bits.appendBits(Mode.ECI.getBits(), 4);
        bits.appendBits(eci.getValue(), 8);
    }
}
