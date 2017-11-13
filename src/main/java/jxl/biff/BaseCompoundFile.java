package jxl.biff;

import jxl.common.Assert;
import jxl.common.Logger;

public abstract class BaseCompoundFile {
    protected static final byte[] IDENTIFIER = new byte[]{(byte) -48, (byte) -49, (byte) 17, (byte) -32, (byte) -95, (byte) -79, (byte) 26, (byte) -31};
    public static final String[] STANDARD_PROPERTY_SETS = new String[]{"Root Entry", "Workbook", "\u0005SummaryInformation", "\u0005DocumentSummaryInformation"};
    private static Logger logger = Logger.getLogger(BaseCompoundFile.class);

    public class PropertyStorage {
        public int child;
        public int colour;
        public byte[] data;
        public String name;
        public int next;
        public int previous;
        public int size;
        public int startBlock;
        public int type;

        public PropertyStorage(byte[] d) {
            this.data = d;
            int nameSize = IntegerHelper.getInt(this.data[64], this.data[65]);
            if (nameSize > 64) {
                BaseCompoundFile.logger.warn("property set name exceeds max length - truncating");
                nameSize = 64;
            }
            this.type = this.data[66];
            this.colour = this.data[67];
            this.startBlock = IntegerHelper.getInt(this.data[116], this.data[117], this.data[118], this.data[119]);
            this.size = IntegerHelper.getInt(this.data[120], this.data[121], this.data[122], this.data[123]);
            this.previous = IntegerHelper.getInt(this.data[68], this.data[69], this.data[70], this.data[71]);
            this.next = IntegerHelper.getInt(this.data[72], this.data[73], this.data[74], this.data[75]);
            this.child = IntegerHelper.getInt(this.data[76], this.data[77], this.data[78], this.data[79]);
            int chars = 0;
            if (nameSize > 2) {
                chars = (nameSize - 1) / 2;
            }
            StringBuffer n = new StringBuffer("");
            for (int i = 0; i < chars; i++) {
                n.append((char) this.data[i * 2]);
            }
            this.name = n.toString();
        }

        public PropertyStorage(String name) {
            boolean z;
            this.data = new byte[128];
            if (name.length() >= 32) {
                z = false;
            } else {
                z = true;
            }
            Assert.verify(z);
            IntegerHelper.getTwoBytes((name.length() + 1) * 2, this.data, 64);
            for (int i = 0; i < name.length(); i++) {
                this.data[i * 2] = (byte) ((byte) name.charAt(i));
            }
        }

        public void setType(int t) {
            this.type = t;
            this.data[66] = (byte) ((byte) t);
        }

        public void setStartBlock(int sb) {
            this.startBlock = sb;
            IntegerHelper.getFourBytes(sb, this.data, 116);
        }

        public void setSize(int s) {
            this.size = s;
            IntegerHelper.getFourBytes(s, this.data, 120);
        }

        public void setPrevious(int prev) {
            this.previous = prev;
            IntegerHelper.getFourBytes(prev, this.data, 68);
        }

        public void setNext(int nxt) {
            this.next = nxt;
            IntegerHelper.getFourBytes(this.next, this.data, 72);
        }

        public void setChild(int dir) {
            this.child = dir;
            IntegerHelper.getFourBytes(this.child, this.data, 76);
        }

        public void setColour(int col) {
            int i = 0;
            if (col != 0) {
                i = 1;
            }
            this.colour = i;
            this.data[67] = (byte) ((byte) this.colour);
        }
    }

    protected BaseCompoundFile() {
    }
}
