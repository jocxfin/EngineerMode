package jxl.biff.formula;

import java.util.Stack;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class Attribute extends Operator {
    private static Logger logger = Logger.getLogger(Attribute.class);
    private VariableArgFunction ifConditions;
    private int options;
    private WorkbookSettings settings;
    private int word;

    public Attribute(WorkbookSettings ws) {
        this.settings = ws;
    }

    public Attribute(StringFunction sf, WorkbookSettings ws) {
        this.settings = ws;
        if (sf.getFunction(this.settings) == Function.SUM) {
            this.options |= 16;
        } else if (sf.getFunction(this.settings) == Function.IF) {
            this.options |= 2;
        }
    }

    void setIfConditions(VariableArgFunction vaf) {
        this.ifConditions = vaf;
        this.options |= 2;
    }

    public int read(byte[] data, int pos) {
        this.options = data[pos];
        this.word = IntegerHelper.getInt(data[pos + 1], data[pos + 2]);
        if (isChoose()) {
            return ((this.word + 1) * 2) + 3;
        }
        return 3;
    }

    public boolean isSum() {
        return (this.options & 16) != 0;
    }

    public boolean isIf() {
        return (this.options & 2) != 0;
    }

    public boolean isChoose() {
        return (this.options & 4) != 0;
    }

    public void getOperands(Stack s) {
        if ((this.options & 16) != 0) {
            add((ParseItem) s.pop());
        } else if ((this.options & 2) != 0) {
            add((ParseItem) s.pop());
        }
    }

    public void getString(StringBuffer buf) {
        ParseItem[] operands;
        if ((this.options & 16) != 0) {
            operands = getOperands();
            buf.append(Function.SUM.getName(this.settings));
            buf.append('(');
            operands[0].getString(buf);
            buf.append(')');
        } else if ((this.options & 2) != 0) {
            buf.append(Function.IF.getName(this.settings));
            buf.append('(');
            operands = this.ifConditions.getOperands();
            for (int i = 0; i < operands.length - 1; i++) {
                operands[i].getString(buf);
                buf.append(',');
            }
            operands[operands.length - 1].getString(buf);
            buf.append(')');
        }
    }

    byte[] getBytes() {
        byte[] data = new byte[0];
        if (isSum()) {
            byte[] newdata;
            ParseItem[] operands = getOperands();
            for (int i = operands.length - 1; i >= 0; i--) {
                byte[] opdata = operands[i].getBytes();
                newdata = new byte[(data.length + opdata.length)];
                System.arraycopy(data, 0, newdata, 0, data.length);
                System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
                data = newdata;
            }
            newdata = new byte[(data.length + 4)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            newdata[data.length] = (byte) Token.ATTRIBUTE.getCode();
            newdata[data.length + 1] = (byte) 16;
            data = newdata;
        } else if (isIf()) {
            return getIf();
        }
        return data;
    }

    private byte[] getIf() {
        ParseItem[] operands = this.ifConditions.getOperands();
        int numArgs = operands.length;
        byte[] data = operands[0].getBytes();
        int pos = data.length;
        byte[] newdata = new byte[(data.length + 4)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        data = newdata;
        data[pos] = (byte) Token.ATTRIBUTE.getCode();
        data[pos + 1] = (byte) 2;
        int falseOffsetPos = pos + 2;
        byte[] truedata = operands[1].getBytes();
        newdata = new byte[(data.length + truedata.length)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        System.arraycopy(truedata, 0, newdata, data.length, truedata.length);
        data = newdata;
        pos = data.length;
        newdata = new byte[(data.length + 4)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        data = newdata;
        data[pos] = (byte) Token.ATTRIBUTE.getCode();
        data[pos + 1] = (byte) 8;
        int gotoEndPos = pos + 2;
        if (numArgs > 2) {
            IntegerHelper.getTwoBytes((data.length - falseOffsetPos) - 2, data, falseOffsetPos);
            byte[] falsedata = operands[numArgs - 1].getBytes();
            newdata = new byte[(data.length + falsedata.length)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            System.arraycopy(falsedata, 0, newdata, data.length, falsedata.length);
            data = newdata;
            pos = data.length;
            newdata = new byte[(data.length + 4)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            data = newdata;
            data[pos] = (byte) Token.ATTRIBUTE.getCode();
            data[pos + 1] = (byte) 8;
            data[pos + 2] = (byte) 3;
        }
        pos = data.length;
        newdata = new byte[(data.length + 4)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        data = newdata;
        data[pos] = (byte) Token.FUNCTIONVARARG.getCode();
        data[pos + 1] = (byte) ((byte) numArgs);
        data[pos + 2] = (byte) 1;
        data[pos + 3] = (byte) 0;
        int endPos = data.length - 1;
        if (numArgs < 3) {
            IntegerHelper.getTwoBytes((endPos - falseOffsetPos) - 5, data, falseOffsetPos);
        }
        IntegerHelper.getTwoBytes((endPos - gotoEndPos) - 2, data, gotoEndPos);
        return data;
    }

    int getPrecedence() {
        return 3;
    }
}
