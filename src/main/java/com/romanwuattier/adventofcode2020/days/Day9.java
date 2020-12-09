package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 implements Day {
    public static void main(String[] args) {
        new Day9().printParts();
    }

    private final List<Long> numbers = Arrays.stream(readDay(9).split(System.lineSeparator()))
                                             .map(Long::parseLong)
                                             .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        // TwoSum within sliding window
        var preamble = 25;
        var window = new ArrayDeque<Long>(preamble);
        var occurrences = new HashMap<Long, Integer>();

        for (long x : numbers) {
            if (window.size() >= preamble) {
                boolean noneMatch = window.stream()
                                          .noneMatch(prev -> prev * 2 != x && occurrences.getOrDefault(x - prev, 0) > 0);
                if (noneMatch) {
                    return x;
                }

                var removed = window.poll();
                occurrences.put(removed, occurrences.get(removed) - 1);
                if (occurrences.get(removed) == 0) {
                    occurrences.remove(removed);
                }

            }
            window.offer(x);
            occurrences.put(x, occurrences.getOrDefault(x, 0) + 1);
        }
        throw new RuntimeException("No weakness in the XMAS data found");
    }

    @Override
    public Object part2() {
        var target = (long) part1();
        var cumulativeSum = numbers.toArray(Long[]::new);
        Arrays.parallelPrefix(cumulativeSum, Long::sum);
        var leftIndex = 0;
        var rightIndex = 0;
        var left = 0L;
        var right = 0L;
        var sum = 0L;
        while (sum != target) {
            if (sum < target) {
                right = cumulativeSum[rightIndex++];
            } else { // sum > target
                left = cumulativeSum[leftIndex++];
            }
            sum = right - left;
        }

        var stats = IntStream.rangeClosed(leftIndex, rightIndex)
                             .mapToLong(numbers::get)
                             .summaryStatistics();

        return stats.getMin() + stats.getMax();
    }
}
