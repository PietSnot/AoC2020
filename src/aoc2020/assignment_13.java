/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_13 {
    
    private static List<String> input;
    private static int departure;
    
    public static void main(String... args) throws IOException {
        readInput();
        int resA = solveA();
        System.out.println("resA = " + resA);
        var resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_13.txt");
        var invoer = Files.readAllLines(path);
        departure = Integer.parseInt(invoer.get(0));
        input = Arrays.stream(invoer.get(1).split(",")).collect(toList());
    }
    
    private static int solveA() {
        var ids = input.stream()
            .filter(s -> !s.equals("x"))
            .map(Integer::valueOf)
            .collect(toList())
        ; 
        int minWaitingtime = Integer.MAX_VALUE;
        int product = 0;
        for (int id: ids) {
            if (departure % id == 0) return 0;
            int waitingtime = (departure / id + 1) * id - departure;
            if (waitingtime < minWaitingtime) {
                minWaitingtime = waitingtime;
                product = minWaitingtime * id;
            }
        }
        return product;
    }
    
    private static long solveB() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_13.txt");
        var temp = Files.readAllLines(path).get(1);
        String[] list = temp.split(",");
        var map = IntStream.range(0, list.length)
            .filter(i -> !list[i].equals("x"))
            .boxed()
            .collect(toMap(i -> Long.parseLong(list[i]), i -> -i))
        ;
        var total = map.keySet().stream().reduce((a, b) -> a * b).get();
        var resB = map.keySet().stream()
            .mapToLong(i -> getSecondProduct(i, total / i) * map.get(i))
            .sum()
        ;
        while (resB > total) resB -= total;
        while (resB < 0) resB += total;
        return resB;
    }    
    
    private static long getSecondProduct(long first, long second) {
        for (int i = 1; ; i++) {
            if ((i * second - 1) % first == 0) return i * second;
        }
//        throw new RuntimeException("unable to solve the equation");
    }
}
