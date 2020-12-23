package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day23 implements Day {
    public static void main(String[] args) {
        new Day23().printParts();
    }

    private final int[] cups = Arrays.stream(readDay(23).split("")).mapToInt(Integer::parseInt).toArray();

    @Override
    public Object part1() {
        var references = new HashMap<Integer, Node>();
        Node head = null;
        Node current = null;
        for (var val : cups) {
            if (current == null) {
                current = new Node(val);
                head = current;
            } else {
                current.next = new Node(val);
                current = current.next;
            }
            references.put(val, current);
        }
        current.next = head;

        play(head, 9, references, 100);
        var startingNode = references.get(1).next;
        var sb = new StringBuilder();
        while (startingNode.val != 1) {
            sb.append(startingNode.val);
            startingNode = startingNode.next;
        }
        return sb.toString();
    }

    @Override
    public Object part2() {
        var references = new HashMap<Integer, Node>();
        var max = 0;
        Node head = null;
        Node current = null;
        for (var val : cups) {
            if (current == null) {
                current = new Node(val);
                head = current;
            } else {
                current.next = new Node(val);
                current = current.next;
            }
            references.put(val, current);
            current.next = head;
            max = Math.max(max, val);
        }

        for (var val = max + 1; val <= 1000000; val++) {
            current.next = new Node(val);
            current = current.next;
            references.put(val, current);
        }
        current.next = head;

        play(head, 1000000, references, 10000000);
        return (long) references.get(1).next.val * references.get(1).next.next.val;
    }

    private void play(Node head, int max, Map<Integer, Node> references, int moves) {
        var current = head;
        while (moves-- > 0) {
            var one = current.next;
            var two = current.next.next;
            var three = current.next.next.next;
            current.next = three.next;
            var destination = current.val - 1;
            while (destination < 1 || destination == one.val || destination == two.val || destination == three.val) {
                destination = destination < 1 ? max : destination - 1;
            }
            var destinationNode = references.get(destination);
            three.next = destinationNode.next;
            destinationNode.next = one;
            current = current.next;
        }
    }

    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }
}
