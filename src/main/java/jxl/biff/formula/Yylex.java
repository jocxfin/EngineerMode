package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import java.io.IOException;
import java.io.Reader;

import jxl.biff.WorkbookMethods;

class Yylex {
    private static final int[] ZZ_ACTION = zzUnpackAction();
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
    private static final char[] ZZ_CMAP = zzUnpackCMap("\b\u0000\u0003\u0015\u0015\u0000\u0001\u0015\u0001\u0014\u0001\u0011\u0001\u0016\u0001\b\u0002\u0000\u0001\u0012\u0001\u0005\u0001\u0006\u0001!\u0001\u001f\u0001\u0004\u0001 \u0001\u0007\u0001\u001b\u0001\u001c\t\u0002\u0001\u0003\u0001\u0000\u0001$\u0001#\u0001\"\u0001\u001e\u0001\u0000\u0001\u000e\u0002\u0001\u0001\u0018\u0001\f\u0001\r\u0002\u0001\u0001\u0019\u0002\u0001\u0001\u000f\u0001\u001d\u0001\u0017\u0003\u0001\u0001\n\u0001\u0010\u0001\t\u0001\u000b\u0001\u001a\u0004\u0001\u0004\u0000\u0001\u0013\u0001\u0000\u001a\u0001ﾅ\u0000");
    private static final String[] ZZ_ERROR_MSG = new String[]{"Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large"};
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
    private static final int[] ZZ_TRANS = zzUnpackTrans();
    private boolean emptyString;
    private ExternalSheet externalSheet;
    private WorkbookMethods nameTable;
    private int yychar;
    private int yyline;
    private boolean zzAtBOL = true;
    private boolean zzAtEOF;
    private char[] zzBuffer = new char[16384];
    private int zzCurrentPos;
    private int zzEndRead;
    private int zzLexicalState = 0;
    private int zzMarkedPos;
    private int zzPushbackPos;
    private Reader zzReader;
    private int zzStartRead;
    private int zzState;

    private static int[] zzUnpackAction() {
        int[] result = new int[94];
        int offset = zzUnpackAction("\u0001\u0000\u0001\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0006\u0001\u0007\u0001\u0000\u0002\u0002\u0001\b\u0001\u0000\u0001\t\u0001\u0000\u0001\n\u0001\u000b\u0001\f\u0001\r\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0001\u0001\u0011\u0001\u0002\u0001\u0012\u0001\u0000\u0001\u0013\u0001\u0000\u0001\u0002\u0003\u0000\u0002\u0002\u0005\u0000\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0002\u0001\u0000\u0001\u0017\u0001\u0000\u0001\u0012\u0002\u0000\u0001\u0018\u0001\u0000\u0002\u0002\b\u0000\u0001\u0017\u0001\u0000\u0001\u0019\u0001\u0000\u0001\u001a\b\u0000\u0001\u001b\u0002\u0000\u0001\u0019\u0002\u0000\u0001\u001c\u0004\u0000\u0001\u001d\u0003\u0000\u0001\u001d\u0001\u0000\u0001\u001e\u0001\u0000", 0, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int j = offset;
        int l = packed.length();
        int i = 0;
        while (i < l) {
            int j2;
            int i2 = i + 1;
            int count = packed.charAt(i);
            i = i2 + 1;
            int value = packed.charAt(i2);
            while (true) {
                j2 = j + 1;
                result[j] = value;
                count--;
                if (count <= 0) {
                    break;
                }
                j = j2;
            }
            j = j2;
        }
        return j;
    }

    private static int[] zzUnpackRowMap() {
        int[] result = new int[94];
        int offset = zzUnpackRowMap("\u0000\u0000\u0000%\u0000J\u0000o\u0000\u0000\u0000\u0000\u0000¹\u0000Þ\u0000ă\u0000\u0000Ĩ\u0000\u0000ō\u0000\u0000\u0000\u0000\u0000Ų\u0000\u0000Ɨ\u0000Ƽ\u0000\u0000ǡ\u0000Ȇ\u0000ȫ\u0000\u0000ɐ\u0000ɵ\u0000ʚ\u0000ʿ\u0000ˤ\u0000̉\u0000̮\u0000͓\u0000͸\u0000Ν\u0000ς\u0000ϧ\u0000\u0000\u0000\u0000Ќ\u0000б\u0000і\u0000ѻ\u0000Ҡ\u0000Ӆ\u0000Ӫ\u0000ʿ\u0000ԏ\u0000Դ\u0000ՙ\u0000վ\u0000֣\u0000׈\u0000׭\u0000ؒ\u0000ط\u0000ٜ\u0000ځ\u0000\u0000ڦ\u0000ۋ\u0000ۋ\u0000Ќ\u0000۰\u0000ܕ\u0000ܺ\u0000ݟ\u0000ބ\u0000ީ\u0000ߎ\u0000߳\u0000࠘\u0000࠘\u0000࠽\u0000ࡢ\u0000ࢇ\u0000ࢬ\u0000\u0000࣑\u0000ࣶ\u0000छ\u0000ी\u0000॥\u0000ঊ\u0000য\u0000৔\u0000\u0000৹\u0000ਞ\u0000ਞ", 0, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int j = offset;
        int l = packed.length();
        int j2 = j;
        int i = 0;
        while (i < l) {
            int i2 = i + 1;
            int high = packed.charAt(i) << 16;
            j = j2 + 1;
            i = i2 + 1;
            result[j2] = packed.charAt(i2) | high;
            j2 = j;
        }
        return j2;
    }

    private static int[] zzUnpackTrans() {
        int[] result = new int[2627];
        int offset = zzUnpackTrans("\u0001\u0000\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\u0000\u0001\t\u0001\n\u0003\u0003\u0001\u000b\u0003\u0003\u0001\f\u0001\r\u0002\u0000\u0001\u000e\u0001\u000f\u0004\u0003\u0001\u0010\u0001\u0004\u0001\u0003\u0001\u0000\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0015\u0001\u0016\u0011\u0017\u0001\u0018\u0013\u0017\u0001\u0000\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\b\u0019\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004\u0019\u0001\u0000\u0001\u001a\u0001\u0019\t\u0000\u0001\u0004\u0004\u0000\u0001 \u0014\u0000\u0001\u0004.\u0000\u0001!\u0007\u0000\b!\u0006\u0000\u0004!\u0002\u0000\u0001!\b\u0000\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\u0001\u0019\u0001\"\u0006\u0019\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004\u0019\u0001\u0000\u0001\u001a\u0001\u0019\b\u0000\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\u0005\u0019\u0001#\u0002\u0019\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004\u0019\u0001\u0000\u0001\u001a\u0001\u0019\u0007\u0000\u0012\r\u0001$\u0012\r\n\u0000\u0001%\f\u0000\u0001&\u0001'\u0001\u0000\u0001(-\u0000\u0001)#\u0000\u0001*\u0001+\u0001\u0000\u0011\u0017\u0001\u0000\u0013\u0017\u0001\u0000\u0001,\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\b,\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001a\u0001,\b\u0000\u0001\u001e\u0001\u001a\u0001-\u0005\u0000\b\u001e\u0002\u0000\u0001\u001e\u0003\u0000\u0004\u001e\u0001\u0000\u0001\u001a\u0001\u001e\b\u0000\u0001.\u0006\u0000\u0001/\b.\u0006\u0000\u0004.\u0002\u0000\u0001.\t\u0000\u00010\u0019\u0000\u00010\t\u0000\u0002\u001e\u0006\u0000\b\u001e\u0002\u0000\u0001\u001e\u0003\u0000\u0004\u001e\u0001\u0000\u0002\u001e\b\u0000\u00011\u0006\u0000\u00012\b1\u0006\u0000\u00041\u0002\u0000\u00011\t\u0000\u00013\u0019\u0000\u00013\t\u0000\u00014\u00010\u0001\u001b\u0004\u0000\u0001\u001d\b4\u0006\u0000\u00044\u0001\u0000\u00010\u00014\b\u0000\u0001,\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\u0002,\u00015\u0005,\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001a\u0001,\b\u0000\u0001,\u0001\u001a\u0001\u001b\u0001\u0000\u0001\u001c\u0002\u0000\u0001\u001d\u0006,\u00016\u0001,\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001a\u0001,\u001b\u0000\u00017\u001c\u0000\u00018#\u0000\u00019\u0002\u0000\u0001:/\u0000\u0001;\u0019\u0000\u0001<\u0017\u0000\u0001,\u0001\u001e\u0002\u0000\u0001\u001c\u0003\u0000\b,\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001e\u0001,\b\u0000\u0001=\u0006\u0000\u0001>\b=\u0006\u0000\u0004=\u0002\u0000\u0001=\b\u0000\u0001?\u0007\u0000\b?\u0006\u0000\u0004?\u0002\u0000\u0001?\b\u0000\u0001.\u0007\u0000\b.\u0006\u0000\u0004.\u0002\u0000\u0001.\t\u0000\u00010\u0001-\u0018\u0000\u00010\t\u0000\u0001@\u0001A\u0005\u0000\u0001B\b@\u0006\u0000\u0004@\u0001\u0000\u0001A\u0001@\b\u0000\u00011\u0007\u0000\b1\u0006\u0000\u00041\u0002\u0000\u00011\t\u0000\u00010\u0001\u001b\u0004\u0000\u0001\u001d\u0013\u0000\u00010\t\u0000\u0001,\u0001\u001e\u0002\u0000\u0001\u001c\u0003\u0000\u0003,\u0001C\u0004,\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001e\u0001,\b\u0000\u0001,\u0001\u001e\u0002\u0000\u0001\u001c\u0003\u0000\u0007,\u00015\u0002\u0000\u0001\u001e\u0001\u001f\u0002\u0000\u0004,\u0001\u0000\u0001\u001e\u0001,\b\u0000\u0001D\u0006\u0000\u0001E\bD\u0006\u0000\u0004D\u0002\u0000\u0001D\u0014\u0000\u0001F&\u0000\u0001G\r\u0000\u0001F$\u0000\u0001H!\u0000\u0001I\u0019\u0000\u0001J\u0016\u0000\u0001K\u0001L\u0005\u0000\u0001M\bK\u0006\u0000\u0004K\u0001\u0000\u0001L\u0001K\b\u0000\u0001=\u0007\u0000\b=\u0006\u0000\u0004=\u0002\u0000\u0001=\t\u0000\u0001A\u0005\u0000\u0001B\u0013\u0000\u0001A\n\u0000\u0001A\u0019\u0000\u0001A\t\u0000\u0001N\u0001O\u0001P\u0004\u0000\u0001Q\bN\u0006\u0000\u0004N\u0001\u0000\u0001O\u0001N\b\u0000\u0001D\u0007\u0000\bD\u0006\u0000\u0004D\u0002\u0000\u0001D\u001b\u0000\u0001R\u001f\u0000\u0001F!\u0000\u0001S3\u0000\u0001T\u0014\u0000\u0001U\u001b\u0000\u0001L\u0005\u0000\u0001M\u0013\u0000\u0001L\n\u0000\u0001L\u0019\u0000\u0001L\n\u0000\u0001O\u0001P\u0004\u0000\u0001Q\u0013\u0000\u0001O\n\u0000\u0001O\u0001V\u0018\u0000\u0001O\t\u0000\u0001W\u0006\u0000\u0001X\bW\u0006\u0000\u0004W\u0002\u0000\u0001W\t\u0000\u0001O\u0019\u0000\u0001O&\u0000\u0001R\"\u0000\u0001F\u0014\u0000\u0001F\u0019\u0000\u0001Y\u0006\u0000\u0001Z\bY\u0006\u0000\u0004Y\u0002\u0000\u0001Y\b\u0000\u0001[\u0007\u0000\b[\u0006\u0000\u0004[\u0002\u0000\u0001[\b\u0000\u0001W\u0007\u0000\bW\u0006\u0000\u0004W\u0002\u0000\u0001W\b\u0000\u0001\\\u0001]\u0005\u0000\u0001^\b\\\u0006\u0000\u0004\\\u0001\u0000\u0001]\u0001\\\b\u0000\u0001Y\u0007\u0000\bY\u0006\u0000\u0004Y\u0002\u0000\u0001Y\t\u0000\u0001]\u0005\u0000\u0001^\u0013\u0000\u0001]\n\u0000\u0001]\u0019\u0000\u0001]\b\u0000", 0, result);
        return result;
    }

    private static int zzUnpackTrans(String packed, int offset, int[] result) {
        int j = offset;
        int l = packed.length();
        int i = 0;
        while (i < l) {
            int j2;
            int i2 = i + 1;
            int count = packed.charAt(i);
            i = i2 + 1;
            int value = packed.charAt(i2) - 1;
            while (true) {
                j2 = j + 1;
                result[j] = value;
                count--;
                if (count <= 0) {
                    break;
                }
                j = j2;
            }
            j = j2;
        }
        return j;
    }

    private static int[] zzUnpackAttribute() {
        int[] result = new int[94];
        int offset = zzUnpackAttribute("\u0001\u0000\u0003\u0001\u0004\t\u0001\u0000\u0002\u0001\u0001\t\u0001\u0000\u0001\t\u0001\u0000\u0004\t\u0001\u0001\u0001\t\u0002\u0001\u0001\t\u0002\u0001\u0001\u0000\u0001\t\u0001\u0000\u0001\u0001\u0003\u0000\u0002\u0001\u0005\u0000\u0003\t\u0001\u0001\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0002\u0000\u0001\u0001\u0001\u0000\u0002\u0001\b\u0000\u0001\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\b\u0000\u0001\u0001\u0002\u0000\u0001\u0001\u0002\u0000\u0001\t\u0004\u0000\u0001\u0001\u0003\u0000\u0001\t\u0001\u0000\u0001\u0001\u0001\u0000", 0, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int j = offset;
        int l = packed.length();
        int i = 0;
        while (i < l) {
            int j2;
            int i2 = i + 1;
            int count = packed.charAt(i);
            i = i2 + 1;
            int value = packed.charAt(i2);
            while (true) {
                j2 = j + 1;
                result[j] = value;
                count--;
                if (count <= 0) {
                    break;
                }
                j = j2;
            }
            j = j2;
        }
        return j;
    }

    int getPos() {
        return this.yychar;
    }

    void setExternalSheet(ExternalSheet es) {
        this.externalSheet = es;
    }

    void setNameTable(WorkbookMethods nt) {
        this.nameTable = nt;
    }

    Yylex(Reader in) {
        this.zzReader = in;
    }

    private static char[] zzUnpackCMap(String packed) {
        char[] map = new char[65536];
        int j = 0;
        int i = 0;
        while (i < 100) {
            int j2;
            int i2 = i + 1;
            int count = packed.charAt(i);
            i = i2 + 1;
            char value = packed.charAt(i2);
            while (true) {
                j2 = j + 1;
                map[j] = (char) value;
                count--;
                if (count <= 0) {
                    break;
                }
                j = j2;
            }
            j = j2;
        }
        return map;
    }

    private boolean zzRefill() throws IOException {
        if (this.zzStartRead > 0) {
            System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzPushbackPos -= this.zzStartRead;
            this.zzStartRead = 0;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            char[] newBuffer = new char[(this.zzCurrentPos * 2)];
            System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
            this.zzBuffer = newBuffer;
        }
        int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        if (numRead < 0) {
            return true;
        }
        this.zzEndRead += numRead;
        return false;
    }

    public final void yybegin(int newState) {
        this.zzLexicalState = newState;
    }

    public final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }

    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[0];
        }
        throw new Error(message);
    }

    public ParseItem yylex() throws IOException, FormulaException {
        int zzEndReadL = this.zzEndRead;
        char[] zzBufferL = this.zzBuffer;
        char[] zzCMapL = ZZ_CMAP;
        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;
        while (true) {
            int zzCurrentPosL;
            int zzMarkedPosL = this.zzMarkedPos;
            this.yychar += zzMarkedPosL - this.zzStartRead;
            boolean zzR = false;
            for (zzCurrentPosL = this.zzStartRead; zzCurrentPosL < zzMarkedPosL; zzCurrentPosL++) {
                switch (zzBufferL[zzCurrentPosL]) {
                    case '\n':
                        if (!zzR) {
                            this.yyline++;
                            break;
                        }
                        zzR = false;
                        break;
                    case '\u000b':
                    case '\f':
                    case '':
                    case ' ':
                    case ' ':
                        this.yyline++;
                        zzR = false;
                        break;
                    case '\r':
                        this.yyline++;
                        zzR = true;
                        break;
                    default:
                        zzR = false;
                        break;
                }
            }
            if (zzR) {
                boolean z;
                if (zzMarkedPosL < zzEndReadL) {
                    z = zzBufferL[zzMarkedPosL] == '\n';
                } else if (this.zzAtEOF) {
                    z = false;
                } else {
                    boolean eof = zzRefill();
                    zzEndReadL = this.zzEndRead;
                    zzMarkedPosL = this.zzMarkedPos;
                    zzBufferL = this.zzBuffer;
                    z = !eof ? zzBufferL[zzMarkedPosL] == '\n' : false;
                }
                if (z) {
                    this.yyline--;
                }
            }
            int zzAction = -1;
            this.zzStartRead = zzMarkedPosL;
            this.zzCurrentPos = zzMarkedPosL;
            zzCurrentPosL = zzMarkedPosL;
            this.zzState = this.zzLexicalState;
            int zzCurrentPosL2 = zzCurrentPosL;
            while (true) {
                int zzInput;
                if (zzCurrentPosL2 >= zzEndReadL) {
                    if (this.zzAtEOF) {
                        zzInput = -1;
                        zzCurrentPosL = zzCurrentPosL2;
                    } else {
                        this.zzCurrentPos = zzCurrentPosL2;
                        this.zzMarkedPos = zzMarkedPosL;
                        eof = zzRefill();
                        zzCurrentPosL = this.zzCurrentPos;
                        zzMarkedPosL = this.zzMarkedPos;
                        zzBufferL = this.zzBuffer;
                        zzEndReadL = this.zzEndRead;
                        if (eof) {
                            zzInput = -1;
                        } else {
                            zzCurrentPosL2 = zzCurrentPosL + 1;
                            zzInput = zzBufferL[zzCurrentPosL];
                            zzCurrentPosL = zzCurrentPosL2;
                        }
                    }
                    this.zzMarkedPos = zzMarkedPosL;
                    if (zzAction >= 0) {
                        zzAction = ZZ_ACTION[zzAction];
                    }
                    switch (zzAction) {
                        case Light.MAIN_KEY_LIGHT /*1*/:
                            this.emptyString = false;
                            return new StringValue(yytext());
                        case Light.CHARGE_RED_LIGHT /*2*/:
                            return new NameRange(yytext(), this.nameTable);
                        case Light.CHARGE_GREEN_LIGHT /*3*/:
                            return new IntegerValue(yytext());
                        case 4:
                            return new RangeSeparator();
                        case 5:
                            return new ArgumentSeparator();
                        case Light.MAIN_KEY_NORMAL /*6*/:
                            return new OpenParentheses();
                        case 7:
                            return new CloseParentheses();
                        case 8:
                            this.emptyString = true;
                            yybegin(1);
                            break;
                        case 9:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                            break;
                        case 10:
                            return new Divide();
                        case 11:
                            return new Plus();
                        case 12:
                            return new Minus();
                        case 13:
                            return new Multiply();
                        case 14:
                            return new GreaterThan();
                        case 15:
                            return new Equal();
                        case 16:
                            return new LessThan();
                        case 17:
                            yybegin(0);
                            if (this.emptyString) {
                                break;
                            }
                            return new StringValue("");
                        case 18:
                            return new CellReference(yytext());
                        case 19:
                            return new StringFunction(yytext());
                        case 20:
                            return new GreaterEqual();
                        case 21:
                            return new NotEqual();
                        case 22:
                            return new LessEqual();
                        case 23:
                            return new ColumnRange(yytext());
                        case 24:
                            return new DoubleValue(yytext());
                        case 25:
                            return new CellReference3d(yytext(), this.externalSheet);
                        case 26:
                            return new BooleanValue(yytext());
                        case 27:
                            return new Area(yytext());
                        case 28:
                            return new ErrorConstant(yytext());
                        case 29:
                            return new ColumnRange3d(yytext(), this.externalSheet);
                        case 30:
                            return new Area3d(yytext(), this.externalSheet);
                        default:
                            if (zzInput != -1 || this.zzStartRead != this.zzCurrentPos) {
                                zzScanError(1);
                                break;
                            }
                            this.zzAtEOF = true;
                            return null;
                    }
                }
                zzCurrentPosL = zzCurrentPosL2 + 1;
                zzInput = zzBufferL[zzCurrentPosL2];
                int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
                if (zzNext != -1) {
                    this.zzState = zzNext;
                    int zzAttributes = zzAttrL[this.zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = this.zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) != 8) {
                        }
                    }
                    zzCurrentPosL2 = zzCurrentPosL;
                }
                this.zzMarkedPos = zzMarkedPosL;
                if (zzAction >= 0) {
                    zzAction = ZZ_ACTION[zzAction];
                }
                switch (zzAction) {
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        this.emptyString = false;
                        return new StringValue(yytext());
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        return new NameRange(yytext(), this.nameTable);
                    case Light.CHARGE_GREEN_LIGHT /*3*/:
                        return new IntegerValue(yytext());
                    case 4:
                        return new RangeSeparator();
                    case 5:
                        return new ArgumentSeparator();
                    case Light.MAIN_KEY_NORMAL /*6*/:
                        return new OpenParentheses();
                    case 7:
                        return new CloseParentheses();
                    case 8:
                        this.emptyString = true;
                        yybegin(1);
                        break;
                    case 9:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                        break;
                    case 10:
                        return new Divide();
                    case 11:
                        return new Plus();
                    case 12:
                        return new Minus();
                    case 13:
                        return new Multiply();
                    case 14:
                        return new GreaterThan();
                    case 15:
                        return new Equal();
                    case 16:
                        return new LessThan();
                    case 17:
                        yybegin(0);
                        if (this.emptyString) {
                            break;
                        }
                        return new StringValue("");
                    case 18:
                        return new CellReference(yytext());
                    case 19:
                        return new StringFunction(yytext());
                    case 20:
                        return new GreaterEqual();
                    case 21:
                        return new NotEqual();
                    case 22:
                        return new LessEqual();
                    case 23:
                        return new ColumnRange(yytext());
                    case 24:
                        return new DoubleValue(yytext());
                    case 25:
                        return new CellReference3d(yytext(), this.externalSheet);
                    case 26:
                        return new BooleanValue(yytext());
                    case 27:
                        return new Area(yytext());
                    case 28:
                        return new ErrorConstant(yytext());
                    case 29:
                        return new ColumnRange3d(yytext(), this.externalSheet);
                    case 30:
                        return new Area3d(yytext(), this.externalSheet);
                    default:
                        if (zzInput != -1) {
                            this.zzAtEOF = true;
                            return null;
                        }
                        zzScanError(1);
                        break;
                }
            }
        }
    }
}
