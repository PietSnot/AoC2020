/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Piet
 */
public class assignment_05 {
    
    private static final Map<Character, String> map = Map.of('B', "1", 'F', "0", 'L', "0", 'R', "1");
    private static final Path path = Paths.get(AoC2020.INVOERMAP, "assignment_05A.txt");
    private static Set<String> invoer;
    
    public static void main(String... args) throws IOException {
        leesInvoer();
        long resA = solveA();
        System.out.println("resA = " + resA);
        long resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static long solveA() {
        return invoer.stream().mapToLong(assignment_05::convertToID).max().getAsLong();
    }
    
    private static long solveB() {
        var presentNumbers = invoer.stream()
            .mapToLong(s -> convertToID(s))
            .sorted()
            .toArray()
        ;
        for (int i = 1; i < presentNumbers.length; i++) {
            if (presentNumbers[i] - presentNumbers[i - 1] == 2) {
                return presentNumbers[i] - 1;
            }
        }
        return -1;
    }
    
    private static void leesInvoer() throws IOException {
        invoer = Files.lines(path).collect(toSet());
    }
    
    private static long convertToID(String string) {
        var s = string.chars().mapToObj(i -> map.get((char) i)).collect(joining());
        return Long.parseLong(s, 2);
    }
    
    private static String convertToSeat(long x) {
        var s = Long.toBinaryString(x);
        var seat = s.replaceAll("0", "L").replaceAll("1", "R");
        while (seat.length() < 3) seat = "L" + seat;
        return seat;
    }
    private static String convertToRow(long x) {
        var s = Long.toBinaryString(x);
        var seat = s.replaceAll("0", "F").replaceAll("1", "B");
        while (seat.length() < 7) seat = "F" + seat;
        return seat;
    }
    
    private static String convertToString(long x) {
        var s = convertToRow(x / 8);
        var t = convertToSeat(x % 8);
        return s + t;
    }
}
