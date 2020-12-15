package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 implements Day {
    public static void main(String[] args) {
        new Day15().printParts();
    }

    private final List<Integer> numbers = Arrays.stream(readDay(15).split(","))
                                                .map(Integer::parseInt)
                                                .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return play(2020);
    }

    @Override
    public Object part2() {
        return play(30_000_000);
    }

    private int play(int numTurn) {
        var turnHistory = new int[numTurn];
        var countHistory = new int[numTurn];
        IntStream.range(0, numbers.size()).forEach(turn -> {
            var spoken = numbers.get(turn);
            turnHistory[spoken] = turn + 1;
            countHistory[spoken]++;
        });

        var turn = numbers.size() + 1;
        var lastSpoken = numbers.get(numbers.size() - 1);
        var spoken = -1;
        while (turn < numTurn) {
            spoken = countHistory[lastSpoken] == 0 ? 0 : turn - turnHistory[lastSpoken];
            turnHistory[lastSpoken] = turn;
            countHistory[lastSpoken]++;
            lastSpoken = spoken;
            turn++;
        }
        return lastSpoken;
    }
}
