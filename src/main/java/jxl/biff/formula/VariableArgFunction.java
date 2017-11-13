package jxl.biff.formula;

import java.util.Stack;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class VariableArgFunction extends Operator {
    private static Logger logger = Logger.getLogger(VariableArgFunction.class);
    private int arguments;
    private Function function;
    private boolean readFromSheet = true;
    private WorkbookSettings settings;

    public VariableArgFunction(WorkbookSettings ws) {
        this.settings = ws;
    }

    public VariableArgFunction(Function f, int a, WorkbookSettings ws) {
        this.function = f;
        this.arguments = a;
        this.settings = ws;
    }

    public int read(byte[] data, int pos) throws FormulaException {
        this.arguments = data[pos];
        int index = IntegerHelper.getInt(data[pos + 1], data[pos + 2]);
        this.function = Function.getFunction(index);
        if (this.function != Function.UNKNOWN) {
            return 3;
        }
        throw new FormulaException(FormulaException.UNRECOGNIZED_FUNCTION, index);
    }

    public void getOperands(Stack s) {
        int i;
        ParseItem[] items = new ParseItem[this.arguments];
        for (i = this.arguments - 1; i >= 0; i--) {
            items[i] = (ParseItem) s.pop();
        }
        for (i = 0; i < this.arguments; i++) {
            add(items[i]);
        }
    }

    public void getString(StringBuffer buf) {
        buf.append(this.function.getName(this.settings));
        buf.append('(');
        if (this.arguments > 0) {
            ParseItem[] operands = getOperands();
            int i;
            if (this.readFromSheet) {
                operands[0].getString(buf);
                for (i = 1; i < this.arguments; i++) {
                    buf.append(',');
                    operands[i].getString(buf);
                }
            } else {
                operands[this.arguments - 1].getString(buf);
                for (i = this.arguments - 2; i >= 0; i--) {
                    buf.append(',');
                    operands[i].getString(buf);
                }
            }
        }
        buf.append(')');
    }

    Function getFunction() {
        return this.function;
    }

    byte[] getBytes() {
        byte[] newdata;
        handleSpecialCases();
        ParseItem[] operands = getOperands();
        byte[] data = new byte[0];
        for (ParseItem bytes : operands) {
            byte[] opdata = bytes.getBytes();
            newdata = new byte[(data.length + opdata.length)];
            System.arraycopy(data, 0, newdata, 0, data.length);
            System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
            data = newdata;
        }
        newdata = new byte[(data.length + 4)];
        System.arraycopy(data, 0, newdata, 0, data.length);
        newdata[data.length] = (byte) (useAlternateCode() ? Token.FUNCTIONVARARG.getCode2() : Token.FUNCTIONVARARG.getCode());
        newdata[data.length + 1] = (byte) ((byte) this.arguments);
        IntegerHelper.getTwoBytes(this.function.getCode(), newdata, data.length + 2);
        return newdata;
    }

    int getPrecedence() {
        return 3;
    }

    private void handleSpecialCases() {
        if (this.function == Function.SUMPRODUCT) {
            ParseItem[] operands = getOperands();
            for (int i = operands.length - 1; i >= 0; i--) {
                if (operands[i] instanceof Area) {
                    operands[i].setAlternateCode();
                }
            }
        }
    }
}
