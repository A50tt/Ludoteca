package com.ccsw.ludoteca.game;

import com.ccsw.ludoteca.author.AuthorService;
import com.ccsw.ludoteca.category.CategoryService;
import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.exception.CommonErrorResponse;
import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.game.model.Game;
import com.ccsw.ludoteca.game.model.GameDto;
import com.ccsw.ludoteca.game.model.GameException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Game> gameList = this.gameRepository.findAll(titleSpec.and(categorySpec));
        return gameList;
    }

    @Override
    public StatusResponse save(Long id, GameDto dto) throws GameException {
        // Se ha introducido un Game sin 'title', 'age', 'Category' o 'Author'
        try {
            if (dto.getTitle().isEmpty() || dto.getAge() == null || dto.getCategory().getId() == null || dto.getAuthor().getId() == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException ex) {
            throw new GameException(CommonErrorResponse.MISSING_REQUIRED_FIELDS, CommonErrorResponse.MISSING_REQUIRED_FIELDS_EXTENDED);
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
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG);
            }
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG);
        } catch (Exception ex) {
            throw new GameException(CommonErrorResponse.DEFAULT_ERROR, CommonErrorResponse.DEFAULT_ERROR_EXTENDED);
        }
    }
}
