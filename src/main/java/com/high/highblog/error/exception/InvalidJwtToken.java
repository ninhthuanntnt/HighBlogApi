package com.high.highblog.error.exception;

public class InvalidJwtToken extends ValidatorException{

    public InvalidJwtToken(String errorCode) {
        super("Invalid jwt token", "access_token", errorCode);
    }

    public InvalidJwtToken(){
        super("Invalid jwt token", "access_token");
    }
}
