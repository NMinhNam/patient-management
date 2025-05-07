package com.pm.patientservice.exception;

public enum ErrorCode {
    EMAIL_ALREADY_EXIST("1000", "Email already exist"),
    PATIENT_NOT_FOUND("1001", "Patient not found");

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;
}
