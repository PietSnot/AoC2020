/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.util.HashMap;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 */
public class assignment_15 {
    public static void main(String... args) {
        var input = List.of(6,19,0,5,7,13,1).stream().mapToLong(i -> i).boxed().collect(toList());
        solveA(input);
    }
    
    private static void solveA(List<Long> input) {
//        var map = IntStream.rangeClosed(1, input.size())
//            .collect(toMap(i -> input.get(i), i -> Pair15.of((long) i, 1L)))
//        ;
        long roundsToPlay = 30_000_000L;
        var map = new HashMap<Long, Pair15>();
        for (int i = 0; i < input.size(); i++) map.put(input.get(i), Pair15.of(i + 1L, 1L));
        var lastSpoken = input.get(input.size() - 1);
        long round = input.size() + 1L;
        while (round <= roundsToPlay) {
            if (map.get(lastSpoken).timesSpoken == 1) lastSpoken = 0L;
            else {
                var p = map.get(lastSpoken);
                lastSpoken = p.lastRoundSpoken - p.previousRoundSpoken;
            }
            if (map.containsKey(lastSpoken)) map.get(lastSpoken).update(round);
            else map.put(lastSpoken, Pair15.of(round, 1L));
            round++;
        }
        System.out.println("after 2020 rounds, lastSpoken = " + lastSpoken);
    }
}

class Pair15 {
    long lastRoundSpoken;
    long previousRoundSpoken;
    long timesSpoken;
    
    private Pair15(long lastRoundSpoken, long ts) {
        this.lastRoundSpoken = lastRoundSpoken;
        this.timesSpoken = ts;
    }
    
    static Pair15 of(long lastRoundSpoken, long ts) {
        return new Pair15(lastRoundSpoken, ts);
    }
    
    public void update(long round) {
        previousRoundSpoken = lastRoundSpoken;
        lastRoundSpoken = round;
        timesSpoken++;
    }
    
    @Override
    public String toString() {
        return String.format("lrs: %d, prs: %d, tS: %d", lastRoundSpoken, previousRoundSpoken, timesSpoken);
    }
}
