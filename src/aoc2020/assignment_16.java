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
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 */
public class assignment_16 {
    
    public static void main(String... args) throws IOException {
        long resA = solveA();
        System.out.println("resA = " + resA);
    }
    
    private static long solveA() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_16.txt");
        var lines = Files.readAllLines(path);
        var ticket = new Ticket();
        var numbersToInvestigate = new ArrayList<Long>();
        boolean readingRanges = true;
        boolean readingMyTicket = false;
        boolean readingOtherTickets = false;
        for (var line: lines) {
            if (line.isEmpty()) continue;
            if (line.startsWith("your")) {
                readingRanges = false;
                readingOtherTickets = false;
                readingMyTicket = true;
                continue;
            }
            if (line.startsWith("nearby")) {
                readingRanges = false;
                readingOtherTickets = true;
                readingMyTicket = false;
                continue;
            }
            if (readingRanges) {
                var arr = line.split(":");
                var arr2 = arr[1].split(" or ");
                for (var range: arr2) {
                    var arr3 = range.split("-");
                    ticket.addRange(new Range(Integer.parseInt(arr3[0].trim()), Integer.parseInt(arr3[1].trim())));
                }
            }
            else if (readingOtherTickets) {
                var list = Arrays.stream(line.split(","))
                    .map(Long::valueOf)
                    .collect(toList())
                ; 
                numbersToInvestigate.addAll(list);
            }
        }
        long resA = numbersToInvestigate.stream()
            .filter(i -> !ticket.isValidTicketNumber(i))
            .mapToLong(i -> i)
            .sum()
        ;
        return resA;
    }
}

class Range {
    private final long min, max;
    
    Range(long min, long max) {
        this.min = min;
        this.max = max;
    }
    
    public boolean isInRange(long nr) {
        return min <= nr && nr <= max;
    }
    
    @Override
    public String toString() {
        return String.format("%d-%d", min, max);
    }
}

class Ticket {
    List<Range> ranges = new ArrayList<>();
    
    public void addRange(Range range) {
        ranges.add(range);
    }
    
    public boolean isValidTicketNumber(long nr) {
        return ranges.stream().anyMatch(r -> r.isInRange(nr));
    }
    
    @Override
    public String toString() {
        return "Not now please!!!";
    }
}
