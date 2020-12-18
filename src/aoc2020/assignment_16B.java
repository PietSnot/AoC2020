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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_16B {
    
    static TicketModel model = new TicketModel();
    static List<String> input = new ArrayList<>();
    static List<Long> myTicket = new ArrayList<>();
    static List<List<Long>> otherTickets = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        readInput();
        createModel();
        createMyTicket();
        createOtherTickets();
        otherTickets = otherTickets.stream()
            .filter(list -> model.allValidFields(list))
            .collect(toList())
        ;
        model.orderFields(otherTickets);
        long resB = 1;
        for (var e: model.ordered.entrySet()) {
            if (e.getKey().name.startsWith("departure")) {
                var val = myTicket.get(e.getValue());
                resB *= val;
            }
        }
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_16.txt");
        input = Files.readAllLines(path);
    }
    
    private static void createModel() {
        for (int i = 0; i < 20; i++) {
            var t = new TicketField(input.get(i));
            model.addTicketField(t);
        }
    }
    
    private static void createMyTicket() {
        myTicket = Arrays.stream(input.get(22).split(",")).map(Long::valueOf).collect(toList());
    }
    
    private static void createOtherTickets() {
        for (int i = 25; i < input.size(); i++) {
            var list = Arrays.stream(input.get(i).split(",")).map(Long::valueOf).collect(toList());
            otherTickets.add(list);
        }
    }
}

class RangeB {
    long min, max;
    
    RangeB(long n, long x) {
        min = n;
        max = x;
    }
    
    public boolean isInRange(long nr) {
        return min <= nr && nr <= max;
    }
    
    @Override
    public String toString() {
        return String.format("[%d-%d]", min, max);
    }
}

class TicketField {
    String name;
    List<RangeB> ranges = new ArrayList<>();
    
    TicketField(String invoer) {
        var arr = invoer.split(":");
        name = arr[0].trim();
        var arr2 = arr[1].split(" or ");
        for (var s: arr2) {
            var arr3 = s.trim().split("-");
            ranges.add(new RangeB(Long.valueOf(arr3[0]), Long.valueOf(arr3[1])));
        }
    }
    
    public boolean isValidNumber(long nr) {
        return ranges.stream().anyMatch(r -> r.isInRange(nr));
    }
    
    public boolean allValidNumbers(List<Long> list) {
        return list.stream().allMatch(this::isValidNumber);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof TicketField)) return false;
        return name.equals(((TicketField) other).name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s %s", name, ranges.get(0), ranges.get(1));
    }
}

class TicketModel {
    List<TicketField> fields = new ArrayList<>();
    Map<TicketField, Integer> ordered = new HashMap<>();
    
    public void addTicketField(TicketField t) {
        fields.add(t);
    }
    
    public boolean allValidFields(List<Long> list) {
        var bool = list.stream().allMatch(l -> fields.stream().anyMatch(field -> field.isValidNumber(l)));
        return bool;
    }
    
    public void orderFields(List<List<Long>> list) {
        if (list.get(0).size() != fields.size()) 
            throw new RuntimeException("unequal number of fields!!!");
        while (ordered.size() < fields.size()) {
            for (int i = 0; i < fields.size(); i++) {
                var lst = getIthColumn(list, i);
                var tfjes = new ArrayList<TicketField>();
                for (var t: fields) {
                    if (t.allValidNumbers(lst) && !ordered.containsKey(t)) {
                          tfjes.add(t);
                    }
                }  
                if (tfjes.size() == 1) ordered.put(tfjes.get(0), i);
            }
        }
    }
    
    private static List<Long> getIthColumn(List<List<Long>> list, int col) {
        return IntStream.range(0, list.size())
            .mapToObj(i -> list.get(i).get(col))
            .collect(toList())
        ;
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        for(var t: fields) sb.append(t + "\n");
        return sb.toString();
    }
}
