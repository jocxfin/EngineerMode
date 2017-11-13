package jxl.format;

public final class PaperSize {
    public static final PaperSize A2 = new PaperSize(66);
    public static final PaperSize A3 = new PaperSize(8);
    public static final PaperSize A3_EXTRA = new PaperSize(63);
    public static final PaperSize A3_EXTRA_TRANSVERSE = new PaperSize(68);
    public static final PaperSize A3_ROTATED = new PaperSize(76);
    public static final PaperSize A3_TRANSVERSE = new PaperSize(67);
    public static final PaperSize A4 = new PaperSize(9);
    public static final PaperSize A4_EXTRA = new PaperSize(53);
    public static final PaperSize A4_PLUS = new PaperSize(60);
    public static final PaperSize A4_ROTATED = new PaperSize(77);
    public static final PaperSize A4_SMALL = new PaperSize(10);
    public static final PaperSize A4_TRANSVERSE = new PaperSize(55);
    public static final PaperSize A5 = new PaperSize(11);
    public static final PaperSize A5_EXTRA = new PaperSize(64);
    public static final PaperSize A5_ROTATED = new PaperSize(78);
    public static final PaperSize A5_TRANSVERSE = new PaperSize(61);
    public static final PaperSize A6 = new PaperSize(70);
    public static final PaperSize A6_ROTATED = new PaperSize(83);
    public static final PaperSize B4 = new PaperSize(12);
    public static final PaperSize B4_ISO = new PaperSize(33);
    public static final PaperSize B4_ISO_2 = new PaperSize(42);
    public static final PaperSize B4_ROTATED = new PaperSize(79);
    public static final PaperSize B5 = new PaperSize(13);
    public static final PaperSize B5_EXTRA = new PaperSize(65);
    public static final PaperSize B5_ISO = new PaperSize(34);
    public static final PaperSize B5_ROTATED = new PaperSize(80);
    public static final PaperSize B5_TRANSVERSE = new PaperSize(62);
    public static final PaperSize B6 = new PaperSize(88);
    public static final PaperSize B6_ISO = new PaperSize(35);
    public static final PaperSize B6_ROTATED = new PaperSize(89);
    public static final PaperSize C = new PaperSize(24);
    public static final PaperSize D = new PaperSize(25);
    public static final PaperSize DOUBLE_JAPANESE_POSTCARD = new PaperSize(69);
    public static final PaperSize DOUBLE_JAPANESE_POSTCARD_ROTATED = new PaperSize(82);
    public static final PaperSize E = new PaperSize(26);
    public static final PaperSize ENVELOPE_10 = new PaperSize(20);
    public static final PaperSize ENVELOPE_11 = new PaperSize(21);
    public static final PaperSize ENVELOPE_12 = new PaperSize(22);
    public static final PaperSize ENVELOPE_14 = new PaperSize(23);
    public static final PaperSize ENVELOPE_6_75 = new PaperSize(38);
    public static final PaperSize ENVELOPE_9 = new PaperSize(19);
    public static final PaperSize ENVELOPE_C3 = new PaperSize(29);
    public static final PaperSize ENVELOPE_C4 = new PaperSize(30);
    public static final PaperSize ENVELOPE_C5 = new PaperSize(28);
    public static final PaperSize ENVELOPE_C6 = new PaperSize(31);
    public static final PaperSize ENVELOPE_C6_C5 = new PaperSize(32);
    public static final PaperSize ENVELOPE_DL = new PaperSize(27);
    public static final PaperSize ENVELOPE_INVITE = new PaperSize(47);
    public static final PaperSize ENVELOPE_ITALY = new PaperSize(36);
    public static final PaperSize ENVELOPE_MONARCH = new PaperSize(37);
    public static final PaperSize EXECUTIVE = new PaperSize(7);
    public static final PaperSize FOLIO = new PaperSize(14);
    public static final PaperSize GERMAN_FANFOLD = new PaperSize(40);
    public static final PaperSize GERMAN_LEGAL_FANFOLD = new PaperSize(41);
    public static final PaperSize JAPANESE_POSTCARD = new PaperSize(43);
    public static final PaperSize JAPANESE_POSTCARD_ROTATED = new PaperSize(81);
    public static final PaperSize LEDGER = new PaperSize(4);
    public static final PaperSize LEGAL = new PaperSize(5);
    public static final PaperSize LEGAL_EXTRA = new PaperSize(51);
    public static final PaperSize LETTER = new PaperSize(1);
    public static final PaperSize LETTER_EXTRA = new PaperSize(50);
    public static final PaperSize LETTER_EXTRA_TRANSVERSE = new PaperSize(56);
    public static final PaperSize LETTER_PLUS = new PaperSize(59);
    public static final PaperSize LETTER_ROTATED = new PaperSize(75);
    public static final PaperSize LETTER_SMALL = new PaperSize(2);
    public static final PaperSize LETTER_TRANSVERSE = new PaperSize(54);
    public static final PaperSize NOTE = new PaperSize(18);
    public static final PaperSize QUARTO = new PaperSize(15);
    public static final PaperSize SIZE_10x11 = new PaperSize(45);
    public static final PaperSize SIZE_10x14 = new PaperSize(16);
    public static final PaperSize SIZE_10x17 = new PaperSize(17);
    public static final PaperSize SIZE_15x11 = new PaperSize(46);
    public static final PaperSize SIZE_9x11 = new PaperSize(44);
    public static final PaperSize STATEMENT = new PaperSize(6);
    public static final PaperSize SUPER_A_A4 = new PaperSize(57);
    public static final PaperSize SUPER_B_A3 = new PaperSize(58);
    public static final PaperSize TABLOID = new PaperSize(3);
    public static final PaperSize TABLOID_EXTRA = new PaperSize(52);
    public static final PaperSize UNDEFINED = new PaperSize(0);
    public static final PaperSize US_FANFOLD = new PaperSize(39);
    private static PaperSize[] paperSizes = new PaperSize[90];
    private int val;

    private PaperSize(int v, boolean growArray) {
        this.val = v;
        if (v >= paperSizes.length && growArray) {
            PaperSize[] newarray = new PaperSize[(v + 1)];
            System.arraycopy(paperSizes, 0, newarray, 0, paperSizes.length);
            paperSizes = newarray;
        }
        if (v < paperSizes.length) {
            paperSizes[v] = this;
        }
    }

    private PaperSize(int v) {
        this(v, true);
    }

    public int getValue() {
        return this.val;
    }

    public static PaperSize getPaperSize(int val) {
        PaperSize p = null;
        if (val <= paperSizes.length - 1) {
            p = paperSizes[val];
        }
        return p != null ? p : new PaperSize(val, false);
    }
}
