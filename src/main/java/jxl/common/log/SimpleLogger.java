package jxl.common.log;

import jxl.common.Logger;

public class SimpleLogger extends Logger {
    private boolean suppressWarnings = false;

    public void error(Object message) {
        System.err.print("Error: ");
        System.err.println(message);
    }

    public void warn(Object message) {
        if (!this.suppressWarnings) {
            System.err.print("Warning:  ");
            System.err.println(message);
        }
    }

    public void warn(Object message, Throwable t) {
        if (!this.suppressWarnings) {
            System.err.print("Warning:  ");
            System.err.println(message);
            t.printStackTrace();
        }
    }

    protected Logger getLoggerImpl(Class c) {
        return this;
    }

    public void setSuppressWarnings(boolean w) {
        this.suppressWarnings = w;
    }
}
