package jxl.biff;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import jxl.WorkbookSettings;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;
import jxl.biff.formula.FormulaParser;
import jxl.biff.formula.ParseContext;
import jxl.common.Assert;
import jxl.common.Logger;

public class DVParser {
    public static final DVType ANY = new DVType(0, "any");
    public static final Condition BETWEEN = new Condition(0, "{0} <= x <= {1}");
    public static final DVType DATE = new DVType(4, "date");
    public static final DVType DECIMAL = new DVType(2, "dec");
    private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    public static final Condition EQUAL = new Condition(2, "x == {0}");
    public static final DVType FORMULA = new DVType(7, "form");
    public static final Condition GREATER_EQUAL = new Condition(6, "x >= {0}");
    public static final Condition GREATER_THAN = new Condition(4, "x > {0}");
    public static final ErrorStyle INFO = new ErrorStyle(2);
    public static final DVType INTEGER = new DVType(1, "int");
    public static final Condition LESS_EQUAL = new Condition(7, "x <= {0}");
    public static final Condition LESS_THAN = new Condition(5, "x < {0}");
    public static final DVType LIST = new DVType(3, "list");
    public static final Condition NOT_BETWEEN = new Condition(1, "!({0} <= x <= {1}");
    public static final Condition NOT_EQUAL = new Condition(3, "x != {0}");
    public static final ErrorStyle STOP = new ErrorStyle(0);
    public static final DVType TEXT_LENGTH = new DVType(6, "strlen");
    public static final DVType TIME = new DVType(5, "time");
    public static final ErrorStyle WARNING = new ErrorStyle(1);
    private static Logger logger = Logger.getLogger(DVParser.class);
    private int column1;
    private int column2;
    private Condition condition;
    private boolean copied;
    private boolean emptyCellsAllowed;
    private ErrorStyle errorStyle;
    private String errorText;
    private String errorTitle;
    private boolean extendedCellsValidation;
    private FormulaParser formula1;
    private String formula1String;
    private FormulaParser formula2;
    private String formula2String;
    private String promptText;
    private String promptTitle;
    private int row1;
    private int row2;
    private boolean showError;
    private boolean showPrompt;
    private boolean stringListGiven;
    private boolean suppressArrow;
    private DVType type;

    public static class Condition {
        private static Condition[] types = new Condition[0];
        private MessageFormat format;
        private int value;

        Condition(int v, String pattern) {
            this.value = v;
            this.format = new MessageFormat(pattern);
            Condition[] oldtypes = types;
            types = new Condition[(oldtypes.length + 1)];
            System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
            types[oldtypes.length] = this;
        }

        static Condition getCondition(int v) {
            Condition found = null;
            for (int i = 0; i < types.length && found == null; i++) {
                if (types[i].value == v) {
                    found = types[i];
                }
            }
            return found;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static class DVType {
        private static DVType[] types = new DVType[0];
        private String desc;
        private int value;

        DVType(int v, String d) {
            this.value = v;
            this.desc = d;
            DVType[] oldtypes = types;
            types = new DVType[(oldtypes.length + 1)];
            System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
            types[oldtypes.length] = this;
        }

        static DVType getType(int v) {
            DVType found = null;
            for (int i = 0; i < types.length && found == null; i++) {
                if (types[i].value == v) {
                    found = types[i];
                }
            }
            return found;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static class ErrorStyle {
        private static ErrorStyle[] types = new ErrorStyle[0];
        private int value;

        ErrorStyle(int v) {
            this.value = v;
            ErrorStyle[] oldtypes = types;
            types = new ErrorStyle[(oldtypes.length + 1)];
            System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
            types[oldtypes.length] = this;
        }

        static ErrorStyle getErrorStyle(int v) {
            ErrorStyle found = null;
            for (int i = 0; i < types.length && found == null; i++) {
                if (types[i].value == v) {
                    found = types[i];
                }
            }
            return found;
        }

        public int getValue() {
            return this.value;
        }
    }

    public DVParser(byte[] data, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws) {
        boolean z;
        int pos;
        if (nt == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        this.copied = false;
        int options = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
        this.type = DVType.getType(options & 15);
        this.errorStyle = ErrorStyle.getErrorStyle((options & 112) >> 4);
        this.condition = Condition.getCondition((15728640 & options) >> 20);
        if ((options & 128) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.stringListGiven = z;
        if ((options & 256) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.emptyCellsAllowed = z;
        if ((options & 512) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.suppressArrow = z;
        if ((262144 & options) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.showPrompt = z;
        if ((524288 & options) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.showError = z;
        int length = IntegerHelper.getInt(data[4], data[5]);
        if (length > 0 && data[6] == (byte) 0) {
            this.promptTitle = StringHelper.getString(data, length, 7, ws);
            pos = (length + 3) + 4;
        } else if (length <= 0) {
            pos = 7;
        } else {
            this.promptTitle = StringHelper.getUnicodeString(data, length, 7);
            pos = ((length * 2) + 3) + 4;
        }
        length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        if (length > 0 && data[pos + 2] == (byte) 0) {
            this.errorTitle = StringHelper.getString(data, length, pos + 3, ws);
            pos += length + 3;
        } else if (length <= 0) {
            pos += 3;
        } else {
            this.errorTitle = StringHelper.getUnicodeString(data, length, pos + 3);
            pos += (length * 2) + 3;
        }
        length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        if (length > 0 && data[pos + 2] == (byte) 0) {
            this.promptText = StringHelper.getString(data, length, pos + 3, ws);
            pos += length + 3;
        } else if (length <= 0) {
            pos += 3;
        } else {
            this.promptText = StringHelper.getUnicodeString(data, length, pos + 3);
            pos += (length * 2) + 3;
        }
        length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        if (length > 0 && data[pos + 2] == (byte) 0) {
            this.errorText = StringHelper.getString(data, length, pos + 3, ws);
            pos += length + 3;
        } else if (length <= 0) {
            pos += 3;
        } else {
            this.errorText = StringHelper.getUnicodeString(data, length, pos + 3);
            pos += (length * 2) + 3;
        }
        int formula1Length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 4;
        int formula1Pos = pos;
        pos += formula1Length;
        int formula2Length = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 4;
        int formula2Pos = pos;
        pos = (pos + formula2Length) + 2;
        this.row1 = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 2;
        this.row2 = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 2;
        this.column1 = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 2;
        this.column2 = IntegerHelper.getInt(data[pos], data[pos + 1]);
        pos += 2;
        if (this.row1 == this.row2 && this.column1 == this.column2) {
            z = false;
        } else {
            z = true;
        }
        this.extendedCellsValidation = z;
        try {
            byte[] tokens;
            EmptyCell tmprt = new EmptyCell(this.column1, this.row1);
            if (formula1Length != 0) {
                tokens = new byte[formula1Length];
                System.arraycopy(data, formula1Pos, tokens, 0, formula1Length);
                this.formula1 = new FormulaParser(tokens, tmprt, es, nt, ws, ParseContext.DATA_VALIDATION);
                this.formula1.parse();
            }
            if (formula2Length != 0) {
                tokens = new byte[formula2Length];
                System.arraycopy(data, formula2Pos, tokens, 0, formula2Length);
                this.formula2 = new FormulaParser(tokens, tmprt, es, nt, ws, ParseContext.DATA_VALIDATION);
                this.formula2.parse();
            }
        } catch (FormulaException e) {
            logger.warn(e.getMessage() + " for cells " + CellReferenceHelper.getCellReference(this.column1, this.row1) + "-" + CellReferenceHelper.getCellReference(this.column2, this.row2));
        }
    }

    public DVParser(DVParser copy) {
        String str = null;
        this.copied = true;
        this.type = copy.type;
        this.errorStyle = copy.errorStyle;
        this.condition = copy.condition;
        this.stringListGiven = copy.stringListGiven;
        this.emptyCellsAllowed = copy.emptyCellsAllowed;
        this.suppressArrow = copy.suppressArrow;
        this.showPrompt = copy.showPrompt;
        this.showError = copy.showError;
        this.promptTitle = copy.promptTitle;
        this.promptText = copy.promptText;
        this.errorTitle = copy.errorTitle;
        this.errorText = copy.errorText;
        this.extendedCellsValidation = copy.extendedCellsValidation;
        this.row1 = copy.row1;
        this.row2 = copy.row2;
        this.column1 = copy.column1;
        this.column2 = copy.column2;
        if (copy.formula1String == null) {
            try {
                this.formula1String = copy.formula1.getFormula();
                if (copy.formula2 != null) {
                    str = copy.formula2.getFormula();
                }
                this.formula2String = str;
                return;
            } catch (FormulaException e) {
                logger.warn("Cannot parse validation formula:  " + e.getMessage());
                return;
            }
        }
        this.formula1String = copy.formula1String;
        this.formula2String = copy.formula2String;
    }

    public byte[] getData() {
        byte[] f2Bytes;
        byte[] f1Bytes = this.formula1 == null ? new byte[0] : this.formula1.getBytes();
        if (this.formula2 == null) {
            f2Bytes = new byte[0];
        } else {
            f2Bytes = this.formula2.getBytes();
        }
        byte[] data = new byte[(((((((((((((((this.promptTitle.length() * 2) + 4) + 3) + (this.errorTitle.length() * 2)) + 3) + (this.promptText.length() * 2)) + 3) + (this.errorText.length() * 2)) + 3) + f1Bytes.length) + 2) + f2Bytes.length) + 2) + 4) + 10)];
        int options = ((this.type.getValue() | 0) | (this.errorStyle.getValue() << 4)) | (this.condition.getValue() << 20);
        if (this.stringListGiven) {
            options |= 128;
        }
        if (this.emptyCellsAllowed) {
            options |= 256;
        }
        if (this.suppressArrow) {
            options |= 512;
        }
        if (this.showPrompt) {
            options |= 262144;
        }
        if (this.showError) {
            options |= 524288;
        }
        IntegerHelper.getFourBytes(options, data, 0);
        IntegerHelper.getTwoBytes(this.promptTitle.length(), data, 4);
        int pos = 4 + 2;
        data[pos] = (byte) 1;
        StringHelper.getUnicodeBytes(this.promptTitle, data, pos + 1);
        pos = (this.promptTitle.length() * 2) + 7;
        IntegerHelper.getTwoBytes(this.errorTitle.length(), data, pos);
        pos += 2;
        data[pos] = (byte) 1;
        pos++;
        StringHelper.getUnicodeBytes(this.errorTitle, data, pos);
        pos += this.errorTitle.length() * 2;
        IntegerHelper.getTwoBytes(this.promptText.length(), data, pos);
        pos += 2;
        data[pos] = (byte) 1;
        pos++;
        StringHelper.getUnicodeBytes(this.promptText, data, pos);
        pos += this.promptText.length() * 2;
        IntegerHelper.getTwoBytes(this.errorText.length(), data, pos);
        pos += 2;
        data[pos] = (byte) 1;
        pos++;
        StringHelper.getUnicodeBytes(this.errorText, data, pos);
        pos += this.errorText.length() * 2;
        IntegerHelper.getTwoBytes(f1Bytes.length, data, pos);
        pos += 4;
        System.arraycopy(f1Bytes, 0, data, pos, f1Bytes.length);
        pos += f1Bytes.length;
        IntegerHelper.getTwoBytes(f2Bytes.length, data, pos);
        pos += 4;
        System.arraycopy(f2Bytes, 0, data, pos, f2Bytes.length);
        pos += f2Bytes.length;
        IntegerHelper.getTwoBytes(1, data, pos);
        pos += 2;
        IntegerHelper.getTwoBytes(this.row1, data, pos);
        pos += 2;
        IntegerHelper.getTwoBytes(this.row2, data, pos);
        pos += 2;
        IntegerHelper.getTwoBytes(this.column1, data, pos);
        pos += 2;
        IntegerHelper.getTwoBytes(this.column2, data, pos);
        pos += 2;
        return data;
    }

    public int getFirstColumn() {
        return this.column1;
    }

    public int getLastColumn() {
        return this.column2;
    }

    public int getFirstRow() {
        return this.row1;
    }

    public int getLastRow() {
        return this.row2;
    }

    public void setCell(int col, int row, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws) throws FormulaException {
        if (!this.extendedCellsValidation) {
            this.row1 = row;
            this.row2 = row;
            this.column1 = col;
            this.column2 = col;
            this.formula1 = new FormulaParser(this.formula1String, es, nt, ws, ParseContext.DATA_VALIDATION);
            this.formula1.parse();
            if (this.formula2String != null) {
                this.formula2 = new FormulaParser(this.formula2String, es, nt, ws, ParseContext.DATA_VALIDATION);
                this.formula2.parse();
            }
        }
    }

    public boolean extendedCellsValidation() {
        return this.extendedCellsValidation;
    }

    public boolean copied() {
        return this.copied;
    }
}
