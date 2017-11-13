package jxl.write.biff;

import java.util.ArrayList;
import java.util.Iterator;

import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class SSTContinueRecord extends WritableRecordData {
    private static int maxBytes = 8224;
    private int byteCount = 0;
    private byte[] data;
    private String firstString;
    private int firstStringLength;
    private boolean includeLength;
    private ArrayList stringLengths = new ArrayList(50);
    private ArrayList strings = new ArrayList(50);

    public SSTContinueRecord() {
        super(Type.CONTINUE);
    }

    public int setFirstString(String s, boolean b) {
        int bytes;
        this.includeLength = b;
        this.firstStringLength = s.length();
        if (this.includeLength) {
            bytes = (s.length() * 2) + 3;
        } else {
            bytes = (s.length() * 2) + 1;
        }
        if (bytes > maxBytes) {
            int charsAvailable = !this.includeLength ? (maxBytes - 2) / 2 : (maxBytes - 4) / 2;
            this.firstString = s.substring(0, charsAvailable);
            this.byteCount = maxBytes - 1;
            return s.length() - charsAvailable;
        }
        this.firstString = s;
        this.byteCount += bytes;
        return 0;
    }

    public int getOffset() {
        return this.byteCount;
    }

    public int add(String s) {
        int bytes = (s.length() * 2) + 3;
        if (this.byteCount >= maxBytes - 5) {
            return s.length();
        }
        this.stringLengths.add(new Integer(s.length()));
        if (this.byteCount + bytes >= maxBytes) {
            int bytesLeft = (maxBytes - 3) - this.byteCount;
            int charsAvailable = bytesLeft % 2 != 0 ? (bytesLeft - 1) / 2 : bytesLeft / 2;
            this.strings.add(s.substring(0, charsAvailable));
            this.byteCount += (charsAvailable * 2) + 3;
            return s.length() - charsAvailable;
        }
        this.strings.add(s);
        this.byteCount += bytes;
        return 0;
    }

    public byte[] getData() {
        int pos;
        this.data = new byte[this.byteCount];
        if (this.includeLength) {
            IntegerHelper.getTwoBytes(this.firstStringLength, this.data, 0);
            this.data[2] = (byte) 1;
            pos = 3;
        } else {
            this.data[0] = (byte) 1;
            pos = 1;
        }
        StringHelper.getUnicodeBytes(this.firstString, this.data, pos);
        pos += this.firstString.length() * 2;
        Iterator i = this.strings.iterator();
        int count = 0;
        while (i.hasNext()) {
            String s = (String) i.next();
            IntegerHelper.getTwoBytes(((Integer) this.stringLengths.get(count)).intValue(), this.data, pos);
            this.data[pos + 2] = (byte) 1;
            StringHelper.getUnicodeBytes(s, this.data, pos + 3);
            pos += (s.length() * 2) + 3;
            count++;
        }
        return this.data;
    }
}
