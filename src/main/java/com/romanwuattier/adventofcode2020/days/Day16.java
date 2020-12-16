package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 implements Day {
    public static void main(String[] args) {
        new Day16().printParts();
    }

    private final List<String> chunks = Arrays.stream(readDay(16).split("\n\n"))
                                              .collect(Collectors.toUnmodifiableList());

    private final List<Rule> rules = Arrays.stream(chunks.get(0).split(System.lineSeparator()))
                                           .map(this::parseRules)
                                           .collect(Collectors.toUnmodifiableList());

    private final List<Integer> nearbyTickets = Arrays.stream(chunks.get(2).split(System.lineSeparator()))
                                                      .skip(1)
                                                      .flatMap(l -> Arrays.stream(l.split(",")))
                                                      .map(Integer::valueOf)
                                                      .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return nearbyTickets.stream()
                            .filter(ticket -> !isValid(ticket, rules))
                            .mapToInt(ticket -> ticket)
                            .sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    private Rule parseRules(String rules) {
        var nameInterval = rules.split(": ");
        var name = nameInterval[0];
        var intervals = Arrays.stream(nameInterval[1].split(" or "))
                              .map(this::parseInterval)
                              .toArray(int[][]::new);
        return new Rule(name, intervals[0][0], intervals[0][1], intervals[1][0], intervals[1][1]);
    }

    private int[] parseInterval(String interval) {
        return Arrays.stream(interval.split("-"))
                     .mapToInt(Integer::parseInt)
                     .toArray();
    }

    private boolean isValid(int ticket, List<Rule> rules) {
        return rules.stream().anyMatch(rule -> isValidRule(ticket, rule));
    }

    private boolean isValidRule(int ticket, Rule rule) {
        return ticket >= rule.min1 && ticket <= rule.max1 || ticket >= rule.min2 && ticket <= rule.max2;
    }

    private record Rule(String name, int min1, int max1, int min2, int max2) {
    }
}
