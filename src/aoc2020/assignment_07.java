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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 */
public class assignment_07 {
    
    private static List<String> rawInput;
    private static final Map<String, List<String>> mapA = new HashMap<>();
    private static final Map<String, List<Pair<String, Integer>>> mapB = new HashMap<>();
    private static Map<String, Integer> nrOfBags = new HashMap<>();
    
    public static void main(String... args) throws IOException {
        readInput();
        createMapA();
        var s = "shiny gold";
        var resA = solveA(s);
        System.out.println("resA = " + resA);
        createMapB();
        var resB = solveB(s);
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_07A.txt");
        rawInput = Files.readAllLines(path);
    }
    
    private static void createMapA() {
        for (var s: rawInput) {
            var scan = new Scanner(s);
            var colour = scan.next() + " " + scan.next();
            if (s.contains("no")) {
                mapA.computeIfAbsent(colour, k -> new ArrayList<>()).add("");
                scan.close();
                continue;
            }
            var arr = s.split("contain");
            var end = arr[1].split(",");
            for (int i = 0; i < end.length; i++) {
                scan = new Scanner(end[i]);
                var x = scan.next();   // is the number of bags, may be important for B
                mapA.computeIfAbsent(colour, k -> new ArrayList<>()).add(scan.next() + " " + scan.next());
                scan.close();
            }
        }
    }

    private static void createMapB() {
        for (var s: rawInput) {
            var scan = new Scanner(s);
            var colour = scan.next() + " " + scan.next();
            if (s.contains("no")) {
                mapB.computeIfAbsent(colour, k -> new ArrayList<>());
                scan.close();
                continue;
            }
            var arr = s.split("contain");
            var end = arr[1].split(",");
            for (int i = 0; i < end.length; i++) {
                scan = new Scanner(end[i]);
                var x = Integer.parseInt(scan.next());   // is the number of bags, may be important for B
                var p = scan.next() + " " + scan.next();
                mapB.computeIfAbsent(colour, k -> new ArrayList<>()).add(Pair.of(p, x));
                scan.close();
            }
        }
    }

    private static int solveA(String start) {
        var reversedMapA = mapA.entrySet().stream()
            .flatMap(e -> e.getValue().stream().map(s -> Pair.of(e.getKey(), s)))
            .collect(groupingBy(p -> p.b, mapping(p -> p.a, toList())))
        ;
        var seen = new HashSet<String>();
        var queue = new LinkedList<String>();
        int count = 0;
        var currentString = start;
        seen.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            currentString = queue.removeFirst();
            var next = reversedMapA.getOrDefault(currentString, new ArrayList<>());
            next.removeIf(seen::contains);
            count += next.size();
            seen.addAll(next);
            queue.addAll(next);
        }
        return count;
    }
    
    private static int solveB(String s) {
        return nrOfBags(s);
    }
    
    private static int nrOfBags(String s) {
        if (nrOfBags.containsKey(s)) return nrOfBags.get(s);
        if (mapB.get(s).isEmpty()) {
            nrOfBags.put(s, 0);
            return 0;
        }
        int x = mapB.get(s).stream()
            .mapToInt(p -> p.b + p.b * nrOfBags(p.a))
            .sum()
        ;
        nrOfBags.put(s, x);
        return x;
    }
}

class Pair<K, V> {
    final K a;
    final V b;
    
    private Pair(K k, V v) {
        a = k; b = v;
    }
    
    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Pair)) return false;
        var p = (Pair) o;
        return a.equals(p.a) && b.equals(p.b);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.a);
        hash = 67 * hash + Objects.hashCode(this.b);
        return hash;
    }
    
    @Override
    public String toString() {
        return a + ": " + b;
    }
}
