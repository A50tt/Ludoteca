package com.ccsw.ludoteca.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ClientException extends Exception {

    public static String CLIENT_ID_NOT_FOUND = "CLIENT_ID_NOT_FOUND";
    public static String CLIENT_ID_NOT_FOUND_EXTENDED = "No se puede borrar el cliente debido a que el ID proporcionado no existe en la base de datos.";

    public static String NAME_ALREADY_EXISTS = "NAME_ALREADY_EXISTS";
    public static String NAME_ALREADY_EXISTS_EXTENDED = "No se puede guardar el cliente debido a que ya existe otro cliente con ese nombre en la base de datos.";

    public static String CLIENT_HAS_GAMES = "CLIENT_HAS_LENDINGS";
    public static String CLIENT_HAS_GAMES_EXTENDED = "No se puede borrar el autor debido a que tiene préstamos registrados a su nombre.";

    public String extendedMessage;

    public ClientException(String error, String extendedMessage) {
        super(error);
        this.extendedMessage = extendedMessage;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }
}
