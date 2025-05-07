package com.pm.patientservice.exception;

public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    private Object object;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, Object object) {
        this.errorCode = errorCode;
        this.object = object;
    }
}
