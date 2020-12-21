package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 implements Day {
    public static void main(String[] args) {
        new Day21().printParts();
    }

    private final Map<String, Set<String>> allergensToIngredients = new HashMap<>();
    private final Set<String> foundAllergenIngredients = new HashSet<>();
    // We could speed up the algorithm by storing each ingredient and how often they appear in the recipe in a Map.
    private final List<String> allIngredients = new ArrayList<>();

    Day21() {
        List<String> lines = Arrays.stream(readDay(21).split(System.lineSeparator()))
                                   .collect(Collectors.toUnmodifiableList());
        lines.forEach(l -> {
            var ingredients = l.split("\\(contains ")[0].trim().split(" ");
            allIngredients.addAll(List.of(ingredients));
            var allergens = l.split("\\(contains ")[1].trim().replace(")", "").split(", ");
            Arrays.stream(allergens).forEach(al -> {
                if (!allergensToIngredients.containsKey(al)) {
                    allergensToIngredients.put(al, new HashSet<>(List.of(ingredients)));
                } else {
                    var intersect = new HashSet<>(allergensToIngredients.get(al));
                    intersect.retainAll(List.of(ingredients));
                    allergensToIngredients.put(al, intersect);
                }
            });
        });

        var allergensHeap = new PriorityQueue<Map.Entry<String, Set<String>>>(Comparator.comparingInt(e -> e.getValue().size()));
        allergensHeap.addAll(allergensToIngredients.entrySet());

        while (!allergensHeap.isEmpty()) {
            var e = allergensHeap.poll();
            var filteredIngredients = e.getValue();
            if (filteredIngredients.size() != 1) {
                filteredIngredients.removeAll(foundAllergenIngredients);
            }
            foundAllergenIngredients.addAll(filteredIngredients);
            allergensToIngredients.put(e.getKey(), filteredIngredients);
        }
    }

    @Override
    public Object part1() {
        allIngredients.removeAll(foundAllergenIngredients);
        return allIngredients.size();
    }

    @Override
    public Object part2() {
        var canonicalDangerousIngredient = new PriorityQueue<Map.Entry<String, Set<String>>>(Map.Entry.comparingByKey());
        canonicalDangerousIngredient.addAll(allergensToIngredients.entrySet());
        var sb = new StringBuilder();
        while (!canonicalDangerousIngredient.isEmpty()) {
            canonicalDangerousIngredient.poll().getValue().forEach(ing -> sb.append(ing).append(","));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
