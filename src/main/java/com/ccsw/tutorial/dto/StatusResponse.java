package com.ccsw.tutorial.dto;

public class StatusResponse {
    private String message;
    private String extendedMessage;

    public static final String OK_REQUEST_MSG = "Request successful.";
    public static final String KO_REQUEST_MSG = "Error in request."; // No se usa en realidad, porque cada clase tiene su propia clase Exception.

    public StatusResponse() {}
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

    public String getExtendedMessage() {
        return extendedMessage;
    }
}