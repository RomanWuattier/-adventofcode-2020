package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day4 implements Day {
    public static void main(String[] args) {
        new Day4().printParts();
    }

    private static final Set<String> VALID_EYES = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    private static final Map<String, Predicate<String>> VALID_PASSPORT = Map.of(
        "byr", s -> isValidYear(s, 1920, 2002),
        "iyr", s -> isValidYear(s, 2010, 2020),
        "eyr", s -> isValidYear(s, 2020, 2030),
        "hgt", s -> {
            if (s.length() < 4) {
                return false;
            }
            int size = Integer.parseInt(s.substring(0, s.length() - 2));
            if (s.endsWith("cm")) {
                return size >= 150 && size <= 193;
            } else if (s.endsWith("in")) {
                return size >= 59 && size <= 76;
            }
            return false;
        },
        "hcl", s -> s.matches("#[0-9a-f]{6}"),
        "ecl", VALID_EYES::contains,
        "pid", s -> s.matches("[0-9]{9}")
    );

    private static boolean isValidYear(String s, int min, int max) {
        return s.matches("[0-9]+") && Integer.parseInt(s) >= min && Integer.parseInt(s) <= max;
    }

    private final List<Map<String, String>> passports = Arrays.stream(readDay(4).strip().split("\n\n"))
                                                              .map(block -> block.replace("\n", " "))
                                                              .map(block -> block.split(" "))
                                                              .map(kv -> Arrays.stream(kv)
                                                                               .map(s -> s.split(":"))
                                                                               .collect(Collectors.toMap(a -> a[0], a -> a[1])))
                                                              .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return passports.stream().filter(passport -> VALID_PASSPORT.keySet()
                                                                   .stream()
                                                                   .allMatch(passport::containsKey))
                        .count();
    }

    @Override
    public Object part2() {
        return passports.stream()
                        .filter(passport -> VALID_PASSPORT.entrySet()
                                                          .stream()
                                                          .allMatch(e -> {
                                                              String requiredKey = e.getKey();
                                                              return passport.containsKey(requiredKey) && e.getValue().test(passport.get(requiredKey));
                                                          }))
                        .count();
    }
}
