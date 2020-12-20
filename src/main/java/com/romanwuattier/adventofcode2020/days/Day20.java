package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20 implements Day {
    public static void main(String[] args) {
        new Day20().printParts();
    }

    private final List<String> chunks = Arrays.stream(readDay(20).split("\n\n"))
                                              .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var tiles = chunks.stream()
                          .map(Tile::init)
                          .collect(Collectors.toUnmodifiableMap(t -> t.id, t -> t));

        var borderMatch = new HashMap<Integer, List<Integer>>();
        tiles.values().forEach(tile -> {
            tile.borders.forEach((orientation, border) -> {
                var key = key(border);
                borderMatch.putIfAbsent(key, new ArrayList<>());
                borderMatch.get(key).add(tile.id);
            });
        });

        borderMatch.values().forEach(tilesMatchedForBorder -> {
            if (tilesMatchedForBorder.size() > 2) {
                throw new IllegalStateException("A border can't be associated with more than 2 tiles");
            }
            if (tilesMatchedForBorder.size() == 2) {
                var t1 = tilesMatchedForBorder.get(0);
                var t2 = tilesMatchedForBorder.get(1);
                tiles.get(t1).neigh.add(t2);
                tiles.get(t2).neigh.add(t1);
            }
        });

        return tiles.values().stream()
                    .filter(t -> t.neigh.size() == 2)
                    .mapToLong(t -> t.id)
                    .reduce((i1, i2) -> i1 * i2)
                    .orElseThrow();
    }

    @Override
    public Object part2() {
        // To build the image, we'll need to associate an orientation to the neighbor tiles. For each tile, rotate
        // all neighbors until its orientation matches the one of the current tile's opposite
        // (right -> left, to -> bottom, etc.).
        return null;
    }

    private int key(String s) {
        var key = s.hashCode();
        var reversedKey = new StringBuilder(s).reverse().toString().hashCode();
        return Math.min(key, reversedKey);
    }

    private record Tile(int id, String[] grid, Map<String, String> borders, Set<Integer> neigh) {
        static Tile init(String tile) {
            var id = Integer.parseInt(tile.substring(0, tile.indexOf('\n')).split(" ")[1].replace(":", ""));
            var grid = tile.substring(tile.indexOf('\n') + 1).split("\n");
            return new Tile(id, grid, getBorders(grid), new HashSet<>());
        }

        private static Map<String, String> getBorders(String[] grid) {
            var top = grid[0].strip();
            var bottom = grid[grid.length - 1].strip();
            var left = "";
            var right = "";
            for (String row : grid) {
                left += row.charAt(0);
                right += row.charAt(row.length() - 1);
            }
            return Map.of("top", top, "bottom", bottom, "left", left, "right", right);
        }
    }
}
