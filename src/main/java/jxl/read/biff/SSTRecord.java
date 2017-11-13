package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;
import jxl.common.Assert;

class SSTRecord extends RecordData {
    private int[] continuationBreaks;
    private String[] strings;
    private int totalStrings;
    private int uniqueStrings;

    private static class BooleanHolder {
        public boolean value;

        private BooleanHolder() {
        }
    }

    private static class ByteArrayHolder {
        public byte[] bytes;

        private ByteArrayHolder() {
        }
    }

    public SSTRecord(Record t, Record[] continuations, WorkbookSettings ws) {
        int i;
        super(t);
        int totalRecordLength = 0;
        for (Record length : continuations) {
            totalRecordLength += length.getLength();
        }
        byte[] data = new byte[(totalRecordLength + getRecord().getLength())];
        System.arraycopy(getRecord().getData(), 0, data, 0, getRecord().getLength());
        int pos = getRecord().getLength() + 0;
        this.continuationBreaks = new int[continuations.length];
        for (i = 0; i < continuations.length; i++) {
            Record r = continuations[i];
            System.arraycopy(r.getData(), 0, data, pos, r.getLength());
            this.continuationBreaks[i] = pos;
            pos += r.getLength();
        }
        this.totalStrings = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
        this.uniqueStrings = IntegerHelper.getInt(data[4], data[5], data[6], data[7]);
        this.strings = new String[this.uniqueStrings];
        readStrings(data, 8, ws);
    }

    private void readStrings(byte[] data, int offset, WorkbookSettings ws) {
        int pos = offset;
        int formattingRuns = 0;
        int extendedRunLength = 0;
        for (int i = 0; i < this.uniqueStrings; i++) {
            boolean extendedString;
            boolean richString;
            boolean asciiEncoding;
            String s;
            int numChars = IntegerHelper.getInt(data[pos], data[pos + 1]);
            pos += 2;
            byte optionFlags = data[pos];
            pos++;
            if ((optionFlags & 4) == 0) {
                extendedString = false;
            } else {
                extendedString = true;
            }
            if ((optionFlags & 8) == 0) {
                richString = false;
            } else {
                richString = true;
            }
            if (richString) {
                formattingRuns = IntegerHelper.getInt(data[pos], data[pos + 1]);
                pos += 2;
            }
            if (extendedString) {
                extendedRunLength = IntegerHelper.getInt(data[pos], data[pos + 1], data[pos + 2], data[pos + 3]);
                pos += 4;
            }
            if ((optionFlags & 1) != 0) {
                asciiEncoding = false;
            } else {
                asciiEncoding = true;
            }
            ByteArrayHolder bah = new ByteArrayHolder();
            BooleanHolder bh = new BooleanHolder();
            bh.value = asciiEncoding;
            pos += getChars(data, bah, pos, bh, numChars);
            if (bh.value) {
                s = StringHelper.getString(bah.bytes, numChars, 0, ws);
            } else {
                s = StringHelper.getUnicodeString(bah.bytes, numChars, 0);
            }
            this.strings[i] = s;
            if (richString) {
                pos += formattingRuns * 4;
            }
            if (extendedString) {
                pos += extendedRunLength;
            }
            if (pos > data.length) {
                Assert.verify(false, "pos exceeds record length");
            }
        }
    }

    private int getChars(byte[] source, ByteArrayHolder bah, int pos, BooleanHolder ascii, int numChars) {
        int i = 0;
        boolean spansBreak = false;
        if (ascii.value) {
            bah.bytes = new byte[numChars];
        } else {
            bah.bytes = new byte[(numChars * 2)];
        }
        while (i < this.continuationBreaks.length && !spansBreak) {
            if (pos <= this.continuationBreaks[i] && bah.bytes.length + pos > this.continuationBreaks[i]) {
                spansBreak = true;
            } else {
                spansBreak = false;
            }
            if (!spansBreak) {
                i++;
            }
        }
        if (spansBreak) {
            int charsRead;
            int breakpos = this.continuationBreaks[i];
            System.arraycopy(source, pos, bah.bytes, 0, breakpos - pos);
            int bytesRead = breakpos - pos;
            if (ascii.value) {
                charsRead = bytesRead;
            } else {
                charsRead = bytesRead / 2;
            }
            return bytesRead + getContinuedString(source, bah, bytesRead, i, ascii, numChars - charsRead);
        }
        System.arraycopy(source, pos, bah.bytes, 0, bah.bytes.length);
        return bah.bytes.length;
    }

    private int getContinuedString(byte[] source, ByteArrayHolder bah, int destPos, int contBreakIndex, BooleanHolder ascii, int charsLeft) {
        int breakpos = this.continuationBreaks[contBreakIndex];
        int bytesRead = 0;
        while (charsLeft > 0) {
            Assert.verify(contBreakIndex < this.continuationBreaks.length, "continuation break index");
            int length;
            if (ascii.value && source[breakpos] == (byte) 0) {
                if (contBreakIndex != this.continuationBreaks.length - 1) {
                    length = Math.min(charsLeft, (this.continuationBreaks[contBreakIndex + 1] - breakpos) - 1);
                } else {
                    length = charsLeft;
                }
                System.arraycopy(source, breakpos + 1, bah.bytes, destPos, length);
                destPos += length;
                bytesRead += length + 1;
                charsLeft -= length;
                ascii.value = true;
            } else if (!ascii.value && source[breakpos] != (byte) 0) {
                length = contBreakIndex != this.continuationBreaks.length + -1 ? Math.min(charsLeft * 2, (this.continuationBreaks[contBreakIndex + 1] - breakpos) - 1) : charsLeft * 2;
                System.arraycopy(source, breakpos + 1, bah.bytes, destPos, length);
                destPos += length;
                bytesRead += length + 1;
                charsLeft -= length / 2;
                ascii.value = false;
            } else if (!ascii.value && source[breakpos] == (byte) 0) {
                int chars;
                if (contBreakIndex != this.continuationBreaks.length - 1) {
                    chars = Math.min(charsLeft, (this.continuationBreaks[contBreakIndex + 1] - breakpos) - 1);
                } else {
                    chars = charsLeft;
                }
                for (j = 0; j < chars; j++) {
                    bah.bytes[destPos] = (byte) source[(breakpos + j) + 1];
                    destPos += 2;
                }
                bytesRead += chars + 1;
                charsLeft -= chars;
                ascii.value = false;
            } else {
                byte[] oldBytes = bah.bytes;
                bah.bytes = new byte[((destPos * 2) + (charsLeft * 2))];
                for (j = 0; j < destPos; j++) {
                    bah.bytes[j * 2] = (byte) oldBytes[j];
                }
                destPos *= 2;
                length = contBreakIndex != this.continuationBreaks.length + -1 ? Math.min(charsLeft * 2, (this.continuationBreaks[contBreakIndex + 1] - breakpos) - 1) : charsLeft * 2;
                System.arraycopy(source, breakpos + 1, bah.bytes, destPos, length);
                destPos += length;
                bytesRead += length + 1;
                charsLeft -= length / 2;
                ascii.value = false;
            }
            contBreakIndex++;
            if (contBreakIndex < this.continuationBreaks.length) {
                breakpos = this.continuationBreaks[contBreakIndex];
            }
        }
        return bytesRead;
    }

    public String getString(int index) {
        boolean z;
        if (index >= this.uniqueStrings) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        return this.strings[index];
    }
}
