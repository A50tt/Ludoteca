package com.ccsw.tutorial.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException {
    public static String CLIENT_ID_NOT_FOUND = "CLIENT_ID_NOT_FOUND";
    public static String CLIENT_ID_NOT_FOUND_EXTENDED = "No se puede borrar el cliente debido a que el ID proporcionado no existe en la base de datos.";

    public static String NAME_ALREADY_EXISTS = "NAME_ALREADY_EXISTS";
    public static String NAME_ALREADY_EXISTS_EXTENDED = "No se puede guardar el cliente debido a que ya existe otro cliente con ese nombre en la base de datos.";

    public static String CLIENT_HAS_GAMES = "CLIENT_HAS_LENDINGS";
    public static String CLIENT_HAS_GAMES_EXTENDED = "No se puede borrar el autor debido a que tiene préstamos registrados a su nombre.";
}
