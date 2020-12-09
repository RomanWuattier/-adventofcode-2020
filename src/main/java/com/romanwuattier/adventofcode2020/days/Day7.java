package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 implements Day {
    public static void main(String[] args) {
        new Day7().printParts();
    }

    private final String myBag = "shiny gold";
    // An index storing all child bags.
    private final Map<String, Set<Bag>> contain = new HashMap<>();
    // A reverse index storing all parent bags to make more efficient finding all possible paths from a key.
    private final Map<String, Set<String>> containedBy = new HashMap<>();

    Day7() {
        Arrays.stream(readDay(7).split(System.lineSeparator()))
              .map(rule -> rule.strip().split("contain"))
              .forEach(splitRule -> {
                  var containingBag = splitRule[0].replace("bags", "").strip();
                  var containedBags = splitRule[1].strip();

                  contain.putIfAbsent(containingBag, new HashSet<>());
                  if (containedBags.equalsIgnoreCase("no other bags.")) {
                      return;
                  }
                  Arrays.stream(containedBags.split(","))
                        .map(contained -> {
                            var bags = contained.strip().split(" ");
                            var qte = Integer.parseInt(bags[0]);
                            var bagName = Arrays.stream(bags, 1, bags.length - 1)
                                                .collect(Collectors.joining(" "))
                                                .strip();
                            return new Bag(bagName, qte);
                        })
                        .forEach(containedBag -> {
                            contain.get(containingBag).add(containedBag);

                            containedBy.putIfAbsent(containedBag.name, new HashSet<>());
                            containedBy.get(containedBag.name).add(containingBag);
                        });
              });
    }

    @Override
    public Object part1() {
        // Thanks to the reverse index, we don't need to scan all the index to find a path.
        // Starting from a specific key leads to all good paths. The set avoids counting multiple times the same bag.
        // If we use a BFS to solve part 1, the reverse index will provide a better runtime too.
        return dfsFromChildrenToParentBags(myBag, new HashSet<>());
    }

    private int dfsFromChildrenToParentBags(String node, Set<String> seen) {
        seen.add(node);
        return Optional.ofNullable(containedBy.get(node))
                       .map(neighs -> neighs.stream()
                                            .filter(neigh -> !seen.contains(neigh))
                                            .mapToInt(neigh -> 1 + dfsFromChildrenToParentBags(neigh, seen))
                                            .sum())
                       .orElse(0);
    }

    @Override
    public Object part2() {
        // We use the classic index as we travel from the parent to the children
        return dfsFromParentToChildrenBags(myBag);
    }

    private int dfsFromParentToChildrenBags(String bag) {
        return contain.get(bag)
                      .stream()
                      .mapToInt(containedBag -> containedBag.qte * (1 + dfsFromParentToChildrenBags(containedBag.name)))
                      .sum();
    }

    private record Bag(String name, int qte) {
    }
}
