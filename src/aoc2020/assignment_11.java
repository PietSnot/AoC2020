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
        boolean isA = true;
        var resA = solve(isA);
        System.out.println("resA = " + resA);
        var resB = solve(!isA);
        System.out.println("resB = " + resB);
    }
    
    private static long solve(boolean isPartA) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_11.txt");
        readInput(path);
        while (nextGeneration(isPartA));
        return nrOfLives();
    }
    
    private static boolean nextGeneration(boolean A) {
        var changed = false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copy[row][col] = original[row][col];
                if (original[row][col] == '.') continue;
                var points = A ? getNeighborsA(row, col) : getNeighborsB(row, col);
                int count = 0;
                for (var p: points) {
                    if (original[p.x][p.y] == '#') count++;
                }
                if (original[row][col] == 'L' && count == 0) {
                    copy[row][col] = '#';
                    changed = true;
                }
                if (original[row][col] == '#' && count >= (A ? 4 : 5)) {
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
    
    private static int nrOfLives() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (original[r][c] == '#') count++;
            }
        }
        return count;
    }
}
