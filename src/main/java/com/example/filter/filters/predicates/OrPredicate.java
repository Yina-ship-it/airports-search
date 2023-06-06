package com.example.filter.filters.predicates;

import com.example.repos.Repository;

import java.util.Set;

public class OrPredicate implements Predicate {
    private final Predicate left;
    private final Predicate right;

    public OrPredicate(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Set<Integer> apply(Repository repository) {
        Set<Integer> leftSet = left.apply(repository);
        Set<Integer> rightSet = right.apply(repository);
        leftSet.addAll(rightSet);
        return leftSet;
    }
}
