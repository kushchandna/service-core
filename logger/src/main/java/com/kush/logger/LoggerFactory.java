package com.kush.logger;

public class LoggerFactory {

	public static final LoggerFactory INSTANCE = new LoggerFactory();

	private LoggerFactory() {
	}

	public Logger getLogger(Class<?> klass) {
		return new JavaUtilLogger(klass);
	}
}
