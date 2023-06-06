package com.example.filter.filters;

import com.example.repos.Repository;

import java.util.*;

public class NotEqualFilter extends Filter {
    public NotEqualFilter(int columnNumber, String value) {
        super(columnNumber, value);
    }

    @Override
    public Set<Integer> apply(Repository repository) {
        Set<Integer> unnecessaryIndices = new HashSet<>(repository.getFields().get(columnNumber).getOrDefault(value, Collections.emptyList()));

        Set<Integer> allIndices = new HashSet<>(repository.getIndexRow().keySet());

        allIndices.removeAll(unnecessaryIndices);

        return allIndices;
    }
}
