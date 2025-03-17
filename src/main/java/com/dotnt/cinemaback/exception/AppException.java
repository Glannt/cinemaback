package com.dotnt.cinemaback.exception;


public class AppException extends RuntimeException {

    public AppException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
