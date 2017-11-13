package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import jxl.WorkbookSettings;
import jxl.biff.WorkbookMethods;
import jxl.common.Logger;

class StringFormulaParser implements Parser {
    private static Logger logger = Logger.getLogger(StringFormulaParser.class);
    private Stack arguments;
    private ExternalSheet externalSheet;
    private String formula;
    private WorkbookMethods nameTable;
    private ParseContext parseContext;
    private String parsedFormula;
    private ParseItem root;
    private WorkbookSettings settings;

    public StringFormulaParser(String f, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc) {
        this.formula = f;
        this.settings = ws;
        this.externalSheet = es;
        this.nameTable = nt;
        this.parseContext = pc;
    }

    public void parse() throws FormulaException {
        this.root = parseCurrent(getTokens().iterator());
    }

    private ParseItem parseCurrent(Iterator i) throws FormulaException {
        Stack stack = new Stack();
        Stack operators = new Stack();
        Stack args = null;
        boolean parenthesesClosed = false;
        ParseItem parseItem = null;
        while (i.hasNext() && !parenthesesClosed) {
            ParseItem pi = (ParseItem) i.next();
            pi.setParseContext(this.parseContext);
            if (pi instanceof Operand) {
                handleOperand((Operand) pi, stack);
            } else if (pi instanceof StringFunction) {
                handleFunction((StringFunction) pi, i, stack);
            } else if (pi instanceof Operator) {
                Operator op = (Operator) pi;
                if (op instanceof StringOperator) {
                    StringOperator sop = (StringOperator) op;
                    if (stack.isEmpty() || (r3 instanceof Operator)) {
                        op = sop.getUnaryOperator();
                    } else {
                        op = sop.getBinaryOperator();
                    }
                }
                if (operators.empty()) {
                    operators.push(op);
                } else {
                    Operator operator = (Operator) operators.peek();
                    if (op.getPrecedence() < operator.getPrecedence()) {
                        operators.push(op);
                    } else if (op.getPrecedence() == operator.getPrecedence() && (op instanceof UnaryOperator)) {
                        operators.push(op);
                    } else {
                        operators.pop();
                        operator.getOperands(stack);
                        stack.push(operator);
                        operators.push(op);
                    }
                }
            } else if (pi instanceof ArgumentSeparator) {
                while (!operators.isEmpty()) {
                    Operator o = (Operator) operators.pop();
                    o.getOperands(stack);
                    stack.push(o);
                }
                if (args == null) {
                    args = new Stack();
                }
                args.push(stack.pop());
                stack.clear();
            } else if (pi instanceof OpenParentheses) {
                ParseItem pi2 = parseCurrent(i);
                Parenthesis p = new Parenthesis();
                pi2.setParent(p);
                p.add(pi2);
                stack.push(p);
            } else if (pi instanceof CloseParentheses) {
                parenthesesClosed = true;
            }
            parseItem = pi;
        }
        while (!operators.isEmpty()) {
            o = (Operator) operators.pop();
            o.getOperands(stack);
            stack.push(o);
        }
        ParseItem rt = stack.empty() ? null : (ParseItem) stack.pop();
        if (!(args == null || rt == null)) {
            args.push(rt);
        }
        this.arguments = args;
        if (!stack.empty() || !operators.empty()) {
            logger.warn("Formula " + this.formula + " has a non-empty parse stack");
        }
        return rt;
    }

    private ArrayList getTokens() throws FormulaException {
        ArrayList tokens = new ArrayList();
        Yylex lex = new Yylex(new StringReader(this.formula));
        lex.setExternalSheet(this.externalSheet);
        lex.setNameTable(this.nameTable);
        try {
            for (ParseItem pi = lex.yylex(); pi != null; pi = lex.yylex()) {
                tokens.add(pi);
            }
        } catch (IOException e) {
            logger.warn(e.toString());
        } catch (Error e2) {
            throw new FormulaException(FormulaException.LEXICAL_ERROR, this.formula + " at char  " + lex.getPos());
        }
        return tokens;
    }

    public String getFormula() {
        if (this.parsedFormula == null) {
            StringBuffer sb = new StringBuffer();
            this.root.getString(sb);
            this.parsedFormula = sb.toString();
        }
        return this.parsedFormula;
    }

    public byte[] getBytes() {
        byte[] bytes = this.root.getBytes();
        if (!this.root.isVolatile()) {
            return bytes;
        }
        byte[] newBytes = new byte[(bytes.length + 4)];
        System.arraycopy(bytes, 0, newBytes, 4, bytes.length);
        newBytes[0] = (byte) Token.ATTRIBUTE.getCode();
        newBytes[1] = (byte) 1;
        return newBytes;
    }

    private void handleFunction(StringFunction sf, Iterator i, Stack stack) throws FormulaException {
        ParseItem pi2 = parseCurrent(i);
        if (sf.getFunction(this.settings) == Function.UNKNOWN) {
            throw new FormulaException(FormulaException.UNRECOGNIZED_FUNCTION);
        } else if (sf.getFunction(this.settings) == Function.SUM && this.arguments == null) {
            a = new Attribute(sf, this.settings);
            a.add(pi2);
            stack.push(a);
        } else if (sf.getFunction(this.settings) == Function.IF) {
            a = new Attribute(sf, this.settings);
            vaf = new VariableArgFunction(this.settings);
            numargs = this.arguments.size();
            for (j = 0; j < numargs; j++) {
                vaf.add((ParseItem) this.arguments.get(j));
            }
            a.setIfConditions(vaf);
            stack.push(a);
        } else if (sf.getFunction(this.settings).getNumArgs() != Light.MAIN_KEY_MAX) {
            BuiltInFunction bif = new BuiltInFunction(sf.getFunction(this.settings), this.settings);
            numargs = sf.getFunction(this.settings).getNumArgs();
            if (numargs != 1) {
                if (this.arguments != null || numargs == 0) {
                    if (this.arguments != null) {
                        if (numargs == this.arguments.size()) {
                        }
                    }
                    for (j = 0; j < numargs; j++) {
                        bif.add((ParseItem) this.arguments.get(j));
                    }
                }
                throw new FormulaException(FormulaException.INCORRECT_ARGUMENTS);
            }
            bif.add(pi2);
            stack.push(bif);
        } else {
            if (this.arguments != null) {
                numargs = this.arguments.size();
                vaf = new VariableArgFunction(sf.getFunction(this.settings), numargs, this.settings);
                ParseItem[] args = new ParseItem[numargs];
                for (j = 0; j < numargs; j++) {
                    args[(numargs - j) - 1] = (ParseItem) this.arguments.pop();
                }
                for (ParseItem add : args) {
                    vaf.add(add);
                }
                stack.push(vaf);
                this.arguments.clear();
                this.arguments = null;
            } else {
                int numArgs;
                if (pi2 == null) {
                    numArgs = 0;
                } else {
                    numArgs = 1;
                }
                vaf = new VariableArgFunction(sf.getFunction(this.settings), numArgs, this.settings);
                if (pi2 != null) {
                    vaf.add(pi2);
                }
                stack.push(vaf);
            }
        }
    }

    private void handleOperand(Operand o, Stack stack) {
        if (o instanceof IntegerValue) {
            if (o instanceof IntegerValue) {
                IntegerValue iv = (IntegerValue) o;
                if (iv.isOutOfRange()) {
                    stack.push(new DoubleValue(iv.getValue()));
                } else {
                    stack.push(iv);
                }
            }
            return;
        }
        stack.push(o);
    }
}
