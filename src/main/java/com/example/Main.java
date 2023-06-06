package com.example;

import com.example.repos.Repository;
import com.example.service.Service;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Repository repository = new Repository();
        Service service = new Service(repository);

        service.loadFromFile("airports.csv");

        System.out.println("Введите фильтр:");
        String inputFilter = scanner.nextLine();
        while (!inputFilter.equals("!quit")) {
            System.out.println("Введите начало имени аэропорта");
            String input = scanner.nextLine();

            long startTime = System.nanoTime();
            Set<Integer> filteredIndexes = service.filterIndexes(inputFilter);

            List<String> searchResults = service.searchByAirportName(input);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;

            for (String result : searchResults) {
                System.out.println(result);
            }
            System.out.println("Количество найденных строк " + filteredIndexes.size() + ", Время выполнения: " + duration + " мс");

            service.resetFilter();
            System.out.println("Введите новый фильтр:");
            inputFilter = scanner.nextLine();
        }
    }
}
