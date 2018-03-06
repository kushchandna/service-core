package com.kush.logger;

import java.util.logging.Level;

class JavaUtilLogger implements Logger {

    private final java.util.logging.Logger underlyingLogger;

    public JavaUtilLogger(Class<?> klass) {
        underlyingLogger = java.util.logging.Logger.getLogger(klass.getName());
    }

    @Override
    public boolean isDebugEnabled() {
        return underlyingLogger.isLoggable(Level.FINE);
    }

    @Override
    public void debug(String format, Object... args) {
        log(Level.FINE, format, args);
    }

    @Override
    public void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    @Override
    public void warn(String format, Object... args) {
        log(Level.WARNING, format, args);
    }

    @Override
    public void error(String format, Object... args) {
        log(Level.SEVERE, format, args);
    }

    @Override
    public void error(Throwable e) {
        underlyingLogger.log(Level.SEVERE, e.getMessage(), e);
    }

    private void log(Level level, String format, Object... args) {
        String message = String.format(format, args);
        underlyingLogger.log(level, message);
    }
}
