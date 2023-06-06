package com.example.filter;

import com.example.filter.filters.*;
import com.example.filter.filters.predicates.AndPredicate;
import com.example.filter.filters.predicates.OrPredicate;
import com.example.filter.filters.predicates.Predicate;

public class FilterParser {
    private final String input;
    private int position;

    public FilterParser(String input) {
        this.input = input;
        this.position = 0;
    }

    public Predicate parse() {
        return parseExpression();
    }

    private Predicate parseExpression() {
        Predicate predicate = parseTerm();

        while (position < input.length() && (input.charAt(position) == '&' || input.charAt(position) == '|')) {
            char operator = input.charAt(position);
            position++;

            Predicate rightPredicate = parseTerm();

            if (operator == '&') {
                predicate = new AndPredicate(predicate, rightPredicate);
            } else {
                predicate = new OrPredicate(predicate, rightPredicate);
            }
        }

        return predicate;
    }

    private Predicate parseTerm() {
        if (position < input.length() && input.charAt(position) == '(') {
            position++;
            Predicate predicate = parseExpression();
            position++;
            return predicate;
        } else {
            return parseFilter();
        }
    }

    private Predicate parseFilter() {
        int columnNumber = parseColumnNumber();
        String operator = parseOperator();
        String value = parseValue();

        switch (operator) {
            case "=":
                return new EqualFilter(columnNumber, value);
            case "<>":
                return new NotEqualFilter(columnNumber, value);
            case ">":
                return new GreaterThanFilter(columnNumber, value);
            case "<":
                return new LessThanFilter(columnNumber, value);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private int parseColumnNumber() {
        int start = input.indexOf("[", position);
        if (start == -1) {
            throw new IllegalArgumentException("Invalid column number format");
        }
        start++;

        int end = input.indexOf("]", start);
        if (end == -1) {
            throw new IllegalArgumentException("Invalid column number format");
        }

        String numberStr = input.substring(start, end);
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid column number format");
        } finally {
            position = end + 1;
        }
    }

    private String parseOperator() {
        if (position < input.length() && input.charAt(position) == '=') {
            position++;
            return "=";
        } else if (position < input.length() - 1 && input.charAt(position) == '<' && input.charAt(position + 1) == '>') {
            position += 2;
            return "<>";
        } else if (position < input.length() && (input.charAt(position) == '>' || input.charAt(position) == '<')) {
            char operator = input.charAt(position);
            position++;
            return Character.toString(operator);
        }

        throw new IllegalArgumentException("Invalid operator at index " + position);
    }

    private String parseValue() {
        StringBuilder value = new StringBuilder();

        if (position < input.length() && input.charAt(position) == '\'') {
            position++;
            boolean escape = false;
            while (position < input.length()) {
                char currentChar = input.charAt(position);
                if (currentChar == '\'' && !escape) {
                    break;
                } else if (currentChar == '\\' && !escape) {
                    escape = true;
                } else {
                    value.append(currentChar);
                    escape = false;
                }
                position++;
            }
            position++;
        } else {
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                value.append(input.charAt(position));
                position++;
            }
        }

        return value.toString();
    }
}
