package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;

public class Day3 implements Day {
    public static void main(String[] args) {
        new Day3().printParts();
    }

    private final char[][] grid = Arrays.stream(readDay(3).split(System.lineSeparator()))
                                        .map(String::toCharArray)
                                        .toArray(char[][]::new);

    @Override
    public Object part1() {
        return countTrees(3, 1);
    }

    @Override
    public Object part2() {
        return Arrays.stream(new int[][]{{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}})
                     .map(rc -> countTrees(rc[0], rc[1]))
                     .reduce(1L, (a, b) -> a * b);
    }

    private long countTrees(int right, int down) {
        int trees = 0;
        for (int row = 0, col = 0; row < grid.length; row += down, col += right) {
            if (grid[row][col % grid[0].length] == '#') {
                trees++;
            }
        }
        return trees;
    }
}
