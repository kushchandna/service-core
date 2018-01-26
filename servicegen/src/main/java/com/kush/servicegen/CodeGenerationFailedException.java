package com.kush.servicegen;

public class CodeGenerationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public CodeGenerationFailedException() {
    }

    public CodeGenerationFailedException(String message, Throwable e) {
        super(message, e);
    }

    public CodeGenerationFailedException(String message) {
        super(message);
    }
}
