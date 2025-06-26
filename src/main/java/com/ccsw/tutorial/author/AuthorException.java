package com.ccsw.tutorial.author;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthorException extends Exception {

    public static String BAD_ID_CANNOT_DELETE = "BAD_ID_CANNOT_DELETE";
    public static String BAD_ID_CANNOT_DELETE_EXTENDED = "No se puede borrar el autor debido a que el ID proporcionado no existe.";

    public static String AUTHOR_HAS_GAMES = "AUTHOR_HAS_GAMES";
    public static String AUTHOR_HAS_GAMES_EXTENDED = "No se puede borrar el autor debido a que tiene juegos registrados a su nombre.";

}
