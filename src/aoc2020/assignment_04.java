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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Piet
 */
public class assignment_04 {
    
    static Path path = Paths.get(AoC2020.INVOERMAP, "assignment_04A.txt");
    static List<Passport> passports = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        leesInvoer();
//        passports.forEach(System.out::println);
//        passports.stream().filter(Passport::isValid).forEach(System.out::println);
        var resA = solveA();
        System.out.println("resA: " + resA);
        var resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static long solveA() {
        return passports.stream().filter(Passport::isValidA).count();
    }
    
    private static long solveB() {
        return passports.stream().filter(Passport::isValidB).count();
    }
    
    private static void leesInvoer() throws IOException {
        var invoer = Files.readAllLines(path);
        var pw = new Passport();
        for (var line: invoer) {
            if (line.isEmpty()) {
                passports.add(pw);
                pw = new Passport();
                continue;
            }
            var s = line.split(" ");
            for (var t: s) pw.process(t);
        }
        passports.add(pw);
    }
}

class Passport {
    
    static Set<String> required = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    Map<String, String> fields = new HashMap<>();
    
    public void process(String input) {
        var arr = input.split(":");
        fields.put(arr[0], arr[1]);
    }
    
    public boolean isValidA() {
        var presentFields = fields.keySet();
        return required.stream().allMatch(presentFields::contains);
    }
    
    public boolean isValidB() {
        if (!isValidA()) return false;
        // byr
        int bj = Integer.parseInt(fields.get("byr"));
        if (bj < 1920 || bj > 2002) return false;
        // iyr
        int ij = Integer.parseInt(fields.get("iyr"));
        if (ij < 2010 || ij > 2020) return false;
        // eyr
        int ej = Integer.parseInt(fields.get("eyr"));
        if (ej < 2020 || ej > 2030) return false;
        // hgt
        var s = fields.get("hgt");
        if (!(s.endsWith("in") || s.endsWith("cm"))) return false;
        int l = Integer.parseInt(s.substring(0, s.length() - 2));
        String msr = s.substring(s.length() - 2, s.length());
        if (msr.equals("in") && (l < 59 || l > 76)) return false;
        if (msr.equals("cm") && (l < 150 || l > 193)) return false;
        // hcl
        var pattern = "#[0-9a-f]{6}";
        if (!(fields.get("hcl").matches(pattern))) return false;
        // ecl
        var set = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
        if (!set.contains(fields.get("ecl"))) return false;
        // pid
        pattern = "[0-9]{9}";
        if (!(fields.get("pid").matches(pattern))) return false;
        return true;
    }
    
    @Override
    public String toString() {
        return fields.toString();
    }
}
