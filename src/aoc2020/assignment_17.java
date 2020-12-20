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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Piet
 */
public class assignment_17 {
    
    public static void main(String... args) throws IOException {
        solveA(false);
//        System.out.println("resA = "+ resA);
    }
    
    private static void solveA(boolean isTest) throws IOException {
        TreeMap<Triple, Boolean> currentGeneration = readInput(isTest);
        for (int round = 1; round <= 6; round++) {
            var nextGeneration = new TreeMap<Triple, Boolean>(Triple.getComparator());
            var nextGenerationTriples = new HashSet<>(currentGeneration.keySet());
            for (var key: currentGeneration.keySet()) nextGenerationTriples.addAll(key.getNeighbors());
            for (var triple: nextGenerationTriples) {
                var neighbors = triple.getNeighbors();
                neighbors.retainAll(currentGeneration.keySet());
                int count = 0;
                for (var neighbor: neighbors) if (currentGeneration.get(neighbor)) count++;
                if (currentGeneration.containsKey(triple)) {
                    if (currentGeneration.get(triple)) {
                        if (count == 2 || count == 3) nextGeneration.put(triple, true);
                        else nextGeneration.put(triple, false);
                    }
                    else if (count == 3) nextGeneration.put(triple, true);
                    else nextGeneration.put(triple, false);
                }
                else nextGeneration.put(triple, count == 3 ? true : false);
            }
            currentGeneration = nextGeneration;
        }
        var resA = currentGeneration.entrySet().stream()
            .filter(e -> e.getValue())
            .count()
        ;
        System.out.println("resA = " + resA);
    }
    
    private static TreeMap<Triple, Boolean> readInput(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, 
                             "assignment_17" + (isTest ? "test" : "") + ".txt"
                             )
        ;
        var lines = Files.readAllLines(path);
        var rows = lines.size();
        var cols = lines.get(0).length();
        var result = new TreeMap<Triple, Boolean>(Triple.getComparator());
        var z = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = lines.get(row).charAt(col);
                result.put(new Triple(row, col, z), c == '#');
            }
        }
        return result;
    }
    
//    private static void print(Map<Triple, Boolean> map) {
//        var len = Math.sqrt(map.size());
//        var sortje = new ArrayList<>(map.keySet()).stream().sorted(Triple.getComparator()).collect(toList());
//        for (int i = 0;)
//    }
}

class Triple {
    final int x, y, z;
    
    Triple(int a, int b, int c) {
        x = a;
        y = b;
        z = c;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public List<Triple> getNeighbors() {
        var list = new ArrayList<Triple>();
        for (int a = x - 1; a <= x + 1; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                for (int c = z - 1; c <= z + 1; c++) {
                    list.add(new Triple(a, b, c));
                }
            }
        }
        list.remove(new Triple(x, y, z));
        return list;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof Triple)) return false;
        var p = (Triple) other;
        return x == p.x && y == p.y && z == p.z;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;
        hash = 83 * hash + this.z;
        return hash;
    }
    
    public static Comparator<Triple> getComparator() {
        return Comparator.comparingInt(Triple::getZ)
            .thenComparingInt(Triple::getX)
            .thenComparingInt(Triple::getY)
        ;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }
}
