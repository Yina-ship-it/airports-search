package com.example.service;

import com.example.filter.FilterParser;
import com.example.repos.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Service {
     private final Repository repository;
    private String filename;
    private Set<Integer> indexes;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public void loadFromFile(String filename) {
        this.filename = filename;
        Map<Integer, Integer> indexRow = repository.getIndexRow();
        Map<Integer, String> rowTitle = repository.getRowTitle();
        Map<Integer, Map<String, List<Integer>>> fields = repository.getFields();
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line;
            int currentRowNumber = 0;
            while ((line = file.readLine()) != null){
                currentRowNumber++;
                int currentColumnNumber = 0;
                StringBuilder field = new StringBuilder();
                int index = 0;
                boolean insideQuotes = false;
                boolean nullable = false;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '\\') {
                        if (i < line.length() - 1) {
                            if (!insideQuotes && line.charAt(i + 1) == 'N')
                                nullable = true;
                            else if (insideQuotes)
                                field.append(line.charAt(i + 1));
                            else
                                throw new Exception("Ошибка заполнения");
                            i++;
                        }
                    }
                    else if (c == '"')
                        insideQuotes = !insideQuotes;
                    else if (c == ',' && !insideQuotes) {
                        currentColumnNumber++;
                        if (currentColumnNumber == 1)
                            index = Integer.parseInt(field.toString());
                        else if (currentColumnNumber == 2)
                            rowTitle.put(currentRowNumber, field.toString());
                        if(!nullable)
                            fields.computeIfAbsent(currentColumnNumber, k -> new HashMap<>())
                                    .computeIfAbsent(field.toString(), k -> new ArrayList<>())
                                    .add(index);
                        field.setLength(0);
                        nullable = false;
                    }
                    else
                        field.append(c);
                }
                indexRow.put(index, currentRowNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        indexes = indexRow.keySet();
    }

    public Set<Integer> filterIndexes(String inputFilter) {
        FilterParser filterParser = new FilterParser(inputFilter);
        try {
            if (!inputFilter.equals(""))
                indexes = filterParser.parse().apply(repository);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return indexes;
    }

    public List<String> searchByAirportName(String airportName) {
        airportName = airportName.toLowerCase();
        List<String> results = new ArrayList<>();
        Set<Integer> rowIndexes = new HashSet<>();

        String finalAirportName = airportName;
        indexes.stream()
                .filter(index -> repository.getRowTitle()
                        .get(repository.getIndexRow().get(index)).toLowerCase().startsWith(finalAirportName))
                .forEach(index -> rowIndexes.add(repository.getIndexRow().get(index)));

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null && !rowIndexes.isEmpty()) {
                lineNum++;
                if (rowIndexes.contains(lineNum)){
                    rowIndexes.remove(lineNum);
                    results.add("\"" + repository.getRowTitle().get(lineNum) + "\"[" + line + "]");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return results;
    }

    public void resetFilter(){
        indexes = repository.getIndexRow().keySet();
    }

    private enum Type {
        STRING, NUMERIC, NULLABLE
    }
}