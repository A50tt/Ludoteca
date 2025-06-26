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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final String CREATION_SUCCESSFUL_EXT_MSG = "El juego se ha creado";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "El juego se ha modificado";

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

        Specification<Game> spec = Specification.where(titleSpec).and(categorySpec);

        return this.gameRepository.findAll(spec);
    }

    @Override
    public ResponseEntity<StatusResponse> save(Long id, GameDto dto) {
        // Tenemos en cuenta si es edición o creación de Autor para devolver el mensaje correspondiente.
        Game game;
        boolean isUpdate = false;

        if (id == null) {
            game = new Game();
        } else {
            game = this.gameRepository.findById(id).orElse(null);
            if (game != null) {
                isUpdate = true;
            }
        }

        BeanUtils.copyProperties(dto, game, "id", "author", "category");
        game.setAuthor(authorService.get(dto.getAuthor().getId()));
        game.setCategory(categoryService.get(dto.getCategory().getId()));

        try {
            this.gameRepository.save(game);
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}
