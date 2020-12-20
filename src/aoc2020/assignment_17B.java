/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 *75 and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Piet
 */
public class assignment_17B {
    public static void main(String... args) throws IOException {
        var resB = solveB(false);
        System.out.println("resB = " + resB);
    }
    
    private static long solveB(boolean isTest) throws IOException {
        var currentGeneration = readInput(isTest);
        for (int round = 1; round <= 6; round++) {
            var nextGeneration = new HashMap<Quad, Boolean>();
            var nextQuadsToInvestigate = new HashSet<>(currentGeneration.keySet());
            for (var key: currentGeneration.keySet()) nextQuadsToInvestigate.addAll(key.getNeighbors());
            for (var key: nextQuadsToInvestigate) {
                var neighbors = key.getNeighbors();
                neighbors.retainAll(currentGeneration.keySet());
                int count = 0;
                for (var k: neighbors) if (currentGeneration.get(k)) count++;
                if (currentGeneration.containsKey(key)) {
                    if (currentGeneration.get(key)) {
                        if (count == 2 || count == 3) nextGeneration.put(key, true);
                        else nextGeneration.put(key, false);
                    }
                    else if (count == 3) nextGeneration.put(key, true);
                    else nextGeneration.put(key, false);
                }
                else if (count == 3) nextGeneration.put(key, true);
                else nextGeneration.put(key, false);
            }
            currentGeneration = nextGeneration;
        }
        var resB = currentGeneration.entrySet().stream()
            .filter(e -> e.getValue())
            .count()
        ;
        System.out.println("resB = " + resB);
        return resB;
    }
    
    private static Map<Quad, Boolean> readInput(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, 
                             "assignment_17" + (isTest ? "test" : "") + ".txt"
                             )
        ;
        var lines = Files.readAllLines(path);
        var rows = lines.size();
        var cols = lines.get(0).length();
        var result = new HashMap<Quad, Boolean>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = lines.get(row).charAt(col);
                result.put(new Quad(row, col), c == '#');
            }
        }
        return result;
    }
}

class Quad {
    final int x, y, z, w;
    
    Quad(int a, int b, int c, int d) {
        x = a;
        y = b;
        z = c;
        w = d;
    }
    
    Quad(int x, int y) {
        this(x, y, 0, 0);
    }
    
    public List<Quad> getNeighbors() {
        var result = new ArrayList<Quad>();
        for (int a = x - 1; a <= x + 1; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                for (int c = z - 1; c <= z + 1; c++) {
                    for (int d = w - 1; d <= w + 1; d++) {
                        result.add(new Quad(a, b, c, d));
                    }
                }
            }
        }
        result.remove(new Quad(x, y, z, w));
        return result;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Quad)) return false;
        var p = (Quad) other;
        return x == p.x && y == p.y && z == p.z && w == p.w;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        hash = 43 * hash + this.z;
        hash = 43 * hash + this.w;
        return hash;
    }
}