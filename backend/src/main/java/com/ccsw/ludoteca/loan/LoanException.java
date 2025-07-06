package com.ccsw.ludoteca.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoanException extends Exception {

    public static final String INVALID_END_DATE = "BAD_END_DATE";
    public static final String INVALID_END_DATE_EXTENDED = "La fecha de finalización del préstamo no puede ser anterior a la fecha de inicio.";

    public static final String INVALID_PERIOD = "BAD_LEND_PERIOD";
    public static final String INVALID_PERIOD_EXTENDED = "No se puede prestar un juego por más de 14 días.";

    public static final String GAME_ALREADY_LENT = "ERROR_GAME_ALREADY_LENT";
    public static final String GAME_ALREADY_LENT_EXTENDED = "El juego ya está prestado para esas fechas.";

    public static final String LOAN_LIMIT_EXCEEDED = "ERROR_GAME_LIMIT_EXCEEDED";
    public static final String LOAN_LIMIT_EXCEEDED_EXTENDED = "El cliente no puede tener más de dos juegos prestados en un mismo día.";

    public static final String ID_NOT_EXIST = "ERROR_ID_DOES_NOT_EXIST";
    public static final String ID_NOT_EXIST_EXTENDED = "El ID proporcionado no existe en la base de datos.";

    public String extendedMessage;

    public LoanException(String error, String extendedMessage) {
        super(error);
        this.extendedMessage = extendedMessage;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }
}
