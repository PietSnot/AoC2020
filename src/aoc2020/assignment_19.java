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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_19 {
    
    private static final Map<Integer, List<String>> rules = new HashMap<>();
    private static final Map<Integer, List<List<Integer>>> rawRules = new HashMap<>();
    private static List<String> rawInput = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        var isTest = false;
        readInput(isTest);
//        var s = "babbbbaabbbbbabbbbbbaabaaabaaa";
//        System.out.println("s sat rules 0 ? " + satisfiesRule0(s));
//        var map = rules.entrySet().stream()
//            .filter(e -> e.getKey() == 31)
//            .flatMap(e -> e.getValue().stream())
//            .collect(groupingBy(s -> s.length(), counting()))
//        ;
//        System.out.println(map);
        var resA = solveA(isTest);
        System.out.println("resA = " + resA);
        var resB = solveB(isTest);
        System.out.println("resB = " + resB);
    }
    
    private static long solveA(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_19Data" + (isTest ? "test" : "") + ".txt");
        var data = Files.readAllLines(path);
        var resA = data.stream().filter(rules.get(0)::contains).count();
        return resA;
    }
    
    private static long solveB(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_19Data" + (isTest ? "test" : "") + ".txt");
        var data = Files.readAllLines(path);
        var resB = data.stream().filter(s -> satisfiesRule0(s)).count();
        return resB;
    }
    
    private static boolean satisfiesRule0(String s) {
        var len = rules.get(42).get(0).length();
        if (s.length() % len != 0) return false;
        var parts = IntStream.iterate(0, i -> i < s.length(), i -> i + len)
            .mapToObj(i -> s.substring(i, i + len))
            .collect(toList())
        ;
        var rule42 = parts.stream()
            .takeWhile(str -> rules.get(42).contains(str))
            .count()
        ;
        var rule31 = IntStream.range(0, parts.size())
            .takeWhile(i -> rules.get(31).contains(parts.get(parts.size() - 1 - i)))
            .count()
        ;
        return rule42 >= (parts.size() / 2 + 1) &&
               (rule31 > 0)                     &&
               (rule42 + rule31 >= parts.size())
        ;
    }
    
    private static void readInput(boolean isTest) throws IOException {
        var string = "assignment_19Rules" + (isTest ? "test" : "") + ".txt";
        var path = Paths.get(AoC2020.INVOERMAP, string);
        rawInput = Files.readAllLines(path);
        processRawInput();
    }
    
    private static void processRawInput() {
        for (var string: rawInput) {
            var t = string.split(":");
            if (t[1].contains("\"")) {
                var key = Integer.parseInt(t[0]);
                var val = new ArrayList<String>();
                var s = t[1].trim();
                val.add(s.substring(1, s.length() - 1));
                rules.put(key, val);
                continue;
            }
            // t[1] of the form 1 2 | 3 4  of 1 2 3
            var scan = new Scanner(t[1]);
            var value = new ArrayList<List<Integer>>();
            var list = new ArrayList<Integer>();
            while (scan.hasNext()) {
                if (scan.hasNextInt()) list.add(scan.nextInt());
                else {
                    // pipe
                    var g = scan.next();
                    if (g.equals("|")) {
                        value.add(list);
                        list = new ArrayList<>();
                    }
                }
            }
            value.add(list);
            rawRules.put(Integer.parseInt(t[0]), value);
        }
        processFinally();
    }
    
    private static void processFinally() {
        while (!rawRules.isEmpty()) {
            var indices = rawRules.keySet().stream()
                .filter((i -> rawRuleIsFullyContainedInRules(i)))
                .collect(toList())
            ;
            for (int i: indices) {
                rules.put(i, fromListListStringToListString(i));
            }
            for (Integer i: indices) {
                rawRules.remove(i);
            }
        }
    }
    
    private static boolean rawRuleIsFullyContainedInRules(int key) {
        var result = rawRules.get(key).stream().allMatch(list -> ListIntegerIsFullyContainedInRules(list));
        return result;
    }
    
    private static boolean ListIntegerIsFullyContainedInRules(List<Integer> list) {
        var result = list.stream().allMatch(rules::containsKey);
        return result;
    }
    
    private static List<String> fromListIntegerToListString(List<Integer> list) {
        var result = new ArrayList<String>();
        result.add("");
        for (int i: list) result = fromListStringAndIntegerToListString(result, i);
        return result;    
    }
    
    private static ArrayList<String> fromListStringAndIntegerToListString(List<String> list, int key) {
        var result = list.stream()
            .flatMap(s -> rules.get(key).stream().map(t -> s + t))
            .collect(toCollection(ArrayList::new))
        ;
        return result;
    }
    
    private static List<String> fromListListStringToListString(int key) {
        var result = new ArrayList<String>();
        for (var list: rawRules.get(key)) {
            result.addAll(fromListIntegerToListString(list));
        }
        return result;
    }
}
