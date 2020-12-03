/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Piet
 */
public class assignment_02 {
    
    static List<String> input;
    
    public static void main(String... args) throws IOException {
        getInput();
        var resA = solve2A();
        System.out.println(resA);
        var resB = solve2B();
        System.out.println(resB);
    }
    
    private static void getInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_02A.txt");
        input = Files.readAllLines(path);
    }
    
    private static long solve2A() {
        return input.stream().filter(assignment_02::isValidPasswordA).count();
    }
    
    private static long solve2B() {
        return input.stream().filter(assignment_02::isValidPasswordB).count();
    }
    
    private static boolean isValidPasswordA(String s) {
        try (var scan = new Scanner(s.replaceAll("-", " ").replaceAll(":", ""))) {
            int min = scan.nextInt();
            int max = scan.nextInt();
            char c = scan.next().charAt(0);
            var pw = scan.next().toCharArray();
            int count = 0;
            for (char q: pw) if (q == c) count++;
            return min <= count && count <= max;
        }
    }
    
    private static boolean isValidPasswordB(String s) {
        try (var scan = new Scanner(s.replaceAll("-", " ").replaceAll(":", ""))) {
            int min = scan.nextInt() - 1;
            int max = scan.nextInt() - 1;
            char c = scan.next().charAt(0);
            var pw = scan.next();
            return (pw.charAt(min) == c) ^ (pw.charAt(max) == c);
        }
    }
}
