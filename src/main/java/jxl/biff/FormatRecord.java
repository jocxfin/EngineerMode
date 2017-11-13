package jxl.biff;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import jxl.WorkbookSettings;
import jxl.common.Logger;
import jxl.format.Format;
import jxl.read.biff.Record;

public class FormatRecord extends WritableRecordData implements DisplayFormat, Format {
    public static final BiffType biff7 = new BiffType();
    public static final BiffType biff8 = new BiffType();
    private static String[] dateStrings = new String[]{"dd", "mm", "yy", "hh", "ss", "m/", "/d"};
    public static Logger logger = Logger.getLogger(FormatRecord.class);
    private byte[] data;
    private boolean date;
    private java.text.Format format;
    private String formatString;
    private int indexCode;
    private boolean initialized = false;
    private boolean number;

    private static class BiffType {
        private BiffType() {
        }
    }

    protected FormatRecord() {
        super(Type.FORMAT);
    }

    public FormatRecord(Record t, WorkbookSettings ws, BiffType biffType) {
        super(t);
        byte[] data = getRecord().getData();
        this.indexCode = IntegerHelper.getInt(data[0], data[1]);
        if (biffType != biff8) {
            byte[] chars = new byte[data[2]];
            System.arraycopy(data, 3, chars, 0, chars.length);
            this.formatString = new String(chars);
        } else {
            int numchars = IntegerHelper.getInt(data[2], data[3]);
            if (data[4] != (byte) 0) {
                this.formatString = StringHelper.getUnicodeString(data, numchars, 5);
            } else {
                this.formatString = StringHelper.getString(data, numchars, 5, ws);
            }
        }
        this.date = false;
        this.number = false;
        for (String dateString : dateStrings) {
            if (this.formatString.indexOf(dateString) != -1 || this.formatString.indexOf(dateString.toUpperCase()) != -1) {
                this.date = true;
                break;
            }
        }
        if (!this.date) {
            if (this.formatString.indexOf(35) != -1 || this.formatString.indexOf(48) != -1) {
                this.number = true;
            }
        }
    }

    public byte[] getData() {
        this.data = new byte[(((this.formatString.length() * 2) + 3) + 2)];
        IntegerHelper.getTwoBytes(this.indexCode, this.data, 0);
        IntegerHelper.getTwoBytes(this.formatString.length(), this.data, 2);
        this.data[4] = (byte) 1;
        StringHelper.getUnicodeBytes(this.formatString, this.data, 5);
        return this.data;
    }

    public int getFormatIndex() {
        return this.indexCode;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void initialize(int pos) {
        this.indexCode = pos;
        this.initialized = true;
    }

    protected final String replace(String input, String search, String replace) {
        String fmtstr = input;
        int pos = fmtstr.indexOf(search);
        while (pos != -1) {
            StringBuffer tmp = new StringBuffer(fmtstr.substring(0, pos));
            tmp.append(replace);
            tmp.append(fmtstr.substring(search.length() + pos));
            fmtstr = tmp.toString();
            pos = fmtstr.indexOf(search);
        }
        return fmtstr;
    }

    protected final void setFormatString(String s) {
        this.formatString = s;
    }

    public final boolean isDate() {
        return this.date;
    }

    public final boolean isNumber() {
        return this.number;
    }

    public final NumberFormat getNumberFormat() {
        if (this.format != null && (this.format instanceof NumberFormat)) {
            return (NumberFormat) this.format;
        }
        try {
            this.format = new DecimalFormat(replace(replace(replace(replace(replace(this.formatString, "E+", "E"), "_)", ""), "_", ""), "[Red]", ""), "\\", ""));
        } catch (IllegalArgumentException e) {
            this.format = new DecimalFormat("#.###");
        }
        return (NumberFormat) this.format;
    }

    public final DateFormat getDateFormat() {
        if (this.format != null && (this.format instanceof DateFormat)) {
            return (DateFormat) this.format;
        }
        StringBuffer sb;
        int i;
        String fmt = this.formatString;
        int pos = fmt.indexOf("AM/PM");
        while (pos != -1) {
            sb = new StringBuffer(fmt.substring(0, pos));
            sb.append('a');
            sb.append(fmt.substring(pos + 5));
            fmt = sb.toString();
            pos = fmt.indexOf("AM/PM");
        }
        pos = fmt.indexOf("ss.0");
        while (pos != -1) {
            sb = new StringBuffer(fmt.substring(0, pos));
            sb.append("ss.SSS");
            pos += 4;
            while (pos < fmt.length() && fmt.charAt(pos) == '0') {
                pos++;
            }
            sb.append(fmt.substring(pos));
            fmt = sb.toString();
            pos = fmt.indexOf("ss.0");
        }
        sb = new StringBuffer();
        for (i = 0; i < fmt.length(); i++) {
            if (fmt.charAt(i) != '\\') {
                sb.append(fmt.charAt(i));
            }
        }
        fmt = sb.toString();
        if (fmt.charAt(0) == '[') {
            int end = fmt.indexOf(93);
            if (end != -1) {
                fmt = fmt.substring(end + 1);
            }
        }
        char[] formatBytes = replace(fmt, ";@", "").toCharArray();
        i = 0;
        while (i < formatBytes.length) {
            if (formatBytes[i] == 'm') {
                int j;
                if (i > 0) {
                    if (formatBytes[i - 1] == 'm' || formatBytes[i - 1] == 'M') {
                        formatBytes[i] = (char) formatBytes[i - 1];
                    }
                }
                int minuteDist = Integer.MAX_VALUE;
                for (j = i - 1; j > 0; j--) {
                    if (formatBytes[j] == 'h') {
                        minuteDist = i - j;
                        break;
                    }
                }
                for (j = i + 1; j < formatBytes.length; j++) {
                    if (formatBytes[j] == 'h') {
                        minuteDist = Math.min(minuteDist, j - i);
                        break;
                    }
                }
                for (j = i - 1; j > 0; j--) {
                    if (formatBytes[j] == 'H') {
                        minuteDist = i - j;
                        break;
                    }
                }
                for (j = i + 1; j < formatBytes.length; j++) {
                    if (formatBytes[j] == 'H') {
                        minuteDist = Math.min(minuteDist, j - i);
                        break;
                    }
                }
                for (j = i - 1; j > 0; j--) {
                    if (formatBytes[j] == 's') {
                        minuteDist = Math.min(minuteDist, i - j);
                        break;
                    }
                }
                for (j = i + 1; j < formatBytes.length; j++) {
                    if (formatBytes[j] == 's') {
                        minuteDist = Math.min(minuteDist, j - i);
                        break;
                    }
                }
                int monthDist = Integer.MAX_VALUE;
                for (j = i - 1; j > 0; j--) {
                    if (formatBytes[j] == 'd') {
                        monthDist = i - j;
                        break;
                    }
                }
                for (j = i + 1; j < formatBytes.length; j++) {
                    if (formatBytes[j] == 'd') {
                        monthDist = Math.min(monthDist, j - i);
                        break;
                    }
                }
                for (j = i - 1; j > 0; j--) {
                    if (formatBytes[j] == 'y') {
                        monthDist = Math.min(monthDist, i - j);
                        break;
                    }
                }
                for (j = i + 1; j < formatBytes.length; j++) {
                    if (formatBytes[j] == 'y') {
                        monthDist = Math.min(monthDist, j - i);
                        break;
                    }
                }
                if (monthDist < minuteDist) {
                    formatBytes[i] = (char) Character.toUpperCase(formatBytes[i]);
                } else if (monthDist == minuteDist && monthDist != Integer.MAX_VALUE) {
                    char ind = formatBytes[i - monthDist];
                    if (ind == 'y' || ind == 'd') {
                        formatBytes[i] = (char) Character.toUpperCase(formatBytes[i]);
                    }
                }
            }
            i++;
        }
        try {
            this.format = new SimpleDateFormat(new String(formatBytes));
        } catch (IllegalArgumentException e) {
            this.format = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        }
        return (DateFormat) this.format;
    }

    public boolean isBuiltIn() {
        return false;
    }

    public int hashCode() {
        return this.formatString.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FormatRecord)) {
            return false;
        }
        FormatRecord fr = (FormatRecord) o;
        if (!this.initialized || !fr.initialized) {
            return this.formatString.equals(fr.formatString);
        }
        if (this.date == fr.date && this.number == fr.number) {
            return this.formatString.equals(fr.formatString);
        }
        return false;
    }
}
