package com.ccsw.ludoteca.category;

import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.game.GameRepository;
import com.ccsw.ludoteca.game.GameSpecification;
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
        return !gameRepository.findAll(categorySpec).isEmpty();
    }
}
