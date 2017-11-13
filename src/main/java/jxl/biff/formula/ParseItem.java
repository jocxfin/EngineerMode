package jxl.biff.formula;

import jxl.common.Logger;

abstract class ParseItem {
    private static Logger logger = Logger.getLogger(ParseItem.class);
    private boolean alternateCode = false;
    private ParseItem parent;
    private ParseContext parseContext;
    private boolean valid = true;
    private boolean volatileFunction = false;

    abstract byte[] getBytes();

    abstract void getString(StringBuffer stringBuffer);

    public ParseItem() {
        ParseContext parseContext = this.parseContext;
        this.parseContext = ParseContext.DEFAULT;
    }

    protected void setParent(ParseItem p) {
        this.parent = p;
    }

    protected void setVolatile() {
        this.volatileFunction = true;
        if (this.parent != null && !this.parent.isVolatile()) {
            this.parent.setVolatile();
        }
    }

    final boolean isVolatile() {
        return this.volatileFunction;
    }

    protected void setAlternateCode() {
        this.alternateCode = true;
    }

    protected final boolean useAlternateCode() {
        return this.alternateCode;
    }

    protected void setParseContext(ParseContext pc) {
        this.parseContext = pc;
    }

    protected final ParseContext getParseContext() {
        return this.parseContext;
    }
}
