package jxl.biff;

import com.android.engineeringmode.functions.Light;

public final class Type {
    public static final Type ALRUNS = new Type(4176);
    public static final Type ARRAY = new Type(545);
    public static final Type AUTOFILTER = new Type(158);
    public static final Type AUTOFILTERINFO = new Type(157);
    public static final Type BACKUP = new Type(64);
    public static final Type BLANK = new Type(513);
    public static final Type BOF = new Type(2057);
    public static final Type BOOKBOOL = new Type(218);
    public static final Type BOOLERR = new Type(517);
    public static final Type BOTTOMMARGIN = new Type(41);
    public static final Type BOUNDSHEET = new Type(133);
    public static final Type BUTTONPROPERTYSET = new Type(442);
    public static final Type CALCCOUNT = new Type(12);
    public static final Type CALCMODE = new Type(13);
    public static final Type CF = new Type(433);
    public static final Type CODEPAGE = new Type(66);
    public static final Type COLINFO = new Type(125);
    public static final Type CONDFMT = new Type(432);
    public static final Type CONTINUE = new Type(60);
    public static final Type COUNTRY = new Type(140);
    public static final Type CRN = new Type(90);
    public static final Type DBCELL = new Type(215);
    public static final Type DEFAULTROWHEIGHT = new Type(549);
    public static final Type DEFCOLWIDTH = new Type(85);
    public static final Type DELTA = new Type(16);
    public static final Type DIMENSION = new Type(512);
    public static final Type DSF = new Type(353);
    public static final Type DV = new Type(446);
    public static final Type DVAL = new Type(434);
    public static final Type EOF = new Type(10);
    public static final Type EXCEL9FILE = new Type(448);
    public static final Type EXTERNNAME = new Type(35);
    public static final Type EXTERNSHEET = new Type(23);
    public static final Type EXTSST = new Type(Light.MAIN_KEY_MAX);
    public static final Type FBI = new Type(4192);
    public static final Type FILEPASS = new Type(47);
    public static final Type FILTERMODE = new Type(155);
    public static final Type FNGROUPCOUNT = new Type(156);
    public static final Type FONT = new Type(49);
    public static final Type FONTX = new Type(4134);
    public static final Type FOOTER = new Type(21);
    public static final Type FORMAT = new Type(1054);
    public static final Type FORMULA = new Type(1030);
    public static final Type FORMULA2 = new Type(6);
    public static final Type GRIDSET = new Type(130);
    public static final Type GUTS = new Type(128);
    public static final Type HCENTER = new Type(131);
    public static final Type HEADER = new Type(20);
    public static final Type HIDEOBJ = new Type(141);
    public static final Type HLINK = new Type(440);
    public static final Type HORIZONTALPAGEBREAKS = new Type(27);
    public static final Type IFMT = new Type(4174);
    public static final Type INDEX = new Type(523);
    public static final Type INTERFACEEND = new Type(226);
    public static final Type INTERFACEHDR = new Type(225);
    public static final Type ITERATION = new Type(17);
    public static final Type LABEL = new Type(516);
    public static final Type LABELSST = new Type(253);
    public static final Type LEFTMARGIN = new Type(38);
    public static final Type MERGEDCELLS = new Type(229);
    public static final Type MMS = new Type(193);
    public static final Type MSODRAWING = new Type(236);
    public static final Type MSODRAWINGGROUP = new Type(235);
    public static final Type MULBLANK = new Type(190);
    public static final Type MULRK = new Type(189);
    public static final Type NAME = new Type(24);
    public static final Type NINETEENFOUR = new Type(34);
    public static final Type NOTE = new Type(28);
    public static final Type NUMBER = new Type(515);
    public static final Type OBJ = new Type(93);
    public static final Type OBJPROJ = new Type(211);
    public static final Type OBJPROTECT = new Type(99);
    public static final Type PALETTE = new Type(146);
    public static final Type PANE = new Type(65);
    public static final Type PASSWORD = new Type(19);
    public static final Type PLS = new Type(77);
    public static final Type PRECISION = new Type(14);
    public static final Type PRINTGRIDLINES = new Type(43);
    public static final Type PRINTHEADERS = new Type(42);
    public static final Type PROT4REV = new Type(431);
    public static final Type PROT4REVPASS = new Type(444);
    public static final Type PROTECT = new Type(18);
    public static final Type REFMODE = new Type(15);
    public static final Type REFRESHALL = new Type(439);
    public static final Type RIGHTMARGIN = new Type(39);
    public static final Type RK = new Type(126);
    public static final Type RK2 = new Type(638);
    public static final Type ROW = new Type(520);
    public static final Type RSTRING = new Type(214);
    public static final Type SAVERECALC = new Type(95);
    public static final Type SBASEREF = new Type(4168);
    public static final Type SCENPROTECT = new Type(221);
    public static final Type SCL = new Type(160);
    public static final Type SELECTION = new Type(29);
    public static final Type SERIES = new Type(4099);
    public static final Type SERIESLIST = new Type(4118);
    public static final Type SETUP = new Type(161);
    public static final Type SHAREDFORMULA = new Type(1212);
    public static final Type SORT = new Type(144);
    public static final Type SST = new Type(252);
    public static final Type STRING = new Type(519);
    public static final Type STYLE = new Type(659);
    public static final Type SUPBOOK = new Type(430);
    public static final Type TABID = new Type(317);
    public static final Type TEMPLATE = new Type(96);
    public static final Type TOPMARGIN = new Type(40);
    public static final Type TXO = new Type(438);
    public static final Type U1C0 = new Type(448);
    public static final Type U1C1 = new Type(449);
    public static final Type UNKNOWN = new Type(65535);
    public static final Type USESELFS = new Type(352);
    public static final Type VCENTER = new Type(132);
    public static final Type VERTICALPAGEBREAKS = new Type(26);
    public static final Type WEIRD1 = new Type(239);
    public static final Type WINDOW1 = new Type(61);
    public static final Type WINDOW2 = new Type(574);
    public static final Type WINDOWPROTECT = new Type(25);
    public static final Type WRITEACCESS = new Type(92);
    public static final Type WSBOOL = new Type(129);
    public static final Type XCT = new Type(89);
    public static final Type XF = new Type(224);
    private static ArbitraryType arbitrary = new ArbitraryType();
    private static Type[] types = new Type[0];
    public final int value;

    private static class ArbitraryType {
        private ArbitraryType() {
        }
    }

    private Type(int v) {
        this.value = v;
        Type[] newTypes = new Type[(types.length + 1)];
        System.arraycopy(types, 0, newTypes, 0, types.length);
        newTypes[types.length] = this;
        types = newTypes;
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == this) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        if (this.value == ((Type) o).value) {
            z = true;
        }
        return z;
    }

    public static Type getType(int v) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].value == v) {
                return types[i];
            }
        }
        return UNKNOWN;
    }
}
