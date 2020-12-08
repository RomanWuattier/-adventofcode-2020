package com.romanwuattier.adventofcode2020.days;

import com.romanwuattier.adventofcode2020.common.Day;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 implements Day {
    public static void main(String[] args) {
        new Day8().printParts();
    }

    private final List<Inst> instructions = Arrays.stream(readDay(8).split(System.lineSeparator()))
                                                  .map(l -> {
                                                      String[] words = l.split(" ");
                                                      return new Inst(words[0], Integer.parseInt(words[1]));
                                                  })
                                                  .collect(Collectors.toUnmodifiableList());

    @Override
    public Object part1() {
        return run(instructions).arg;
    }

    @Override
    public Object part2() {
        return IntStream.range(0, instructions.size())
                        .mapToObj(i -> swap(instructions.get(i)).map(swap -> {
                            var swapped = new ArrayList<>(instructions);
                            swapped.set(i, swap);
                            return swapped;
                        }))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(this::run)
                        .filter(res -> res.op.equals("exit"))
                        .findFirst()
                        .map(run -> run.arg)
                        .orElseThrow();
    }

    private Optional<Inst> swap(Inst inst) {
        switch (inst.op) {
            case "acc":
                return Optional.empty(); // Nothing to do. Return empty to avoid useless computation in part 2
            case "nop":
                return Optional.of(new Inst("jmp", inst.arg));
            case "jmp":
                return Optional.of(new Inst("nop", inst.arg));
            default:
                throw new IllegalArgumentException();
        }
    }

    private Inst run(List<Inst> insts) {
        var seen = new HashSet<>();
        var index = 0;
        var acc = 0;
        while (index < insts.size()) {
            if (seen.contains(index)) {
                return new Inst("loop", acc);
            }
            seen.add(index);
            var inst = insts.get(index);
            switch (inst.op) {
                case "nop":
                    index++;
                    break;
                case "acc":
                    acc += inst.arg;
                    index++;
                    break;
                case "jmp":
                    index += inst.arg;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return new Inst("exit", acc);
    }

    @Value
    private static class Inst {
        String op;
        int arg;
    }
}
