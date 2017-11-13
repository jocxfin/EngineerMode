package jxl.biff;

import java.io.UnsupportedEncodingException;

import jxl.WorkbookSettings;
import jxl.common.Logger;

public final class StringHelper {
    public static String UNICODE_ENCODING = "UnicodeLittle";
    private static Logger logger = Logger.getLogger(StringHelper.class);

    private StringHelper() {
    }

    public static byte[] getBytes(String s) {
        return s.getBytes();
    }

    public static byte[] getBytes(String s, WorkbookSettings ws) {
        try {
            return s.getBytes(ws.getEncoding());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getUnicodeBytes(String s) {
        try {
            byte[] b = s.getBytes(UNICODE_ENCODING);
            if (b.length == (s.length() * 2) + 2) {
                byte[] b2 = new byte[(b.length - 2)];
                System.arraycopy(b, 2, b2, 0, b2.length);
                b = b2;
            }
            return b;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void getBytes(String s, byte[] d, int pos) {
        byte[] b = getBytes(s);
        System.arraycopy(b, 0, d, pos, b.length);
    }

    public static void getUnicodeBytes(String s, byte[] d, int pos) {
        byte[] b = getUnicodeBytes(s);
        System.arraycopy(b, 0, d, pos, b.length);
    }

    public static String getString(byte[] d, int length, int pos, WorkbookSettings ws) {
        if (length == 0) {
            return "";
        }
        try {
            return new String(d, pos, length, ws.getEncoding());
        } catch (UnsupportedEncodingException e) {
            logger.warn(e.toString());
            return "";
        }
    }

    public static String getUnicodeString(byte[] d, int length, int pos) {
        try {
            byte[] b = new byte[(length * 2)];
            System.arraycopy(d, pos, b, 0, length * 2);
            return new String(b, UNICODE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static final String replace(String input, String search, String replace) {
        String fmtstr = input;
        int pos = fmtstr.indexOf(search);
        while (pos != -1) {
            StringBuffer tmp = new StringBuffer(fmtstr.substring(0, pos));
            tmp.append(replace);
            tmp.append(fmtstr.substring(search.length() + pos));
            fmtstr = tmp.toString();
            pos = fmtstr.indexOf(search, replace.length() + pos);
        }
        return fmtstr;
    }
}
