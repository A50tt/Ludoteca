package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;

public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (criteria.getOperation().equalsIgnoreCase("eq") && criteria.getValue() != null) {
            System.out.println(path.getJavaType());
            if (path.getJavaType().equals(Game.class)) {
                Predicate p = builder.equal(path, criteria.getValue());
                return builder.equal(path, criteria.getValue());
            }
        }
        if (criteria.getOperation().equals("<=") && criteria.getValue() != null) {
            return builder.lessThanOrEqualTo((Path<Calendar>) path, (Calendar) criteria.getValue());
        } else if (criteria.getOperation().equals(">=") && criteria.getValue() != null) {
            return builder.greaterThanOrEqualTo((Path<Calendar>) path, (Calendar) criteria.getValue());
        }
        return null;
    }

    private Path<String> getPath(Root<Loan> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }
}