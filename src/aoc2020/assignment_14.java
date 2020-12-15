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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Piet
 */
public class assignment_14 {
    
    static Map<String, String> memory = new HashMap<>();
    static String mask;
    
    public static void main(String... args) throws IOException {
//        solveA();
        solveB();
    }
    
    private static void solveA() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_14.txt");
        var lines = Files.readAllLines(path);
        for (var line: lines) {
            if (line.startsWith("mask")) processMask(line);
            else if (line.startsWith("mem")) processData(line);
        }
        var resA = memory.values().stream().mapToLong(s -> Long.parseLong(s, 2)).sum();
        System.out.println("resA = " + resA);
    }
    
    private static void solveB() throws IOException {
        memory.clear();
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_14.txt");
        var lines = Files.readAllLines(path);
        for (var line: lines) {
            if (line.startsWith("mask")) processMask(line);
            else if (line.startsWith("mem")) processDataB(line);
        }
        var resB = memory.values().stream().mapToLong(s -> Long.parseLong(s)).sum();
        System.out.println("resB = " + resB);
    }
    
    private static void processMask(String s) {
        var temp = s.split(" = ");
        mask = temp[1].trim();
    }
    
    private static void processData(String s) {
        var start = s.indexOf("[") + 1;
        var end = s.indexOf("]");
        var mem = s.substring(start, end);
        start = s.indexOf("=") + 1;
        var temp = s.substring(start).trim();
        var value = convertA(temp);
        memory.put(mem, value);
    }
    
    private static void processDataB(String s) {
        var start = s.indexOf("[") + 1;
        var end = s.indexOf("]");
        var mem = s.substring(start, end);
        start = s.indexOf("=") + 1;
        var value = s.substring(start).trim();
        List<String> addresses = convertB(mem);
        addresses.stream().forEach(t -> memory.put(t, value));
    }
    
    private static List<String> convertB(String address) {
        var string = convertAddressLeaveX(address);
        var list2 = convertAddressB2(string);
        return list2;
    }
    
    private static String pad(String s, int length) {
        while (s.length() < length) s = "0" + s;
        return s;
    }
    
    private static String convertA(String s) {
        s = pad(Integer.toBinaryString(Integer.parseInt(s)), mask.length());
        var result = "";
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '0' || c == '1') result += c;
            else result += s.charAt(i);
        }
        return result;
    }
    
    private static String convertAddressLeaveX(String address) {
        String result = "";
        var temp = Integer.toBinaryString(Integer.parseInt(address));
        address = pad(temp, mask.length());
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '0') result += address.charAt(i);
            else if (c == '1') result += '1';
            else result += "X";
        }
        return result;
    }
    
    private static List<String> convertAddressB2(String x) {
        var queue = new LinkedList<String>();
        var result = new ArrayList<String>();
        queue.add(x);
        while (!queue.isEmpty()) {
            var s = queue.removeFirst();
            if (!s.contains("X")) result.add(s);
            else queue.addAll(removeFirstX(s));
        }
        return result;
    }
    
    private static List<String> removeFirstX(String s) {
        var result = new ArrayList<String>();
        var start = s.indexOf("X");
        if (start == -1) return result;
        var sb1 = new StringBuilder(s);
        var sb2 = new StringBuilder(s);
        sb1.setCharAt(start, '0');
        sb2.setCharAt(start, '1');
        result.add(sb1.toString());
        result.add(sb2.toString());
        return result;
    }
}
