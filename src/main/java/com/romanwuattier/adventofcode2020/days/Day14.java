package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 implements Day {
    public static void main(String[] args) {
        new Day14().printParts();
    }

    private final List<String> lines = Arrays.stream(readDay(14).split(System.lineSeparator()))
                                             .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var onesMask = 0L;
        var zerosMask = 0L;
        var maskPattern = Pattern.compile("mask = ([01X]+)");
        var memPattern = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
        var mem = new HashMap<Long, Long>();
        for (var l : lines) {
            var maskMatcher = maskPattern.matcher(l);
            if (maskMatcher.find()) {
                onesMask = Long.parseLong(maskMatcher.group(1).replace('X', '1').trim(), 2);
                zerosMask = Long.parseLong(maskMatcher.group(1).replace('X', '0').trim(), 2);
            } else {
                var memMatcher = memPattern.matcher(l);
                if (!memMatcher.find()) {
                    throw new IllegalArgumentException("Unexpected value: " + l);
                }
                var addr = Long.parseLong(memMatcher.group(1));
                var val = (Long.parseLong(memMatcher.group(2)) & onesMask) | zerosMask;
                mem.put(addr, val);
            }
        }
        return mem.values().stream().mapToLong(i -> i).sum();
    }

    @Override
    public Object part2() {
        var maskPattern = Pattern.compile("mask = ([01X]+)");
        var memPattern = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
        var mask = "";
        var mem = new HashMap<Long, Long>();
        for (var l : lines) {
            var maskMatcher = maskPattern.matcher(l);
            if (maskMatcher.find()) {
                mask = maskMatcher.group(1);
            } else {
                var memMatcher = memPattern.matcher(l);
                if (!memMatcher.find()) {
                    throw new IllegalArgumentException("Unexpected value: " + l);
                }
                var immutableMask = mask;
                var val = Long.parseLong(memMatcher.group(2));
                StringBuilder addrSb = new StringBuilder(Long.toBinaryString(Long.parseLong(memMatcher.group(1))));
                while (addrSb.length() < immutableMask.length()) {
                    addrSb.insert(0, '0');
                }
                var addr = addrSb.toString();

                char[] finalMask = IntStream.range(0, immutableMask.length())
                                            .mapToObj(i -> switch (immutableMask.charAt(i)) {
                                                case '0' -> addr.charAt(i);
                                                case '1' -> '1';
                                                case 'X' -> 'X';
                                                default -> throw new IllegalStateException("Unexpected value: " + immutableMask.charAt(i));
                                            })
                                            .map(Object::toString)
                                            .collect(Collectors.joining())
                                            .toCharArray();

                List<String> combinations = new ArrayList<>();
                maskCombination(finalMask, 0, combinations);
                combinations.forEach(r -> mem.put(Long.parseLong(r, 2), val));
            }
        }
        return mem.values().stream().mapToLong(i -> i).sum();
    }

    private void maskCombination(char[] chars, int i, List<String> res) {
        while (i < chars.length && chars[i] != 'X') i++;

        if (i >= chars.length) {
            res.add(String.valueOf(chars));
            return;
        }

        chars[i] = '0';
        maskCombination(chars, i + 1, res);
        chars[i] = '1';
        maskCombination(chars, i + 1, res);
        chars[i] = 'X';
    }
}