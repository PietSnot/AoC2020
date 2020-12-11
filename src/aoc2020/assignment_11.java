/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Piet
 */
public class assignment_11 {
    
    static char[][] original, copy, temp;
    static int rows, cols;
    
    public static void main(String... args) throws IOException {
        var resA = solveA();
        System.out.println("resA = " + resA);
        var resB = solveB(false);
        System.out.println("resB = " + resB);
    }
    
    private static long solveA() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_11.txt");
        readInput(path);
        boolean changed = true;
        while (changed) {
            changed = nextGenerationA();
        }
        int lives = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (original[row][col] == '#') lives++;
            }
        }
        return lives;
    }
    
    private static boolean nextGenerationA() {
        var changed = false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copy[row][col] = original[row][col];
                if (original[row][col] == '.') continue;
                var points = getNeighborsA(row, col);
                int count = 0;
                for (var p: points) {
                    if (original[p.x][p.y] == '#') count++;
                }
                if (original[row][col] == 'L' && count == 0) {
                    copy[row][col] = '#';
                    changed = true;
                }
                if (original[row][col] == '#' && count >= 4) {
                    copy[row][col] = 'L';
                    changed = true;
                }       
            }
        }
        temp = copy;
        copy = original;
        original = temp;
        return changed;
    }
    
     private static boolean nextGenerationB() {
        var changed = false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copy[row][col] = original[row][col];
                if (original[row][col] == '.') continue;
                var points = getNeighborsB(row, col);
                int count = 0;
                for (var p: points) {
                    if (original[p.x][p.y] == '#') count++;
                }
                if (original[row][col] == 'L' && count == 0) {
                    copy[row][col] = '#';
                    changed = true;
                }
                if (original[row][col] == '#' && count >= 5) {
                    copy[row][col] = 'L';
                    changed = true;
                }       
            }
        }
        temp = copy;
        copy = original;
        original = temp;
        return changed;
    }

    private static long solveB(boolean test) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_11.txt");
        readInput(path);
        boolean changed = true;
        while (changed) {
            changed = nextGenerationB();
            if (test) print(original);
        }
        int lives = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (original[row][col] == '#') lives++;
            }
        }
        return lives;   
    }
    
    private static List<Point> getNeighborsA(int row, int col) {
        var list = new ArrayList<Point>();
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;
                if (row + r < 0 || row + r >= rows || col + c < 0 || col + c >= cols) continue;
                list.add(new Point(row + r, col + c));
            }
        }
        return list;
    }
    
    private static List<Point> getNeighborsB(int row, int col) {
        var list = new ArrayList<Point>();
        findFirstPoint(row, col, 0, -1, list);
        findFirstPoint(row, col, -1, -1, list);
        findFirstPoint(row, col, -1, 0, list);
        findFirstPoint(row, col, -1, 1, list);
        findFirstPoint(row, col, 0, 1, list);
        findFirstPoint(row, col, 1, 1, list);
        findFirstPoint(row, col, 1, 0, list);
        findFirstPoint(row, col, 1, -1, list);
        return list;
    }
    
    private static void findFirstPoint(int row, int col, int dx, int dy, List<Point> list) {
        int deltadx = dx < 0 ? -1 : dx == 0 ? 0 : 1;
        int deltady = dy < 0 ? -1 : dy == 0 ? 0 : 1;
        
        while (row + dx >= 0 && row + dx < rows && col + dy >= 0 && col + dy < cols) {
            if (original[row + dx][col +  dy] != '.') {
                list.add(new Point(row + dx, col + dy));
                return;
            }
            dx += deltadx;
            dy += deltady;
        }
    }
    
    private static void readInput(Path path) throws IOException {
        var input = Files.readAllLines(path);
        rows = input.size();
        cols = input.get(0).length();
        original = new char[rows][cols];
        copy = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            var string = input.get(row);
            for (int col = 0; col < cols; col++) {
                original[row][col] = string.charAt(col);
            }
        }
    }
    
    private static void print(char[][] x) {
        for (var arr: x) {
            for (char c: arr) System.out.print(c);
            System.out.println();
        }
        System.out.println("----------------------------");
    }
}
