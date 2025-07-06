package com.ccsw.ludoteca.common.exception;

import com.ccsw.ludoteca.dto.StatusResponse;

public class CommonErrorResponse extends StatusResponse {

    public static final String KO_REQUEST_MSG = "Error in request.";

    public static String DEFAULT_ERROR = "UNKNOWN_ERROR_BACKEND";
    public static String DEFAULT_ERROR_EXTENDED = "Error desconocido... pero es un error, eso seguro.";

    public static String MISSING_REQUIRED_FIELDS = "MISSING_REQUIRED_FIELDS";
    public static String MISSING_REQUIRED_FIELDS_EXTENDED = "Por favor, introduzca información en todos los campos obligatorios.";

    public CommonErrorResponse(String message, String details) {
        super(message, details);
    }
}