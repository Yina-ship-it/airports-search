package com.example.filter.filters.predicates;

import com.example.repos.Repository;

import java.util.Set;

public class AndPredicate implements Predicate {
    private final Predicate left;
    private final Predicate right;

    public AndPredicate(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Set<Integer> apply(Repository repository) {
        // вычислить пересечение множеств
        Set<Integer> leftSet = left.apply(repository);
        Set<Integer> rightSet = right.apply(repository);
        leftSet.retainAll(rightSet);
        return leftSet;
    }
}
