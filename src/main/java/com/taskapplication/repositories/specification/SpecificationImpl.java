package com.taskapplication.repositories.specification;

import com.taskapplication.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

import static com.taskapplication.repositories.specification.SpecificationCriteria.SpecificationOperation.*;
import static com.taskapplication.util.ArrayUtil.isEmpty;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public final class SpecificationImpl<T> implements Specification<T> {

    private final SpecificationCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        checkCriteria();
        final Path<T> path = getPath(root);

        return getPredicate(root, criteriaBuilder, path);
    }

    private void checkCriteria() {
        if (isNull(criteria))
            throw new SpecificationCreationException("SearchCriteria should be defined.");
    }

    private Path<T> getPath(Root<T> root) {
        if (isEmpty(criteria.getNestedFields()))
            return root;

        return getPathOfNestedFields(root);
    }

    private Path<T> getPathOfNestedFields(Root<T> root) {
        Path<T> path = root;
        for (String field : criteria.getNestedFields())
            path = path.get(field);

        return path;
    }

    private Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Path<T> path) {
        if (criteria.getOperation().equals(GREATER_THAN_OR_EQUAL_TO))
            return criteriaBuilder.greaterThanOrEqualTo(
                    path.get(criteria.getKey()), criteria.getValue().toString());
        if (criteria.getOperation().equals(GREATER_THAN))
            return criteriaBuilder.greaterThan(
                    path.get(criteria.getKey()), criteria.getValue().toString());
        if (criteria.getOperation().equals(LESS_THAN_OR_EQUAL_TO))
            return criteriaBuilder.lessThanOrEqualTo(
                    path.get(criteria.getKey()), criteria.getValue().toString());
        if (criteria.getOperation().equals(LESS_THAN))
            return criteriaBuilder.lessThan(
                    path.get(criteria.getKey()), criteria.getValue().toString());
        if (criteria.getOperation().equals(LIKE)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class)
                return criteriaBuilder.like(criteriaBuilder.lower(path.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase(Locale.ROOT) + "%");
            return criteriaBuilder.equal(path.get(criteria.getKey()), criteria.getValue());
        }

        throw new SpecificationCreationException("Search operation should be specified correctly.");
    }

    public static final class SpecificationCreationException extends BaseException {
        public SpecificationCreationException(String message) {
            super(message);
        }
    }
}
