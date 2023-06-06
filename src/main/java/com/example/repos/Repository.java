package com.example.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private final Map<Integer, Integer> indexRow = new HashMap<>();
    private final Map<Integer, String> rowTitle = new HashMap<>();
    private final Map<Integer, Map<String, List<Integer>>> fields = new HashMap<>();

    public Map<Integer, Integer> getIndexRow() {
        return indexRow;
    }

    public Map<Integer, String> getRowTitle() {
        return rowTitle;
    }

    public Map<Integer, Map<String, List<Integer>>> getFields() {
        return fields;
    }

}