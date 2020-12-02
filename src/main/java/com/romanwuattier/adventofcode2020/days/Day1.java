package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Day1 implements Day {
    public static void main(String[] args) {
        new Day1().printParts();
    }

    private final Set<Integer> unique = getNumbers();

    @Override
    public Object part1() {
        return unique.stream()
                     .filter(v -> unique.contains(2020 - v))
                     .findFirst()
                     .map(v -> v * (2020 - v))
                     .orElseThrow();
    }

    @Override
    public Object part2() {
        var sumMul = unique.stream()
                           .flatMap(v1 -> unique.stream().map(v2 -> new int[]{v1, v2}))
                           .collect(Collectors.toMap(a -> a[0] + a[1], a -> a[0] * a[1], (i1, i2) -> i1));

        return unique.stream()
                     .filter(v -> sumMul.containsKey(2020 - v))
                     .findFirst()
                     .map(v -> v * sumMul.get(2020 - v))
                     .orElseThrow();
    }

    private Set<Integer> getNumbers() {
        return Arrays.stream(readDay(1).split(System.lineSeparator()))
                     .mapToInt(Integer::parseInt)
                     .boxed()
                     .collect(Collectors.toUnmodifiableSet());
    }
}
