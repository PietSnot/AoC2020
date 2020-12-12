/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import static aoc2020.Direction.E;
import java.awt.Point;
import java.io.IOException;
import static java.lang.Math.abs;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Piet
 */
public class assignment_12 {
    
    private static List<String> input;
    
    public static void main(String... args) throws IOException {
        readInput();
        int resA = solveA();
        System.out.println("resA = " + resA);
        int resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_12.txt");
        input = Files.readAllLines(path);
    }
    
    private static int solveA() {
        var position = new Position();
        var currentDirection = E;
        for (var s: input) {
            if (s.startsWith("L") || s.startsWith("R")) currentDirection = currentDirection.rotate(s);
            else if (s.startsWith("F")) position.move(s, currentDirection);
            else position.move(s, Direction.find(s.substring(0, 1)));
        }
        return position.getDistance();
    }
    
    private static int solveB() {
        var ship = new Position();
        var deltaWPx = 10;
        var deltaWPy = 1;
        var wp = new Position(ship.x + deltaWPx, ship.y + deltaWPy);
        for (var s: input) {
            var c = s.substring(0, 1);
            int dist = Integer.parseInt(s.substring(1));
            if (c.equals("L") || c.equals("R")) {
                for (int i = 1; i <= dist / 90; i++) wp = ship.rotateAround(wp, c);
                deltaWPx = wp.x - ship.x;
                deltaWPy = wp.y - ship.y;
                continue;
            }
            if (c.equals("F")) {
                ship = new Position(ship.x + dist * deltaWPx, ship.y + dist * deltaWPy);
                wp = new Position(ship.x + deltaWPx, ship.y + deltaWPy);
                continue;
            }
            // moving the waypoint
            wp.move(s, Direction.find(s.substring(0, 1)));
            deltaWPx = wp.x - ship.x;
            deltaWPy = wp.y - ship.y;
        }
        return ship.getDistance();
    }
}

enum Direction {
    N(0, 1), E(1, 0), S(0, -1), W(-1, 0);
    
    private int dx, dy;
    private static Map<Direction, Direction> left, right;
    private static Map<String, Direction> find;
    static {
        left = Map.of(N, W, W, S, S, E, E, N);
        right = Map.of(N, E, E, S, S, W, W, N);
        find = Map.of("N", N, "E", E, "S", S, "W", W);
    }
    
    Direction(int x, int y) {
        dx = x;
        dy = y;
    }
    
    public Direction rotate(String s) {
        var multOf90 = Integer.parseInt(s.substring(1)) / 90;
        var map = s.startsWith("L") ? left : right;
        var result = this;
        for (int i = 1; i <= multOf90; i++) result = map.get(result);
        return result;
    }
    
    public Point getDeltas() {
        return new Point(dx, dy);
    }
    
    public static Direction find(String s) {
        return find.get(s);
    }
}

class Position {
    private static List<String> possibilities = List.of("N", "E", "S", "W", "F");
    int x, y;
    Position(int a, int b) {
        x = a;
        y = b;
    }
    Position() {
        this(0, 0);
    }
    
    public void move(String s, Direction d) {
        if (!possibilities.contains(s.substring(0, 1))) 
            throw new RuntimeException("unknown command: " + s);
        var dist = Integer.parseInt(s.substring(1));
        var deltas = d.getDeltas();
        this.x += deltas.x * dist;
        this.y += deltas.y * dist;
    }
    
    public Position rotateAround(Position p, String s) {
        var px = p.x - x;
        var py = p.y - y;
        var newpx = s.startsWith("L") ? -py : py;
        var newpy = s.startsWith("R") ? -px : px;
        return new Position(newpx + x, newpy + y);
    }
    
    public int getDistance() {
        return abs(x) + abs(y);
    }
}
