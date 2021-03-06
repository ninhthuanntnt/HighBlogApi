package com.high.highblog.error.exception;

import java.util.Optional;

public class ValidatorException extends RuntimeException{
    private final String fieldName;
    private final String errorCode;
    private static final String DEFAULT_ERROR_CODE = "HB-0100";

    public ValidatorException(final String message, final String fieldName) {
        super(message);
        this.fieldName = fieldName;
        this.errorCode = DEFAULT_ERROR_CODE;
    }

    public ValidatorException(final Throwable cause, final String fieldName) {
        super(cause);
        this.fieldName = fieldName;
        this.errorCode = DEFAULT_ERROR_CODE;
    }

    public ValidatorException(final String message, final String fieldName, final String errorCode) {
        super(message);
        this.fieldName = fieldName;
        this.errorCode = (String) Optional.ofNullable(errorCode).orElse(DEFAULT_ERROR_CODE);
    }

    public ValidatorException(final String message, final Throwable cause, final String fieldName, final String errorCode) {
        super(message, cause);
        this.fieldName = fieldName;
        this.errorCode = (String)Optional.ofNullable(errorCode).orElse("HB-0100");
    }

    public ValidatorException(final Throwable cause, final String fieldName, final String errorCode) {
        super(cause);
        this.fieldName = fieldName;
        this.errorCode = (String)Optional.ofNullable(errorCode).orElse("HB-0100");
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public String toString() {
        String s = this.getClass().getName() + "[" + this.fieldName + "]";
        String message = this.getLocalizedMessage();
        return message != null ? s + ": " + message : s;
    }
}
