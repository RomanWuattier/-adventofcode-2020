package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 implements Day {
    public static void main(String[] args) {
        new Day13().printParts();
    }

    private final List<String> input = Arrays.stream(readDay(13).split(System.lineSeparator()))
                                             .collect(Collectors.toUnmodifiableList());
    private final List<String> buses = Arrays.stream(input.get(1).split(","))
                                             .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var timestamp = Integer.parseInt(input.get(0));
        return buses.stream()
                    .filter(id -> !"x".equals(id))
                    .map(Integer::parseInt)
                    .map(id -> new int[]{id - (timestamp % id), id})
                    .min(Comparator.comparingInt(minutesBusId -> minutesBusId[0]))
                    .map(minutesBusId -> minutesBusId[0] * minutesBusId[1])
                    .orElseThrow();
    }

    @Override
    public Object part2() {
        // Floyd's Tortoise and Hare cycle detection algorithm may work if we find the timestamp of the first cycle
        // for each bus. Then find the least common multiplier of all timestamps.
        // But, I went for a hint on Redis and decided to use the Chinese Remainder Theorem https://brilliant.org/wiki/chinese-remainder-theorem/
        var n = buses.stream()
                     .filter(id -> !"x".equals(id))
                     .map(Long::parseLong)
                     .collect(Collectors.toUnmodifiableList());
        var a = IntStream.range(0, buses.size())
                         .filter(i -> !"x".equals(buses.get(i)))
                         .mapToObj(i -> Math.floorMod(-i, Long.parseLong(buses.get(i))))
                         .collect(Collectors.toUnmodifiableList());
        return applyChineseRemainderTheorem(n, a);
    }

    private long applyChineseRemainderTheorem(List<Long> n, List<Long> a) {
        var product = n.stream().reduce((x, y) -> x * y).orElseThrow();
        var sum = 0L;
        for (var i = 0; i < n.size(); i++) {
            var p = product / n.get(i);
            sum += a.get(i) * BigInteger.valueOf(p).modInverse(BigInteger.valueOf(n.get(i))).longValue() * p;
        }
        return sum % product;
    }
}
