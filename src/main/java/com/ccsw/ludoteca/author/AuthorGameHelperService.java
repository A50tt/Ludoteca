package com.ccsw.ludoteca.author;

import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.game.GameRepository;
import com.ccsw.ludoteca.game.GameSpecification;
import com.ccsw.ludoteca.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AuthorGameHelperService {

    private final GameRepository gameRepository;
    private final AuthorRepository authorRepository;

    public AuthorGameHelperService(GameRepository gameRepository, AuthorRepository authorRepository) {
        this.gameRepository = gameRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Recupera los juegos filtrando por autor
     *
     * @param idAuthor PK de la entidad Author
     */
    public boolean findGamesByAuthor(Long idAuthor) {
        GameSpecification authorSpec = new GameSpecification(new SearchCriteria("author.id", ":", idAuthor));

        Specification<Game> spec = authorSpec.and(authorSpec);
        return !gameRepository.findAll(spec).isEmpty();
    }
}
