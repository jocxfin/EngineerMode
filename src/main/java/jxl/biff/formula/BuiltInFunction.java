package jxl.biff.formula;

import java.util.Stack;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.common.Assert;
import jxl.common.Logger;

class BuiltInFunction extends Operator {
    private static Logger logger = Logger.getLogger(BuiltInFunction.class);
    private Function function;
    private WorkbookSettings settings;

    public BuiltInFunction(WorkbookSettings ws) {
        this.settings = ws;
    }

    public BuiltInFunction(Function f, WorkbookSettings ws) {
        this.function = f;
        this.settings = ws;
    }

    public int read(byte[] data, int pos) {
        boolean z;
        int index = IntegerHelper.getInt(data[pos], data[pos + 1]);
        this.function = Function.getFunction(index);
        if (this.function == Function.UNKNOWN) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z, "function code " + index);
        return 2;
    }

    public void getOperands(Stack s) {
        int i;
        ParseItem[] items = new ParseItem[this.function.getNumArgs()];
        for (i = this.function.getNumArgs() - 1; i >= 0; i--) {
            items[i] = (ParseItem) s.pop();
        }
        for (i = 0; i < this.function.getNumArgs(); i++) {
            add(items[i]);
        }
    }

    public void getString(StringBuffer buf) {
        buf.append(this.function.getName(this.settings));
        buf.append('(');
        int numArgs = this.function.getNumArgs();
        if (numArgs > 0) {
            ParseItem[] operands = getOperands();
            operands[0].getString(buf);
            for (int i = 1; i < numArgs; i++) {
                buf.append(',');
                operands[i].getString(buf);
            }
        }
        buf.append(')');
    }

    byte[] getBytes() {
        byte[] newdata;
        ParseItem[] operands = getOperands();
        byte[] data = new byte[0];
        for (ParseItem bytes : operands) {
            byte[] opdata = bytes.getBytes();
            newdata = new byte[(data.length + opdata.length)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
            data = newdata;
        }
        newdata = new byte[(data.length + 3)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        newdata[data.length] = (byte) (useAlternateCode() ? Token.FUNCTION.getCode2() : Token.FUNCTION.getCode());
        IntegerHelper.getTwoBytes(this.function.getCode(), newdata, data.length + 1);
        return newdata;
    }

    int getPrecedence() {
        return 3;
    }
}
