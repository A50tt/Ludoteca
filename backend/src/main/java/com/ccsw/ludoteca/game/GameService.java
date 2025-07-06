package com.ccsw.ludoteca.game;

import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.game.model.Game;
import com.ccsw.ludoteca.game.model.GameDto;
import com.ccsw.ludoteca.game.model.GameException;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface GameService {

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param title título del juego
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, Long idCategory);

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador está o no informado
     * Devolverá un @{code StatusResponse} con error si falta cualquiera de los cambos.
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse save(Long id, GameDto dto) throws GameException;

}