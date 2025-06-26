package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        //        if (criteria.getOperation().equalsIgnoreCase("eq") && criteria.getValue() != null) {
        //            if (path.getJavaType().equals(Game.class) || path.getJavaType().equals(Client.class)) {
        //                Predicate p = builder.equal(path, criteria.getValue());
        //                return builder.equal(path, criteria.getValue());
        //            }
        //        }
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            if (path.getJavaType().equals(Long.class)) {
                Predicate p = builder.equal(path, ((Long) criteria.getValue()));
                return builder.equal(path, criteria.getValue());
            }
        }
        if (path.getJavaType().equals(LocalDate.class)) {
            if (criteria.getOperation().equals("<=") && criteria.getValue() != null) {
                return builder.lessThanOrEqualTo((Path<LocalDate>) path, (LocalDate) criteria.getValue());
            } else if (criteria.getOperation().equals(">=") && criteria.getValue() != null) {
                return builder.greaterThanOrEqualTo((Path<LocalDate>) path, (LocalDate) criteria.getValue());
            }
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