package com.ccsw.tutorial.dto;

public class StatusResponse {
    private String message;
    private String extendedMessage;

    public static final String SUITABLE_REQUEST = "REQUEST VALIDATED";
    public static final String OK_REQUEST_MSG = "Request successful.";

    public StatusResponse(String message) {
        this.message = message;
    }

    public StatusResponse(String message, String extendedMessage) {
        this.message = message;
        this.extendedMessage = extendedMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }

    public void setExtendedMessage(String extendedMessage) {
        this.extendedMessage = extendedMessage;
    }
}