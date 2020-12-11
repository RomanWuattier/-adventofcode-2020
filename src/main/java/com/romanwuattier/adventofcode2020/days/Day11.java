package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;

public class Day11 implements Day {
    public static void main(String[] args) {
        new Day11().printParts();
    }

    private final int[][] grid = Arrays.stream(readDay(11).split(System.lineSeparator()))
                                       .map(l -> l.chars().toArray())
                                       .toArray(int[][]::new);

    @Override
    public Object part1() {
        return simulate(grid.clone(), 4, false);
    }

    @Override
    public Object part2() {
        return simulate(grid.clone(), 5, true);
    }

    private long simulate(int[][] seats, int threshold, boolean increasedVisibility) {
        var dirs = List.of(-1, 0, 1);
        while (true) {
            var nextSeats = new int[seats.length][seats[0].length];
            for (var row = 0; row < seats.length; row++) {
                for (var col = 0; col < seats[0].length; col++) {
                    var canSee = 0;
                    if (seats[row][col] != '.') {
                        for (var dr : dirs) {
                            for (var dc : dirs) {
                                if (dr == 0 && dc == 0) {
                                    continue;
                                }
                                if (increasedVisibility) {
                                    // Part 2
                                    if (seeOccupiedSeat(seats, row, col, dr, dc)) {
                                        canSee++;
                                    }
                                } else {
                                    // Part 1
                                    var r = row + dr;
                                    var c = col + dc;
                                    if (isSafe(seats, r, c) && seats[r][c] == '#') {
                                        canSee++;
                                    }
                                }
                            }
                        }
                    }
                    var c = seats[row][col];
                    if (c == 'L' && canSee == 0) {
                        nextSeats[row][col] = '#';
                    } else if (c == '#' && canSee >= threshold) {
                        nextSeats[row][col] = 'L';
                    } else {
                        nextSeats[row][col] = (char) c;
                    }
                }
            }

            if (Arrays.deepEquals(seats, nextSeats)) {
                return Arrays.stream(nextSeats)
                             .flatMapToInt(l -> Arrays.stream(l).filter(c -> c == '#'))
                             .count();
            }
            seats = nextSeats;
        }
    }

    private boolean seeOccupiedSeat(int[][] seats, int row, int col, int r, int c) {
        var nr = row + r;
        var nc = col + c;
        while (isSafe(seats, nr, nc) && seats[nr][nc] == '.') {
            nr += r;
            nc += c;
        }
        return isSafe(seats, nr, nc) && seats[nr][nc] == '#';
    }

    private boolean isSafe(int[][] seats, int row, int col) {
        return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length;
    }
}
