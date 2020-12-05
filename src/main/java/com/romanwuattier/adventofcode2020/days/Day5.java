package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 implements Day {
    public static void main(String[] args) {
        new Day5().printParts();
    }

    private final Set<Integer> seatIds = Arrays.stream(readDay(5).split(System.lineSeparator()))
                                               .map(this::getSeatId)
                                               .collect(Collectors.toUnmodifiableSet());

    @Override
    public Object part1() {
        return seatIds.stream().max(Comparator.naturalOrder()).orElseThrow();
    }

    @Override
    public Object part2() {
        var stats = seatIds.stream().mapToInt(i -> i).summaryStatistics();
        return IntStream.rangeClosed(stats.getMin(), stats.getMax())
                        .filter(i -> !seatIds.contains(i))
                        .findFirst()
                        .orElseThrow();
    }

    private int getSeatId(String pass) {
        return binarySearch(0, 127, pass.substring(0, 7)) * 8 + binarySearch(0, 7, pass.substring(7));
    }

    private int binarySearch(int low, int high, String chars) {
        for (char c : chars.toCharArray()) {
            int pivot = (high - low) / 2 + low;
            if (c == 'F' || c == 'L') {
                high = pivot;
            } else if (c == 'B' || c == 'R') {
                low = pivot + 1;
            } else {
                throw new IllegalArgumentException("Invalid argument: " + chars);
            }
        }
        return low;
    }
}
