package com.romanwuattier.adventofcode2020.common;

public interface Day extends FileOperations {
    Object part1();

    Object part2();

    default void printParts() {
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }
}
