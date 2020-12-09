package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 implements Day {
    public static void main(String[] args) {
        new Day2().printParts();
    }

    private final List<Policy> policies = getPolicies();

    @Override
    public Object part1() {
        return policies.stream().filter(policy -> {
            long occ = policy.pwd.chars().filter(c -> c == policy.c).count();
            return occ >= policy.min && occ <= policy.max;
        }).count();
    }

    @Override
    public Object part2() {
        return policies.stream().filter(policy -> {
            char[] pwd = policy.pwd.toCharArray();
            return (pwd[policy.min - 1] == policy.c) != (pwd[policy.max - 1] == policy.c);
        }).count();
    }

    private List<Policy> getPolicies() {
        return Arrays.stream(readDay(2).split(System.lineSeparator()))
                     .map(l -> l.split(" "))
                     .map(w -> {
                         int min = Integer.parseInt(w[0].split("-")[0]);
                         int max = Integer.parseInt(w[0].split("-")[1]);
                         return new Policy(min, max, w[1].charAt(0), w[2]);
                     })
                     .collect(Collectors.toUnmodifiableList());
    }

    private record Policy(int min, int max, char c, String pwd) {
    }
}
