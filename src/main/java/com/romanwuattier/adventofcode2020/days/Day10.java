package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day10 implements Day {
    public static void main(String[] args) {
        new Day10().printParts();
    }

    private final List<Long> sortedJolts = Arrays.stream(readDay(10).split(System.lineSeparator()))
                                                 .map(Long::parseLong)
                                                 .sorted()
                                                 .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var three = 1;
        var one = 1;
        for (int i = 1; i < sortedJolts.size(); i++) {
            var prev = sortedJolts.get(i - 1);
            if (sortedJolts.get(i) - prev == 3) {
                three++;
            } else if (sortedJolts.get(i) - prev == 1) {
                one++;
            }
        }
        return one * three;
    }

    @Override
    public Object part2() {
        var steps = List.of(1, 2, 3);
        var target = sortedJolts.get(sortedJolts.size() - 1) + 3;
        var uniqJolts = new HashSet<>(sortedJolts);
        uniqJolts.add(0L);
        uniqJolts.add(target);
        return dfs(target, uniqJolts, steps, new HashMap<>());
    }

    private long dfs(long target, Set<Long> uniqJolts, List<Integer> steps, Map<Long, Long> cache) {
        if (!uniqJolts.contains(target) || target < 0) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        if (cache.containsKey(target)) {
            return cache.get(target);
        }

        var sum = 0L;
        for (var step : steps) {
            sum += dfs(target - step, uniqJolts, steps, cache);
        }
        cache.put(target, sum);
        return sum;
    }
}
