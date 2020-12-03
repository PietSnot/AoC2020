/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */
public class assignment_03 {
    static String[] invoer;
    static int width, height;
    
    public static void main(String... args) throws IOException {
        leesInvoer();
        var resA = solveA(1, 3);
        var resA2 = solveA2(1, 3);
        System.out.println(resA);
        System.out.println(resA2);
        var points = List.of(new Point(1, 1), 
                             new Point(1, 3), 
                             new Point(1, 5), 
                             new Point(1, 7), 
                             new Point(2, 1))
        ;
        var resB = points.stream()
            .mapToLong(p -> solveA(p.x, p.y))
            .reduce(1, (a, b) -> a * b)
        ;
        System.out.println("resB = " + resB);
        long resB2 = 1;
        for (var p: points) {
            long temp = solveA2(p.x, p.y);
            resB2 *= temp;
        }
        System.out.println("resB = " + resB2);
    }
    
    private static void leesInvoer() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_03A.txt");
        invoer = Files.lines(path).toArray(String[]::new);
        System.out.println("invoer telt " + invoer.length + " regels");
        width = invoer[0].length();
        height = invoer.length;
        System.out.format("%d, %d%n", height, width);
    }
    
    private static long solveA(int down, int right) {
        return Stream.iterate(new Point(), p -> p.x < height, p -> nextPoint(p, down, right))
            .filter(p -> invoer[p.x].charAt(p.y) == '#')
            .count()
        ;
    }
    
    private static int solveA2(int down, int right) {
        int res = 0;
        var p = new Point();
        while (p.x < height) {
            if (invoer[p.x].charAt(p.y) == '#') res++;
            p = nextPoint(p, down, right);
        }
        return res;
    }
    
    private static Point nextPoint(Point p, int down, int right) {
        int x = p.x + down;
        int y = (p.y + right) % width;
        return new Point(x, y);
    }
}
