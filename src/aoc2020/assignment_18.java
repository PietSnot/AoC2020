/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.LongBinaryOperator;

/**
 *
 * @author Piet
 */
public class assignment_18 {
    
    private static Map<String, LongBinaryOperator> operators = Map.of("+", (a, b) -> a + b, "*", (a, b) -> a * b);
    private static List<String> allowed = List.of("+", "*", "(", ")");
    
    public static void main(String... args) throws IOException {
        var isTest = true;
//        var resA = solveA(isTest);
//        System.out.println("resA = " + resA);
        
        var resB = solveB();
    }
    
    private static List<String> readInput(boolean isTest) throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_18" + (isTest ? "test" : "") + ".txt");
        return Files.readAllLines(path);
    }
    
    private static long solveA(boolean isTest) throws IOException {
        var input = readInput(isTest);
        var resA = input.stream().mapToLong(s -> solveExpression(s)).sum();
        return resA;
    }
    
    private static long solveExpression(String exp) {
        exp = exp.replace("(", "( ").replace(")", " )");
        var operands = new LinkedList<Long>();
        var operatoren = new LinkedList<String>();
        var status = Status.WaitingForFirstOperand;
        var scan = new Scanner(exp);
        while (scan.hasNext()) {
            if (scan.hasNextLong()) {
                if (status.equals(Status.WaitingForFirstOperand)) {
                    var res = scan.nextLong();
                    operands.add(res);
                    status = Status.WaitingForOperator;
                }
                else if (status.equals(Status.WaitingForSecondOperand)) {
                    var a = operands.removeLast();
                    var op = operators.get(operatoren.removeLast());
                    var b = scan.nextInt();
                    var res = op.applyAsLong(a, b);
                    operands.add(res);
                    status = Status.WaitingForOperator;
                }
                else throw new RuntimeException("was expecting an integer");
            }
            else if (scan.hasNext()) {
                var s = scan.next();
                if (!allowed.contains(s)) throw new RuntimeException("unknown operator");
                if (s.equals("+") || s.equals("*")) {
                    operatoren.add(s);
                    status = Status.WaitingForSecondOperand;
                }
                else if (s.equals("(")) status = Status.WaitingForFirstOperand;
                else if (s.equals(")")) {
                    if (operands.size() >= 2) {
                        var a = operands.removeLast();
                        var op = operators.get(operatoren.removeLast());
                        var b = operands.removeLast();
                        var res = op.applyAsLong(a, b);
                        operands.add(res);
                        status = status.WaitingForOperator;
                    }
                    else status = Status.WaitingForOperator;
                }
            }
        }
        if (operands.size() != 1) throw new RuntimeException("stack should have size of 1");
        var resA = operands.get(0);
        return resA;
    }
}

enum Status {
    WaitingForFirstOperand, WaitingForOperator, 
    WaitingForSecondOperand, WaitingForOperatorOrClose
}
