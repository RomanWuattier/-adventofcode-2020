package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 implements Day {
    public static void main(String[] args) {
        new Day25().printParts();
    }

    private final List<Integer> publicKeys = Arrays.stream(readDay(25).split("\n"))
                                                   .map(Integer::parseInt)
                                                   .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return findEncryptionKey(publicKeys.get(0), publicKeys.get(1));
    }

    @Override
    public Object part2() {
        return "Day 25 has no part 2";
    }

    private long findEncryptionKey(int cardPk, int doorPk) {
        var it = findLoopSize(cardPk, 7);
        var encryptionKey = 1L;
        for (var i = 0; i < it; i++) {
            encryptionKey = (doorPk * encryptionKey) % 20201227;
        }
        return encryptionKey;
    }

    private int findLoopSize(int pk, int subjectNumber) {
        var it = 0;
        var value = 1;
        while (value != pk) {
            value = (subjectNumber * value) % 20201227;
            it++;
        }
        return it;
    }
}
