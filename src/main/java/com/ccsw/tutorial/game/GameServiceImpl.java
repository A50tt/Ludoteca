package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final String CREATION_SUCCESSFUL_EXT_MSG = "El juego se ha creado.";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "El juego se ha modificado.";

    private final GameRepository gameRepository;

    private final AuthorService authorService;

    private final CategoryService categoryService;

    public GameServiceImpl(GameRepository gameRepository, AuthorService authorService, CategoryService categoryService) {
        this.gameRepository = gameRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory) {
        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));
        return this.gameRepository.findAll(titleSpec.and(categorySpec));
    }

    @Override
    public ResponseEntity<StatusResponse> save(Long id, GameDto dto) {
        // Se ha introducido un Game sin 'title', 'age', 'Category' o 'Author'
        if (dto.getTitle().isEmpty() || dto.getAge() == null || dto.getCategory().getId() == null || dto.getAuthor().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        }

        // Tenemos en cuenta si es edición o creación de 'Game' para devolver el StatusResponse con body correspondiente.
        Game game;
        boolean isUpdate = false;

        // Recuperamos un 'Game' con ese 'id' en la BBDD si existe y definimos la operación como UPDATE.
        // Si no existe, lo inicializamos sin valores.
        if (id == null) {
            game = new Game(); // Inicializamos el objeto (sin valores).
        } else {
            game = this.gameRepository.findById(id).orElse(null);
            if (game != null) { // Si se ha encontrado un 'Game' con ese 'id' en la BBDD, es UPDATE.
                isUpdate = true;
            }
        }
        BeanUtils.copyProperties(dto, game, "id", "author", "category");
        game.setAuthor(authorService.get(dto.getAuthor().getId()));
        game.setCategory(categoryService.get(dto.getCategory().getId()));

        try {
            this.gameRepository.save(game); // Si da Exception o no y según UPDATE, devuelve un body u otro.
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}
