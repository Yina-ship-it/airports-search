package com.example.filter.filters;

import com.example.repos.Repository;

import java.util.*;

public class EqualFilter extends Filter {
    public EqualFilter(int columnNumber, String value) {
        super(columnNumber, value);
    }

    @Override
    public Set<Integer> apply(Repository repository) {
        return new HashSet<>(repository.getFields().get(columnNumber).getOrDefault(value, Collections.emptyList()));
    }
}
