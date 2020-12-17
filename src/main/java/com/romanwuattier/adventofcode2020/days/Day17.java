package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 implements Day {
    public static void main(String[] args) {
        new Day17().printParts();
    }

    private final Boolean[][] grid = Arrays.stream(readDay(17).split(System.lineSeparator()))
                                           .map(l -> Arrays.stream(l.split(""))
                                                           .map(s -> s.equals("#"))
                                                           .toArray(Boolean[]::new))
                                           .toArray(Boolean[][]::new);

    @Override
    public Object part1() {
        var activeCells = getActivePoint();
        for (int cycle = 0; cycle < 6; cycle++) {
            activeCells = nextCycle(activeCells, false);
        }
        return activeCells.size();
    }

    @Override
    public Object part2() {
        var activeCells = getActivePoint();
        for (int cycle = 0; cycle < 6; cycle++) {
            activeCells = nextCycle(activeCells, true);
        }
        return activeCells.size();

    }

    private Set<Point> getActivePoint() {
        Set<Point> activeCells = new HashSet<>();
        IntStream.range(0, grid.length).forEach(row -> {
            IntStream.range(0, grid[0].length).forEach(col -> {
                if (grid[row][col]) {
                    activeCells.add(new Point(col, row, 0, 0));
                }
            });
        });
        return activeCells;
    }

    private Set<Point> nextCycle(Set<Point> activeCells, boolean isFourD) {
        var nextCandidate = activeCells.stream()
                                       .flatMap(cell -> findNeigh(cell, isFourD).stream())
                                       .collect(Collectors.toUnmodifiableSet());

        return nextCandidate.stream().filter(point -> {
            var activeNeigh = findNeigh(point, isFourD).stream().filter(activeCells::contains).count();
            if (activeCells.contains(point)) {
                return activeNeigh == 2 || activeNeigh == 3;
            } else {
                return activeNeigh == 3;
            }
        }).collect(Collectors.toUnmodifiableSet());
    }

    private Set<Point> findNeigh(Point point, boolean isFourD) {
        var x = point.x;
        var y = point.y;
        var z = point.z;
        var w = point.w;
        var neigh = new HashSet<Point>();
        var dirs = new int[]{-1, 0, 1};
        Arrays.stream(dirs).forEach(zDir -> {
            Arrays.stream(dirs).forEach(xDir -> {
                Arrays.stream(dirs).forEach(yDir -> {
                    if (isFourD) {
                        Arrays.stream(dirs)
                              .filter(wDir -> zDir != 0 || xDir != 0 || yDir != 0 || wDir != 0)
                              .mapToObj(wDir -> new Point(x + xDir, y + yDir, z + zDir, w + wDir))
                              .forEach(neigh::add);
                    } else {
                        if (zDir == 0 && xDir == 0 && yDir == 0) {
                            return;
                        }
                        neigh.add(new Point(x + xDir, y + yDir, z + zDir, 0));
                    }
                });
            });
        });
        return neigh;
    }

    private record Point(int x, int y, int z, int w) {
    }
}
