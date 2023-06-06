package com.example.filter.filters.predicates;

import com.example.repos.Repository;

import java.util.Set;

public interface Predicate {
    Set<Integer> apply(Repository repository);
}
