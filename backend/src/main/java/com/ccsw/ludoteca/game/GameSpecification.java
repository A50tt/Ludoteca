package com.ccsw.ludoteca.game;

import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.game.model.Game;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class GameSpecification implements Specification<Game> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public GameSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(builder.lower(path), "%" + criteria.getValue().toString().toLowerCase() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }
        return null;
    }

    private Path<String> getPath(Root<Game> root) {
        String key = criteria.getKey();
        if (key == null) {
            throw new IllegalArgumentException("Search criteria key cannot be null");
        }
        String[] split = key.split("[.]");

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}