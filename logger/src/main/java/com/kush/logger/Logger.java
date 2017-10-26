package com.kush.logger;

public interface Logger {

	boolean isDebugEnabled();

	void debug(String format, Object... args);

	void info(String format, Object... args);

	void warn(String format, Object... args);

	void error(String format, Object... args);
	
	void error(Throwable e);
}
