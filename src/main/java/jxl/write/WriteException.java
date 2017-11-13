package jxl.write;

import jxl.JXLException;

public abstract class WriteException extends JXLException {
    protected WriteException(String s) {
        super(s);
    }
}
