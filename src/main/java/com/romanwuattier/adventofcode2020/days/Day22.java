package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day22 implements Day {
    public static void main(String[] args) {
        new Day22().printParts();
    }

    private final List<String[]> decks = Arrays.stream(readDay(22).split("\n\n"))
                                               .map(chunk -> chunk.split("\n"))
                                               .map(line -> Arrays.stream(line).skip(1).toArray(String[]::new))
                                               .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        var deck1 = Arrays.stream(decks.get(0)).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));
        var deck2 = Arrays.stream(decks.get(1)).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));

        var winner = play(deck1, deck2, false);
        return winner == -1 ? score(deck1) : score(deck2);
    }

    @Override
    public Object part2() {
        var deck1 = Arrays.stream(decks.get(0)).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));
        var deck2 = Arrays.stream(decks.get(1)).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));

        var winner = play(deck1, deck2, true);
        return winner == -1 ? score(deck1) : score(deck2);
    }

    private int score(Deque<Integer> deck) {
        var i = 1;
        var res = 0;
        while (!deck.isEmpty()) {
            res += deck.removeLast() * i++;
        }
        return res;
    }

    // Returns -1 when player 1 wins, 1 when player 2 wins, any other value is an illegal state.
    private int play(Deque<Integer> deck1, Deque<Integer> deck2, boolean isRecursiveCombat) {
        // The cache only works for the current game, each recursive call should create a new cache.
        var seen = new HashSet<String>();
        var winner = Integer.MAX_VALUE;

        while (!deck1.isEmpty() && !deck2.isEmpty()) {
            if (isRecursiveCombat) {
                var key = key(deck1, deck2);
                if (seen.contains(key)) {
                    // To prevent infinite game in Recursive Combat, player 1 always win the current game when the state has been seen before.
                    return -1;
                }
                seen.add(key);
            }

            var c1 = deck1.removeFirst();
            var c2 = deck2.removeFirst();

            if (isRecursiveCombat && deck1.size() >= c1 && deck2.size() >= c2) {
                winner = play(deck1.stream().limit(c1).collect(Collectors.toCollection(LinkedList::new)),
                              deck2.stream().limit(c2).collect(Collectors.toCollection(LinkedList::new)),
                              true);
            } else {
                winner = c2.compareTo(c1);
            }

            if (winner == -1) {
                deck1.offerLast(c1);
                deck1.offerLast(c2);
            } else if (winner == 1) {
                deck2.offerLast(c2);
                deck2.offerLast(c1);
            } else {
                throw new IllegalStateException("No winner?");
            }
        }
        return winner;
    }

    private String key(Deque<Integer> deck1, Deque<Integer> deck2) {
        return deck1.toString() + deck2.toString();
    }
}
