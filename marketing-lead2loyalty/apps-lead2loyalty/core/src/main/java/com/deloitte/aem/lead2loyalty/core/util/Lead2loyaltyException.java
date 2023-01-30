package com.deloitte.aem.lead2loyalty.core.util;

public class Lead2loyaltyException {

    private int errorCode;
    private String errorMessage;

    public Lead2loyaltyException(String message, Integer code) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public Lead2loyaltyException() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
