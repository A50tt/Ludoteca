package com.ccsw.tutorial.author;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthorException extends Exception {

    public static String AUTHOR_ID_NOT_FOUND = "AUTHOR_ID_NOT_FOUND";
    public static String AUTHOR_ID_NOT_FOUND_EXTENDED = "No se puede borrar el autor debido a que el ID proporcionado no existe en la base de datos.";

    public static String AUTHOR_HAS_GAMES = "AUTHOR_HAS_GAMES";
    public static String AUTHOR_HAS_GAMES_EXTENDED = "No se puede borrar el autor debido a que tiene juegos registrados a su nombre.";

}
