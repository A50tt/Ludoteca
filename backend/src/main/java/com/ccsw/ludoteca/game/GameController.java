package com.ccsw.ludoteca.game;

import com.ccsw.ludoteca.common.exception.CommonException;
import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.game.model.Game;
import com.ccsw.ludoteca.game.model.GameDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Game", description = "API of Game")
@RequestMapping(value = "/game")
@RestController
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    private final ModelMapper mapper;

    public GameController(GameService gameService, ModelMapper mapper) {
        this.gameService = gameService;
        this.mapper = mapper;
    }

    /**
     * Método para recuperar una lista de {@link Game}
     *
     * @param title título del juego
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link GameDto}
     */
    @Operation(summary = "Find", description = "Method that return a filtered list of Games")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GameDto> find(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "idCategory", required = false) Long idCategory) {
        List<Game> games = gameService.find(title, idCategory);
        return games.stream().map(e -> mapper.map(e, GameDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Game}.
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Game")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody GameDto dto) {
        try {
            StatusResponse response = gameService.save(id, dto); // No se ha informado de 'edad', 'Category' o 'Author'
            if (response.getMessage().equals(StatusResponse.OK_REQUEST_MSG)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (NullPointerException | DataIntegrityViolationException ex1) { // No se ha introducido 'title'
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        } catch (Exception ex2) { // Exception catch por defecto.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}