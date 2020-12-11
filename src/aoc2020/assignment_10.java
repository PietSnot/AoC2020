/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_10 {
    
    static List<Long> input;
    
    public static void main(String... args) throws IOException {
        readInput();
        var resA = solveA();
        System.out.println("resA = " +  resA);
        var resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_10.txt");
        input = Files.lines(path).map(Long::valueOf).sorted().collect(toList());
        input.add(0, 0L);
        input.add(input.get(input.size() - 1) + 3);
        System.out.println("input has " + input.size() + " lines");
    }
    
    private static long solveA() {
        var freqs = IntStream.range(1, input.size())
            .mapToObj(i -> input.get(i) - input.get(i - 1))
            .collect(groupingBy( i -> i, counting()))
        ;
        freqs.keySet().stream().filter(k -> k > 3).forEach(System.out::println);
        System.out.println(freqs);
        return freqs.get(1L) * freqs.get(3L);
    }
    
    private static long solveB() {
        var possibilities = new HashMap<Long, Long>();
        possibilities.put(0L, 1L);
        for (long i: input.subList(1, input.size())) {
            long count = 0;
            for (long j = i - 3; j < i; j++) {
                count += possibilities.getOrDefault(j, 0L);
            }
            possibilities.put(i, count);
        }
        return possibilities.get(input.get(input.size() - 1));
    }
}
