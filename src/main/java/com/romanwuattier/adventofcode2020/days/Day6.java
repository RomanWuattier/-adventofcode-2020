package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6 implements Day {
    public static void main(String[] args) {
        new Day6().printParts();
    }

    private final List<String> answers = Arrays.stream(readDay(6).strip().split("\n\n"))
                                               .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return answers.stream()
                      .map(group -> {
                          // Since we want to consider the group, we remove all newlines to build a single string
                          return group.replaceAll("\n", "");
                      })
                      .map(l -> l.chars().boxed().distinct())
                      .mapToLong(Stream::count)
                      .sum();
    }

    @Override
    public Object part2() {
        return answers.stream()
                      .map(group -> {
                          // Since we want to consider each person in a group, we build a new stream for each line
                          return Arrays.stream(group.split("\n"));
                      })
                      .map(group -> group.map(l -> l.chars().boxed().collect(Collectors.toUnmodifiableSet()))
                                         .reduce((set1, set2) -> {
                                             // Reducing may seem to be inefficient at first glance. But, because the
                                             // `Set#contains` function runs in constant time, it computes the
                                             // intersection of two sets in O(n) time. There are only 26 yes-or-no
                                             // questions, n is at most 26.
                                             var intersection = new HashSet<>(set1);
                                             intersection.retainAll(set2);
                                             return intersection;
                                         }).orElseThrow())
                      .mapToInt(Set::size)
                      .sum();
    }
}
