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
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.LongBinaryOperator;

/**
 *
 * @author Piet
 */
public class assignment_18TweedePoging {
    
    private static Map<String, LongBinaryOperator> map = Map.of("+", (a, b) -> a + b, "*", (a, b) -> a * b);
    
    public static void main(String... args) throws IOException {
        var isTest = false;
        var resA = solveA(isTest);
        System.out.println("resA = " + resA);
        var resB = solveB(isTest);
        System.out.println("resB = " + resB);        
    }
    
    private static long solveA(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_18" + (isTest ? "test" : "") + ".txt");
        var input = Files.readAllLines(path);
        var resA = input.stream().mapToLong(s -> solveExpression(s)).sum();
        return resA;
    }
    
    private static long solveB(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_18" + (isTest ? "test" : "") + ".txt");
        var input = Files.readAllLines(path);
        var resB = input.stream().mapToLong(s -> solveExpressionB(s)).sum();
        return resB;
    }
    
    private static long solveExpression(String s) {
        while (s.contains("(")) s = simplify(s);
        return evaluateSimple(s);
    }
    
    private static String simplify(String s) {
        int end = s.indexOf(")");
        int start = s.substring(0, end).lastIndexOf("(");
        var sub = s.substring(start + 1, end);
        var val = evaluateSimple(sub);
        var sb = new StringBuilder(s);
        sb.replace(start, end + 1, "" + val);
        return sb.toString();
    }
    
    private static long evaluateSimple(String s) {
        if (s.isEmpty()) return 0;
        var operands = new LinkedList<Long>();
        var operators = new LinkedList<String>();
        var scan = new Scanner(s);
        while (scan.hasNext()) {
            if (scan.hasNextLong()) {
                if (!operands.isEmpty()) {
                    var a = scan.nextLong();
                    var op = map.get(operators.removeLast());
                    var b = operands.removeLast();
                    var res = op;
                    operands.add(op.applyAsLong(a, b));
                }
                else operands.add(scan.nextLong());
            }
            else operators.add(scan.next());
        }
        if (!(operands.size() == 1)) throw new RuntimeException("operands size != 1");
        return operands.removeFirst();
    }
    
    private static long solveExpressionB(String s) {
        while (s.contains("(")) s = simplifyB(s);
        return evaluateSimpleB(s);
    }
    
    private static String simplifyB(String s) {
        int end = s.indexOf(")");
        int start = s.substring(0, end).lastIndexOf("(");
        var sub = s.substring(start + 1, end);
        var val = evaluateSimpleB(sub);
        var sb = new StringBuilder(s);
        sb.replace(start, end + 1, "" + val);
        return sb.toString();
    }
    
    private static long evaluateSimpleB(String s) {
        var arr = s.split(" \\* ");
        var val = Arrays.stream(arr)
            .mapToLong(t -> evaluateSimple(t.trim()))
            .reduce((a, b) -> a * b)
            .getAsLong()
        ;
        return val;
    }
}
