package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 implements Day {
    public static void main(String[] args) {
        new Day13().printParts();
    }

    private final List<String> input = Arrays.stream(readDay(13).split(System.lineSeparator()))
                                             .collect(Collectors.toUnmodifiableList());
    private final List<Integer> buses = Arrays.stream(input.get(1).split(","))
                                              .filter(id -> !"x".equals(id))
                                              .map(Integer::parseInt)
                                              .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var timestamp = Integer.parseInt(input.get(0));
        return buses.stream().map(id -> new int[]{id - (timestamp % id), id})
                    .min(Comparator.comparingInt(minutesBusId -> minutesBusId[0]))
                    .map(minutesBusId -> minutesBusId[0] * minutesBusId[1])
                    .orElseThrow();
    }

    @Override
    public Object part2() {
        // Floyd's Tortoise and Hare cycle detection algorithm may work if we find the timestamp of the first cycle
        // for each bus. Then find the least common multiplier of all timestamps.
        // To understand how to apply this algorithm to the input, I need to simulate a small number of buses.
        return null;
    }
}
