package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 implements Day {
    public static void main(String[] args) {
        new Day24().printParts();
    }

    private static final String RE = "(se)|(sw)|(nw)|(ne)|(e)|(w)";

    private final Set<Coord> blackTiles = new HashSet<>();

    Day24() {
        Arrays.stream(readDay(24).split("\n"))
              .map(l -> Pattern.compile(RE).matcher(l).results().map(MatchResult::group).toArray(String[]::new))
              .forEach(dirs -> {
                  var c = new Coord(0, 0);
                  for (var d : dirs) {
                      c = switch (d) {
                          case "e" -> Dirs.E.f.apply(c);
                          case "se" -> Dirs.SE.f.apply(c);
                          case "sw" -> Dirs.SW.f.apply(c);
                          case "w" -> Dirs.W.f.apply(c);
                          case "nw" -> Dirs.NW.f.apply(c);
                          case "ne" -> Dirs.NE.f.apply(c);
                          default -> throw new IllegalArgumentException("Unknown direction " + d);
                      };
                  }
                  if (blackTiles.contains(c)) {
                      blackTiles.remove(c);
                  } else {
                      blackTiles.add(c);
                  }
              });
    }

    @Override
    public Object part1() {
        return blackTiles.size();
    }

    @Override
    public Object part2() {
        return live(blackTiles, 100).size();
    }

    private Set<Coord> live(Set<Coord> blackTiles, int days) {
        for (int i = 0; i < days; i++) {
            var currentBlackTiles = new HashSet<>(blackTiles);
            var nextBlackTiles = new HashSet<Coord>();
            var allNeigh = new HashMap<Coord, Integer>();

            currentBlackTiles.forEach(coord -> {
                var neighs = Arrays.stream(Dirs.values())
                                   .map(dir -> dir.f.apply(coord))
                                   .peek(neigh -> allNeigh.put(neigh, allNeigh.getOrDefault(neigh, 0) + 1))
                                   .collect(Collectors.toCollection(HashSet::new));

                var intersectBlackTiles = new HashSet<>(currentBlackTiles);
                intersectBlackTiles.retainAll(neighs);
                if (intersectBlackTiles.size() == 1 || intersectBlackTiles.size() == 2) {
                    nextBlackTiles.add(coord);
                }
            });

            allNeigh.entrySet().stream()
                    .filter(e -> !nextBlackTiles.contains(e.getKey()) && !currentBlackTiles.contains(e.getKey()))
                    .filter(e -> e.getValue() == 2)
                    .forEach(e -> nextBlackTiles.add(e.getKey()));

            blackTiles = nextBlackTiles;
        }
        return blackTiles;
    }

    private record Coord(int y, int x) {
    }

    private enum Dirs {
        E(c -> new Coord(c.y + 1, c.x + 1)),
        SE(c -> new Coord(c.y, c.x + 1)),
        SW(c -> new Coord(c.y - 1, c.x)),
        W(c -> new Coord(c.y - 1, c.x - 1)),
        NW(c -> new Coord(c.y, c.x - 1)),
        NE(c -> new Coord(c.y + 1, c.x));

        Function<Coord, Coord> f;

        Dirs(Function<Coord, Coord> f) {
            this.f = f;
        }
    }
}
