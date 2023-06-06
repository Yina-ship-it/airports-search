package com.example.filter.filters;

import com.example.filter.filters.predicates.Predicate;
import com.example.repos.Repository;

import java.util.Set;

public abstract class Filter implements Predicate {
    protected int columnNumber;
    protected String value;

    public Filter(int columnNumber, String value) {
        this.columnNumber = columnNumber;
        this.value = value;
    }

    public abstract Set<Integer> apply(Repository repository);
}
