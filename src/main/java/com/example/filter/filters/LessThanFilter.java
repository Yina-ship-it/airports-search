package com.example.filter.filters;

import com.example.repos.Repository;

import java.util.*;

public class LessThanFilter extends Filter {
    public LessThanFilter(int columnNumber, String value) {
        super(columnNumber, value);
    }

    @Override
    public Set<Integer> apply(Repository repository) {
        if (!repository.getFields().containsKey(columnNumber)) {
            return Collections.emptySet();
        }

        Set<Integer> result = new HashSet<>();
        double comparisonValue = Double.parseDouble(value);
        for (Map.Entry<String, List<Integer>> entry : repository.getFields().get(columnNumber).entrySet()) {
            if (Double.parseDouble(entry.getKey()) < comparisonValue) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }
}
