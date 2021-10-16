package com.taskapplication.repositories.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpecificationCriteria {
    private String key;
    private SpecificationOperation operation;
    private Object value;
    private String[] nestedFields;

    public enum SpecificationOperation {
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL_TO,
        LESS_THAN,
        LESS_THAN_OR_EQUAL_TO,
        LIKE
    }
}
