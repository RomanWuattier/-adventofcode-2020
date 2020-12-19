package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day19 implements Day {
    public static void main(String[] args) {
        new Day19().printParts();
    }

    private final List<String> chunks = Arrays.stream(readDay(19).split("\n\n"))
                                              .collect(Collectors.toUnmodifiableList());

    private final Map<String, String> rules = Arrays.stream(chunks.get(0).split("\n"))
                                                    .map(l -> l.split(": "))
                                                    .collect(Collectors.toUnmodifiableMap(s -> s[0], s -> s[1]));

    private final List<String> messages = Arrays.stream(chunks.get(1).split("\n"))
                                                .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var regexCombinations = combinations(rules.get("0"), rules, 0, new HashMap<>());
        return messages.stream().filter(message -> message.matches(regexCombinations)).count();
    }

    @Override
    public Object part2() {
        var rawRules = new HashMap<>(rules);
        rawRules.put("8", "42 | 42 8");
        rawRules.put("11", "42 31 | 42 11 31");

        var regexCombinations = combinations(rawRules.get("0"), rawRules, 0, new HashMap<>());
        return messages.stream().filter(message -> message.matches(regexCombinations)).count();
    }

    private String combinations(String rule, Map<String, String> rawRules, int depth, Map<String, String> cache) {
        // The depth is a hack to break the cycle. Once equal to 5, we have generated enough combinations for the
        // regexp to return the correct answer. I started at 20 and progressively decreased the value.
        // We could do something better because:
        // * Rule 8 becomes one or more instances of rule 42.
        // * Rule 11 is an equal number of rule 42 and 31.
        // But I could build the correct regex.
        if (depth > 5) {
            if (rule.equals("42 31 | 42 11 31")) {
                return "42 31";
            } else if (rule.equals("42 | 42 8")) {
                return "42";
            }
        }
        if (cache.containsKey(rule)) {
            return cache.get(rule);
        }

        var parts = rule.split(" ");
        var res = Arrays.stream(parts)
                        .map(part -> {
                            if (part.matches("\\d+")) {
                                part = "(" + combinations(rawRules.get(part), rawRules, depth + 1, cache) + ")";
                            } else {
                                part = part.replaceAll("\"", "");
                            }
                            return part;
                        })
                        .collect(Collectors.joining());
        cache.put(rule, res);
        return res;
    }
}
