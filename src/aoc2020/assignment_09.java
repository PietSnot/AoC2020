/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_09 {
    
    private static List<Long> input;
    
    public static void main(String... args) throws IOException {
        readInput();
        int preamble = 25;
        long resA = solveA(preamble);
        System.out.println("resA = " + resA);
        long resB = solveB(resA);
        System.out.println("resB = " + resB);  
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_09A.txt");
        input = Files.lines(path).map(Long::valueOf).collect(toList());
        // check for duplicates that are within 25 places from each other
        var map = new HashMap<Long, List<Integer>>();
        IntStream.range(0, input.size())
            .forEach(i -> map.computeIfAbsent(input.get(i), k -> new ArrayList<>()).add(i))
        ;
        var dups = map.entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .filter(e -> Math.abs(e.getValue().get(0) - e.getValue().get(1)) < 25)
            .collect(toMap(e -> e.getKey(), e -> e.getValue()))
        ;
        System.out.println(dups);
        // dups is empty so that is fine
    }
    
    private static long solveA(int preamble) {
        var set = new HashSet<>(input.subList(0, preamble));
        int oldIndex = 0, currentIndex = preamble;
        for (int index = preamble; index < input.size(); index++) {
            if (!isSumOf(set, input.get(index))) return input.get(index);
            set.remove(input.get(oldIndex));
            set.add(input.get(currentIndex));
            oldIndex++;
            currentIndex++;
        }
        return Long.MIN_VALUE;
    }
    
    private static long solveB(long resA) {
        long currentSum = 0;
        var map = new HashMap<Integer, Long>();
        map.put(0, 0L);
        
        for (int i = 1; i <= input.size(); i++) {
            currentSum += input.get(i - 1);
            map.put(i, currentSum);
        }
        for (int diff = 2; diff < input.size(); diff++) {
            for (int current = diff; current < input.size(); current++) {
                if (map.get(current) - map.get(current - diff) == resA) {
                    int firstIndex = current - diff;
                    int secondIndex = current;
                    return findMinAndMax(firstIndex, secondIndex);
                }
            }
        } 
        return Long.MIN_VALUE;
    }
    
    private static long findMinAndMax(int firstIndex, int secondIndex) {
        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        for (int i = firstIndex; i < secondIndex; i++) {
            var number = input.get(i);
            if (number < min) min = number;
            if (number > max) max = number;
        }
        return min + max;
    }
    
    private static boolean isSumOf(Set<Long> set, long l) {
        for (var t: set) {
            if (set.contains(l - t) && t != l - t) return true;
        }
        return false;
    }
}
