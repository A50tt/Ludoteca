package com.ccsw.tutorial.category;

import com.ccsw.tutorial.author.AuthorRepository;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.game.GameSpecification;
import com.ccsw.tutorial.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategoryGameHelperService {
    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;

    public CategoryGameHelperService(GameRepository gameRepository, CategoryRepository categoryRepository) {
        this.gameRepository = gameRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Recupera los juegos filtrando por categoría
     *
     * @param idCategory PK de la entidad Category
     */
    public boolean findGamesByCategory(Long idCategory) {
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));

        Specification<Game> spec = Specification.where(categorySpec);
        return !gameRepository.findAll(spec).isEmpty();
    }
}
