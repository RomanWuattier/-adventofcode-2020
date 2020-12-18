package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day18 implements Day {
    public static void main(String[] args) {
        new Day18().printParts();
    }

    private final List<String> input = Arrays.stream(readDay(18).split(System.lineSeparator()))
                                             .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return input.stream()
                    .map(row -> infixToReversedPolishNotation(row, Map.of('+', 1, '*', 1, '(', 0)))
                    .map(this::evaluateReversedPolishNotation)
                    .mapToLong(i -> i)
                    .sum();
    }

    @Override
    public Object part2() {
        return input.stream()
                    .map(row -> infixToReversedPolishNotation(row, Map.of('+', 2, '*', 1, '(', 0)))
                    .map(this::evaluateReversedPolishNotation)
                    .mapToLong(i -> i)
                    .sum();
    }

    private Queue<Character> infixToReversedPolishNotation(String infix, Map<Character, Integer> precedence) {
        var rpn = new LinkedList<Character>();
        var ops = new Stack<Character>();
        for (char c : infix.toCharArray()) {
            if (c == ' ') {
                continue;
            }

            if (Character.isDigit(c)) {
                rpn.offer(c);
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    rpn.offer(ops.pop());
                }
                ops.pop();
            } else if (precedence.containsKey(c)) {
                while (!ops.isEmpty() && precedence.get(c) <= precedence.get(ops.peek())) {
                    rpn.offer(ops.pop());
                }
                ops.push(c);
            } else {
                throw new IllegalArgumentException("Invalid infix notation " + infix);
            }
        }

        while (!ops.isEmpty()) {
            rpn.add(ops.pop());
        }
        return rpn;
    }

    private long evaluateReversedPolishNotation(Queue<Character> rpn) {
        var stack = new Stack<Long>();
        while (!rpn.isEmpty()) {
            var valOrOp = rpn.poll();
            if (Character.isDigit(valOrOp)) {
                stack.push((long) Character.getNumericValue(valOrOp));
            } else if (valOrOp == '+') {
                stack.push(stack.pop() + stack.pop());
            } else if (valOrOp == '*') {
                stack.push(stack.pop() * stack.pop());
            } else {
                throw new IllegalArgumentException("Invalid RPN notation " + rpn);
            }
        }
        if (stack.size() > 1) {
            throw new IllegalStateException("Something went wrong :/");
        }
        return stack.pop();
    }
}
