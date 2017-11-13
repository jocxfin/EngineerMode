package jxl.common;

import java.security.AccessControlException;

import jxl.common.log.LoggerName;
import jxl.common.log.SimpleLogger;

public abstract class Logger {
    private static Logger logger = null;

    public abstract void error(Object obj);

    protected abstract Logger getLoggerImpl(Class cls);

    public abstract void warn(Object obj);

    public abstract void warn(Object obj, Throwable th);

    public static final Logger getLogger(Class cl) {
        if (logger == null) {
            initializeLogger();
        }
        return logger.getLoggerImpl(cl);
    }

    private static synchronized void initializeLogger() {
        synchronized (Logger.class) {
            if (logger == null) {
                String loggerName = LoggerName.NAME;
                try {
                    loggerName = System.getProperty("logger");
                    if (loggerName == null) {
                        loggerName = LoggerName.NAME;
                    }
                    logger = (Logger) Class.forName(loggerName).newInstance();
                } catch (IllegalAccessException e) {
                    logger = new SimpleLogger();
                    logger.warn("Could not instantiate logger " + loggerName + " using default");
                } catch (InstantiationException e2) {
                    logger = new SimpleLogger();
                    logger.warn("Could not instantiate logger " + loggerName + " using default");
                } catch (AccessControlException e3) {
                    logger = new SimpleLogger();
                    logger.warn("Could not instantiate logger " + loggerName + " using default");
                } catch (ClassNotFoundException e4) {
                    logger = new SimpleLogger();
                    logger.warn("Could not instantiate logger " + loggerName + " using default");
                }
            } else {
                return;
            }
        }
    }

    protected Logger() {
    }

    public void setSuppressWarnings(boolean w) {
    }
}
