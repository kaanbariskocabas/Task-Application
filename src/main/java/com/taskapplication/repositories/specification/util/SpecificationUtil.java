package com.taskapplication.repositories.specification.util;

import com.taskapplication.exception.BaseException;
import com.taskapplication.repositories.specification.SpecificationCriteria;
import com.taskapplication.repositories.specification.SpecificationImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static com.taskapplication.repositories.specification.SpecificationCriteria.SpecificationOperation;

@UtilityClass
public final class SpecificationUtil {

    public enum ParameterRelationType {
        AND,
        OR;

        public boolean isAnd() {
            return AND.equals(this);
        }

        public boolean isOr() {
            return OR.equals(this);
        }
    }

    public static <T> Specification<T> appendToSpecification(String key, SpecificationOperation operation, Object value,
                                                             ParameterRelationType parameterRelationType,
                                                             String[] nestedFields,
                                                             Specification<T> specification) {
        checkParameters(key, operation, value);
        if (isNull(specification))
            return createSpecification(key, operation, value, nestedFields);

        return addNewParameterToSpecification(key, operation, value, parameterRelationType, nestedFields,
                specification);
    }

    private static void checkParameters(String key, SpecificationOperation operation, Object value) {
        final UndefinedParameterPair undefined = getUndefinedParameter(key, operation, value);
        if (undefined.isUndefined())
            throw new SpecificationUtilException(String.format("Please define %s parameter", undefined.getParameter()));
    }

    private static UndefinedParameterPair getUndefinedParameter(String key, SpecificationOperation operation, Object value) {
        final String undefinedParameter = isNull(key)
                ? "key"
                : isNull(operation)
                ? "operation"
                : isNull(value)
                ? "value"
                : null;

        return new UndefinedParameterPair(undefinedParameter, nonNull(undefinedParameter));
    }

    private static <T> Specification<T> createSpecification(String key, SpecificationOperation operation, Object value,
                                                            String[] nestedFields) {
        return new SpecificationImpl<>(new SpecificationCriteria(key, operation, value, nestedFields));
    }

    private static <T> Specification<T> addNewParameterToSpecification(String key, SpecificationOperation operation,
                                                                       Object value,
                                                                       ParameterRelationType parameterRelationType,
                                                                       String[] nestedFields,
                                                                       Specification<T> specification) {
        if (isNull(parameterRelationType))
            throw new SpecificationUtilException("New specification parameter is being added. The parameter relation type should be specified!");
        final Specification<T> newSpecification = createSpecification(key, operation, value, nestedFields);
        if (parameterRelationType.isAnd())
            return specification.and(newSpecification);

        return specification.or(newSpecification);
    }

    public static class SpecificationUtilException extends BaseException {
        public SpecificationUtilException(String message) {
            super(message);
        }
    }

    @AllArgsConstructor
    @Getter
    private static final class UndefinedParameterPair {
        private String parameter;
        private boolean undefined;
    }

}
