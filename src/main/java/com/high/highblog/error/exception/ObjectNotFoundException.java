package com.high.highblog.error.exception;

public class ObjectNotFoundException extends ValidatorException{
    private static final String DEFAULT_ERROR_CODE = "HB-0600";

    public ObjectNotFoundException(final String message, final String fieldName) {
        super(message, fieldName, DEFAULT_ERROR_CODE);
    }

    public ObjectNotFoundException(final String fieldName){
        super("Not found", fieldName, DEFAULT_ERROR_CODE);
    }
}
