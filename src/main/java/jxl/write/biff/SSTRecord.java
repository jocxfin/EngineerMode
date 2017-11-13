package jxl.write.biff;

import java.util.ArrayList;
import java.util.Iterator;

import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class SSTRecord extends WritableRecordData {
    private static int maxBytes = 8216;
    private int byteCount = 0;
    private byte[] data;
    private int numReferences;
    private int numStrings;
    private ArrayList stringLengths = new ArrayList(50);
    private ArrayList strings = new ArrayList(50);

    public SSTRecord(int numRefs, int s) {
        super(Type.SST);
        this.numReferences = numRefs;
        this.numStrings = s;
    }

    public int add(String s) {
        int bytes = (s.length() * 2) + 3;
        if (this.byteCount < maxBytes - 5) {
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
        return s.length() <= 0 ? -1 : s.length();
    }

    public int getOffset() {
        return this.byteCount + 8;
    }

    public byte[] getData() {
        this.data = new byte[(this.byteCount + 8)];
        IntegerHelper.getFourBytes(this.numReferences, this.data, 0);
        IntegerHelper.getFourBytes(this.numStrings, this.data, 4);
        int pos = 8;
        int count = 0;
        Iterator i = this.strings.iterator();
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
