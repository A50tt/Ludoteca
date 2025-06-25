package com.ccsw.tutorial.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoanException extends Exception {

    public static String INVALID_END_DATE = "BAD_END_DATE.";
    public static String INVALID_END_DATE_EXTENDED = "La fecha de finalización del préstamo no puede ser anterior a la fecha de inicio.";

    public static String INVALID_PERIOD = "BAD_LEND_PERIOD";
    public static String INVALID_PERIOD_EXTENDED = "No se puede prestar un juego por más de 14 días.";

    public static String GAME_ALREADY_LENT = "ERROR_GAME_ALREADY_LENT";
    public static String GAME_ALREADY_LENT_EXTENDED = "El juego ya está prestado para esas fechas.";

    public static String LOAN_LIMIT_EXCEEDED = "ERROR_GAME_LIMIT_EXCEEDED";
    public static String LOAN_LIMIT_EXCEEDED_EXTENDED = "Un cliente no puede tener más de dos juegos prestados en una misma fecha.";

    public static String DEFAULT_ERROR = "UNKNOWN_ERROR";
    public static String DEFAULT_ERROR_EXTENDED = "Something went wrong.";

    private final String msg;

    public LoanException(String errorMsg) {
        super();
        this.msg = errorMsg;
    }
}
