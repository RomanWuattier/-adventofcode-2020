package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 implements Day {
    public static void main(String[] args) {
        new Day12().printParts();
    }

    private final List<String> directions = Arrays.stream(readDay(12).split(System.lineSeparator()))
                                                  .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var ship = new HashMap<String, Coord>();
        ship.put("pos", new Coord(0, 0));
        ship.put("orientation", new Coord(0, 1));
        move(directions, ship, "pos", "orientation");
        return Math.abs(ship.get("pos").y) + Math.abs(ship.get("pos").x);
    }

    @Override
    public Object part2() {
        var ship = new HashMap<String, Coord>();
        ship.put("waypoint", new Coord(-1, 10));
        ship.put("pos", new Coord(0, 0));
        move(directions, ship, "waypoint", "waypoint");
        return Math.abs(ship.get("pos").y) + Math.abs(ship.get("pos").x);
    }

    // A map represents the ship and should contain the key `pos` representing the ship's position. It is required to move forward.
    // The movable and orientation keys represent the action of moving or going forward.
    // The map gracefully enables to reuse the same function for both the part 1 and 2.
    private void move(List<String> dirs, final Map<String, Coord> ship, String movableKey, String orientationKey) {
        dirs.forEach(d -> {
            var action = d.charAt(0);
            var magnitude = Integer.parseInt(d.substring(1));
            var movableCoord = ship.get(movableKey);
            switch (action) {
                case 'N' -> ship.put(movableKey, movableCoord.north(magnitude));
                case 'S' -> ship.put(movableKey, movableCoord.south(magnitude));
                case 'E' -> ship.put(movableKey, movableCoord.east(magnitude));
                case 'W' -> ship.put(movableKey, movableCoord.west(magnitude));
                case 'L', 'R' -> {
                    if (magnitude % 90 != 0) {
                        throw new IllegalStateException();
                    }
                    // The orientation could be computed without a loop, but F is constant
                    var timesToRotate = (magnitude / 90);
                    IntStream.range(0, timesToRotate).forEach(__ -> {
                        var orientationCoord = ship.get(orientationKey);
                        ship.put(orientationKey, action == 'L' ? orientationCoord.turnLeft() : orientationCoord.turnRight());
                    });
                }
                case 'F' -> {
                    var shipPos = ship.get("pos");
                    var orientationCoord = ship.get(orientationKey);
                    ship.put("pos", shipPos.forward(orientationCoord.y, orientationCoord.x, magnitude));
                }
                default -> throw new IllegalStateException("Unexpected value: " + action);
            }
        });
    }

    private record Coord(int y, int x) {
        Coord north(int magnitude) {
            return new Coord(y - magnitude, x);
        }

        Coord south(int magnitude) {
            return new Coord(y + magnitude, x);
        }

        Coord east(int magnitude) {
            return new Coord(y, x + magnitude);
        }

        Coord west(int magnitude) {
            return new Coord(y, x - magnitude);
        }

        Coord turnLeft() {
            return new Coord(-x, y);
        }

        Coord turnRight() {
            return new Coord(x, -y);
        }

        Coord forward(int dy, int dx, int magnitude) {
            return new Coord(y + dy * magnitude, x + dx * magnitude);
        }
    }
}
