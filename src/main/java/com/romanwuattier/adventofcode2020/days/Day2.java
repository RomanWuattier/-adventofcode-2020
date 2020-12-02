package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;
import lombok.Value;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day2 implements Day {
    public static void main(String[] args) {
        new Day2().printParts();
    }

    @Override
    public Object part1() {
        return getPolicies().filter(policy -> {
            long occ = policy.pwd.chars().filter(c -> c == policy.c).count();
            return occ >= policy.min && occ <= policy.max;
        }).count();
    }

    @Override
    public Object part2() {
        return getPolicies().filter(policy -> {
            char[] pwd = policy.pwd.toCharArray();
            return (pwd[policy.min - 1] == policy.c) != (pwd[policy.max - 1] == policy.c);
        }).count();
    }

    private Stream<PwdPolicy> getPolicies() {
        return Arrays.stream(readDay(2).split(System.lineSeparator()))
                     .map(line -> line.split(" "))
                     .map(line -> {
                         int min = Integer.parseInt(line[0].split("-")[0]);
                         int max = Integer.parseInt(line[0].split("-")[1]);
                         return new PwdPolicy(min, max, line[1].charAt(0), line[2]);
                     });
    }

    @Value
    private static class PwdPolicy {
        int min, max;
        char c;
        String pwd;
    }
}
