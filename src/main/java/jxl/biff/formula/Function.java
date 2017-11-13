package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.WorkbookSettings;
import jxl.common.Logger;

final class Function {
    public static final Function ABS = new Function(24, "abs", 1);
    public static final Function ACOS = new Function(99, "acos", 1);
    public static final Function ACOSH = new Function(233, "acosh", 1);
    public static final Function ADDRESS = new Function(219, "address", Light.MAIN_KEY_MAX);
    public static final Function AND = new Function(36, "and", Light.MAIN_KEY_MAX);
    public static final Function AREAS = new Function(75, "areas", Light.MAIN_KEY_MAX);
    public static final Function ASIN = new Function(98, "asin", 1);
    public static final Function ASINH = new Function(232, "asinh", 1);
    public static final Function ATAN = new Function(18, "atan", 1);
    public static final Function ATAN2 = new Function(97, "atan2", 1);
    public static final Function ATANH = new Function(234, "atanh", 1);
    public static final Function ATTRIBUTE = new Function(1, "", Light.MAIN_KEY_MAX);
    public static final Function AVEDEV = new Function(269, "avedev", Light.MAIN_KEY_MAX);
    public static final Function AVERAGE = new Function(5, "average", Light.MAIN_KEY_MAX);
    public static final Function AVERAGEA = new Function(361, "averagea", Light.MAIN_KEY_MAX);
    public static final Function AYS360 = new Function(220, "days360", Light.MAIN_KEY_MAX);
    public static final Function BETADIST = new Function(270, "betadist", Light.MAIN_KEY_MAX);
    public static final Function BETAINV = new Function(272, "betainv", Light.MAIN_KEY_MAX);
    public static final Function BINOMDIST = new Function(273, "binomdist", 4);
    public static final Function CEILING = new Function(288, "ceiling", 2);
    public static final Function CELL = new Function(125, "cell", 2);
    public static final Function CHAR = new Function(111, "char", 1);
    public static final Function CHIDIST = new Function(274, "chidist", 2);
    public static final Function CHIINV = new Function(275, "chiinv", 2);
    public static final Function CHITEST = new Function(306, "chitest", Light.MAIN_KEY_MAX);
    public static final Function CHOOSE = new Function(100, "choose", Light.MAIN_KEY_MAX);
    public static final Function CLEAN = new Function(162, "clean", 1);
    public static final Function CODE = new Function(121, "code", 1);
    public static final Function COLUMN = new Function(9, "column", Light.MAIN_KEY_MAX);
    public static final Function COLUMNS = new Function(77, "columns", Light.MAIN_KEY_MAX);
    public static final Function COMBIN = new Function(276, "combin", 2);
    public static final Function CONCATENATE = new Function(336, "concatenate", Light.MAIN_KEY_MAX);
    public static final Function CONFIDENCE = new Function(277, "confidence", 3);
    public static final Function CORREL = new Function(307, "correl", Light.MAIN_KEY_MAX);
    public static final Function COS = new Function(16, "cos", 1);
    public static final Function COSH = new Function(230, "cosh", 1);
    public static final Function COUNT = new Function(0, "count", Light.MAIN_KEY_MAX);
    public static final Function COUNTA = new Function(169, "counta", Light.MAIN_KEY_MAX);
    public static final Function COUNTBLANK = new Function(347, "countblank", 1);
    public static final Function COUNTIF = new Function(346, "countif", 2);
    public static final Function COVAR = new Function(308, "covar", Light.MAIN_KEY_MAX);
    public static final Function CRITBINOM = new Function(278, "critbinom", 3);
    public static final Function DATE = new Function(65, "date", 3);
    public static final Function DATEVALUE = new Function(140, "datevalue", 1);
    public static final Function DAVERAGE = new Function(42, "daverage", 3);
    public static final Function DAY = new Function(67, "day", 1);
    public static final Function DCOUNT = new Function(40, "dcount", 3);
    public static final Function DCOUNTA = new Function(199, "dcounta", Light.MAIN_KEY_MAX);
    public static final Function DDB = new Function(144, "ddb", Light.MAIN_KEY_MAX);
    public static final Function DEGREES = new Function(343, "degrees", 1);
    public static final Function DEVSQ = new Function(318, "devsq", Light.MAIN_KEY_MAX);
    public static final Function DMAX = new Function(44, "dmax", 3);
    public static final Function DMIN = new Function(43, "dmin", 3);
    public static final Function DOLLAR = new Function(13, "dollar", 2);
    public static final Function DPRODUCT = new Function(189, "dproduct", 3);
    public static final Function DSTDEV = new Function(45, "dstdev", 3);
    public static final Function DSTDEVP = new Function(195, "dstdevp", Light.MAIN_KEY_MAX);
    public static final Function DSUM = new Function(41, "dsum", 3);
    public static final Function DVAR = new Function(47, "dvar", 3);
    public static final Function DVARP = new Function(196, "dvarp", Light.MAIN_KEY_MAX);
    public static final Function ERROR = new Function(84, "error", 1);
    public static final Function EVEN = new Function(279, "even", 1);
    public static final Function EXACT = new Function(117, "exact", 2);
    public static final Function EXP = new Function(21, "exp", 1);
    public static final Function EXPONDIST = new Function(280, "expondist", 3);
    public static final Function FACT = new Function(184, "fact", 1);
    public static final Function FALSE = new Function(35, "false", 0);
    public static final Function FDIST = new Function(281, "fdist", 3);
    public static final Function FIND = new Function(124, "find", Light.MAIN_KEY_MAX);
    public static final Function FINDB = new Function(205, "findb", Light.MAIN_KEY_MAX);
    public static final Function FINV = new Function(282, "finv", 3);
    public static final Function FISHER = new Function(283, "fisher", 1);
    public static final Function FISHERINV = new Function(284, "fisherinv", 1);
    public static final Function FIXED = new Function(14, "fixed", Light.MAIN_KEY_MAX);
    public static final Function FLOOR = new Function(285, "floor", 2);
    public static final Function FORECAST = new Function(309, "forecast", Light.MAIN_KEY_MAX);
    public static final Function FTEST = new Function(310, "ftest", Light.MAIN_KEY_MAX);
    public static final Function FV = new Function(57, "fv", Light.MAIN_KEY_MAX);
    public static final Function GAMMADIST = new Function(286, "gammadist", 4);
    public static final Function GAMMAINV = new Function(287, "gammainv", 3);
    public static final Function GAMMALN = new Function(271, "gammaln", 1);
    public static final Function GEOMEAN = new Function(319, "geomean", Light.MAIN_KEY_MAX);
    public static final Function GROWTH = new Function(52, "growth", Light.MAIN_KEY_MAX);
    public static final Function HARMEAN = new Function(320, "harmean", Light.MAIN_KEY_MAX);
    public static final Function HLOOKUP = new Function(101, "hlookup", Light.MAIN_KEY_MAX);
    public static final Function HOUR = new Function(71, "hour", 1);
    public static final Function HYPERLINK = new Function(359, "hyperlink", 2);
    public static final Function HYPGEOMDIST = new Function(289, "hypgeomdist", 4);
    public static final Function IF = new Function(65534, "if", Light.MAIN_KEY_MAX);
    public static final Function INDEX = new Function(29, "index", 3);
    public static final Function INDIRECT = new Function(148, "indirect", Light.MAIN_KEY_MAX);
    public static final Function INFO = new Function(244, "info", 1);
    public static final Function INT = new Function(25, "int", 1);
    public static final Function INTERCEPT = new Function(311, "intercept", Light.MAIN_KEY_MAX);
    public static final Function IPMT = new Function(167, "ipmt", Light.MAIN_KEY_MAX);
    public static final Function ISBLANK = new Function(129, "isblank", 1);
    public static final Function ISERR = new Function(126, "iserr", 1);
    public static final Function ISERROR = new Function(3, "iserror", 1);
    public static final Function ISLOGICAL = new Function(198, "islogical", 1);
    public static final Function ISNA = new Function(2, "isna", 1);
    public static final Function ISNONTEXT = new Function(190, "isnontext", 1);
    public static final Function ISNUMBER = new Function(128, "isnumber", 1);
    public static final Function ISREF = new Function(105, "isref", 1);
    public static final Function ISTEXT = new Function(127, "istext", 1);
    public static final Function KURT = new Function(322, "kurt", Light.MAIN_KEY_MAX);
    public static final Function LARGE = new Function(325, "large", Light.MAIN_KEY_MAX);
    public static final Function LEFT = new Function(115, "left", Light.MAIN_KEY_MAX);
    public static final Function LEFTB = new Function(208, "leftb", Light.MAIN_KEY_MAX);
    public static final Function LEN = new Function(32, "len", 1);
    public static final Function LENB = new Function(211, "lenb", 1);
    public static final Function LINEST = new Function(49, "linest", Light.MAIN_KEY_MAX);
    public static final Function LN = new Function(22, "ln", 1);
    public static final Function LOG = new Function(109, "log", Light.MAIN_KEY_MAX);
    public static final Function LOG10 = new Function(23, "log10", 1);
    public static final Function LOGEST = new Function(51, "logest", Light.MAIN_KEY_MAX);
    public static final Function LOGINV = new Function(291, "loginv", 3);
    public static final Function LOGNORMDIST = new Function(290, "lognormdist", 3);
    public static final Function LOOKUP = new Function(28, "lookup", 2);
    public static final Function LOWER = new Function(112, "lower", 1);
    public static final Function MATCH = new Function(64, "match", 3);
    public static final Function MAX = new Function(7, "max", Light.MAIN_KEY_MAX);
    public static final Function MAXA = new Function(362, "maxa", Light.MAIN_KEY_MAX);
    public static final Function MDETERM = new Function(163, "mdeterm", Light.MAIN_KEY_MAX);
    public static final Function MEDIAN = new Function(227, "median", Light.MAIN_KEY_MAX);
    public static final Function MID = new Function(31, "mid", 3);
    public static final Function MIDB = new Function(210, "midb", 3);
    public static final Function MIN = new Function(6, "min", Light.MAIN_KEY_MAX);
    public static final Function MINA = new Function(363, "mina", Light.MAIN_KEY_MAX);
    public static final Function MINUTE = new Function(72, "minute", 1);
    public static final Function MINVERSE = new Function(164, "minverse", Light.MAIN_KEY_MAX);
    public static final Function MMULT = new Function(165, "mmult", Light.MAIN_KEY_MAX);
    public static final Function MOD = new Function(39, "mod", 2);
    public static final Function MODE = new Function(330, "mode", Light.MAIN_KEY_MAX);
    public static final Function MONTH = new Function(68, "month", 1);
    public static final Function N = new Function(131, "n", 1);
    public static final Function NA = new Function(10, "na", 0);
    public static final Function NEGBINOMDIST = new Function(292, "negbinomdist", 3);
    public static final Function NORMDIST = new Function(293, "normdist", 4);
    public static final Function NORMINV = new Function(295, "norminv", 3);
    public static final Function NORMSDIST = new Function(294, "normsdist", 1);
    public static final Function NORMSINV = new Function(296, "normsinv", 1);
    public static final Function NOT = new Function(38, "not", 1);
    public static final Function NOW = new Function(74, "now", 0);
    public static final Function NPER = new Function(58, "nper", Light.MAIN_KEY_MAX);
    public static final Function NPV = new Function(11, "npv", Light.MAIN_KEY_MAX);
    public static final Function ODAY = new Function(221, "today", 0);
    public static final Function ODD = new Function(298, "odd", 1);
    public static final Function OFFSET = new Function(78, "offset", Light.MAIN_KEY_MAX);
    public static final Function OR = new Function(37, "or", Light.MAIN_KEY_MAX);
    public static final Function PEARSON = new Function(312, "pearson", Light.MAIN_KEY_MAX);
    public static final Function PERCENTILE = new Function(328, "percentile", Light.MAIN_KEY_MAX);
    public static final Function PERCENTRANK = new Function(329, "percentrank", Light.MAIN_KEY_MAX);
    public static final Function PERMUT = new Function(299, "permut", 2);
    public static final Function PI = new Function(19, "pi", 0);
    public static final Function PMT = new Function(59, "pmt", Light.MAIN_KEY_MAX);
    public static final Function POISSON = new Function(300, "poisson", 3);
    public static final Function POWER = new Function(337, "power", 2);
    public static final Function PPMT = new Function(168, "ppmt", Light.MAIN_KEY_MAX);
    public static final Function PROB = new Function(317, "prob", Light.MAIN_KEY_MAX);
    public static final Function PRODUCT = new Function(183, "product", Light.MAIN_KEY_MAX);
    public static final Function PROPER = new Function(114, "proper", 1);
    public static final Function PV = new Function(56, "pv", Light.MAIN_KEY_MAX);
    public static final Function QUARTILE = new Function(327, "quartile", Light.MAIN_KEY_MAX);
    public static final Function RADIANS = new Function(342, "radians", 1);
    public static final Function RAND = new Function(63, "rand", 0);
    public static final Function RANK = new Function(216, "rank", Light.MAIN_KEY_MAX);
    public static final Function RATE = new Function(60, "rate", Light.MAIN_KEY_MAX);
    public static final Function REPLACE = new Function(119, "replace", 4);
    public static final Function REPLACEB = new Function(207, "replaceb", 4);
    public static final Function REPT = new Function(30, "rept", 2);
    public static final Function RIGHT = new Function(116, "right", Light.MAIN_KEY_MAX);
    public static final Function RIGHTB = new Function(209, "rightb", Light.MAIN_KEY_MAX);
    public static final Function ROUND = new Function(27, "round", 2);
    public static final Function ROUNDDOWN = new Function(213, "rounddown", 2);
    public static final Function ROUNDUP = new Function(212, "roundup", 2);
    public static final Function ROW = new Function(8, "row", Light.MAIN_KEY_MAX);
    public static final Function ROWS = new Function(76, "rows", 1);
    public static final Function RSQ = new Function(313, "rsq", Light.MAIN_KEY_MAX);
    public static final Function SEARCH = new Function(82, "search", Light.MAIN_KEY_MAX);
    public static final Function SEARCHB = new Function(206, "searchb", 3);
    public static final Function SECOND = new Function(73, "second", 1);
    public static final Function SIGN = new Function(26, "sign", 1);
    public static final Function SIN = new Function(15, "sin", 1);
    public static final Function SINH = new Function(229, "sinh", 1);
    public static final Function SKEW = new Function(323, "skew", Light.MAIN_KEY_MAX);
    public static final Function SLN = new Function(142, "sln", 3);
    public static final Function SLOPE = new Function(315, "slope", 2);
    public static final Function SMALL = new Function(326, "small", Light.MAIN_KEY_MAX);
    public static final Function SQRT = new Function(20, "sqrt", 1);
    public static final Function STANDARDIZE = new Function(297, "standardize", 3);
    public static final Function STDEV = new Function(12, "stdev", Light.MAIN_KEY_MAX);
    public static final Function STDEVA = new Function(366, "stdeva", Light.MAIN_KEY_MAX);
    public static final Function STDEVP = new Function(193, "stdevp", Light.MAIN_KEY_MAX);
    public static final Function STDEVPA = new Function(364, "stdevpa", Light.MAIN_KEY_MAX);
    public static final Function STEYX = new Function(314, "steyx", Light.MAIN_KEY_MAX);
    public static final Function SUBSTITUTE = new Function(120, "substitute", Light.MAIN_KEY_MAX);
    public static final Function SUBTOTAL = new Function(344, "subtotal", Light.MAIN_KEY_MAX);
    public static final Function SUM = new Function(4, "sum", Light.MAIN_KEY_MAX);
    public static final Function SUMIF = new Function(345, "sumif", Light.MAIN_KEY_MAX);
    public static final Function SUMPRODUCT = new Function(228, "sumproduct", Light.MAIN_KEY_MAX);
    public static final Function SUMSQ = new Function(321, "sumsq", Light.MAIN_KEY_MAX);
    public static final Function SUMX2MY2 = new Function(304, "sumx2my2", Light.MAIN_KEY_MAX);
    public static final Function SUMX2PY2 = new Function(305, "sumx2py2", Light.MAIN_KEY_MAX);
    public static final Function SUMXMY2 = new Function(303, "sumxmy2", Light.MAIN_KEY_MAX);
    public static final Function SYD = new Function(143, "syd", 3);
    public static final Function T = new Function(130, "t", 1);
    public static final Function TAN = new Function(17, "tan", 1);
    public static final Function TANH = new Function(231, "tanh", 1);
    public static final Function TDIST = new Function(301, "tdist", 3);
    public static final Function TEXT = new Function(48, "text", 2);
    public static final Function TIME = new Function(66, "time", 3);
    public static final Function TIMEVALUE = new Function(141, "timevalue", 1);
    public static final Function TINV = new Function(332, "tinv", 2);
    public static final Function TRANSPOSE = new Function(83, "transpose", Light.MAIN_KEY_MAX);
    public static final Function TREND = new Function(50, "trend", Light.MAIN_KEY_MAX);
    public static final Function TRIM = new Function(118, "trim", 1);
    public static final Function TRIMMEAN = new Function(331, "trimmean", Light.MAIN_KEY_MAX);
    public static final Function TRUE = new Function(34, "true", 0);
    public static final Function TRUNC = new Function(197, "trunc", Light.MAIN_KEY_MAX);
    public static final Function TTEST = new Function(316, "ttest", Light.MAIN_KEY_MAX);
    public static final Function TYPE = new Function(86, "type", 1);
    public static final Function UNKNOWN = new Function(65535, "", 0);
    public static final Function UPPER = new Function(113, "upper", 1);
    public static final Function VALUE = new Function(33, "value", 1);
    public static final Function VAR = new Function(46, "var", Light.MAIN_KEY_MAX);
    public static final Function VARA = new Function(367, "vara", Light.MAIN_KEY_MAX);
    public static final Function VARP = new Function(194, "varp", Light.MAIN_KEY_MAX);
    public static final Function VARPA = new Function(365, "varpa", Light.MAIN_KEY_MAX);
    public static final Function VDB = new Function(222, "vdb", Light.MAIN_KEY_MAX);
    public static final Function VLOOKUP = new Function(102, "vlookup", Light.MAIN_KEY_MAX);
    public static final Function WEEKDAY = new Function(70, "weekday", 2);
    public static final Function WEIBULL = new Function(302, "weibull", 4);
    public static final Function YEAR = new Function(69, "year", 1);
    public static final Function ZTEST = new Function(324, "ztest", Light.MAIN_KEY_MAX);
    private static Function[] functions = new Function[0];
    private static Logger logger = Logger.getLogger(Function.class);
    private final int code;
    private final String name;
    private final int numArgs;

    private Function(int v, String s, int a) {
        this.code = v;
        this.name = s;
        this.numArgs = a;
        Function[] newarray = new Function[(functions.length + 1)];
        System.arraycopy(functions, 0, newarray, 0, functions.length);
        newarray[functions.length] = this;
        functions = newarray;
    }

    public int hashCode() {
        return this.code;
    }

    int getCode() {
        return this.code;
    }

    String getPropertyName() {
        return this.name;
    }

    String getName(WorkbookSettings ws) {
        return ws.getFunctionNames().getName(this);
    }

    int getNumArgs() {
        return this.numArgs;
    }

    public static Function getFunction(int v) {
        Function f = null;
        for (int i = 0; i < functions.length; i++) {
            if (functions[i].code == v) {
                f = functions[i];
                break;
            }
        }
        return f == null ? UNKNOWN : f;
    }

    public static Function getFunction(String v, WorkbookSettings ws) {
        Function f = ws.getFunctionNames().getFunction(v);
        return f == null ? UNKNOWN : f;
    }

    static Function[] getFunctions() {
        return functions;
    }
}
