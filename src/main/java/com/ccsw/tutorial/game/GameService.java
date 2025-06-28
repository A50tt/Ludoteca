package com.ccsw.tutorial.game;

import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<StatusResponse> save(Long id, GameDto dto);

}