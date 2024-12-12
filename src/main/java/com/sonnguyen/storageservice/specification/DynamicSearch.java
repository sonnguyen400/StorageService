package com.sonnguyen.storageservice.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DynamicSearch {
    private String key;
    private Object value;
    private Operator operation;

    public enum Operator {
        LT,
        GT,
        LTE,
        GTE,
        EQUAL,
        BEFORE,
        AFTER,
        DEFAULT_LIKE;
    }
}
