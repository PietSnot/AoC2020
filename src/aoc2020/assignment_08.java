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
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_08 {
    
    static int PC;
    static int accumulator;
    static List<Instruction> program;
    
    public static void main(String... args) throws IOException {
        readInput();
        int resA = solveA();
        System.out.println("resA = " + resA);
        int resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_08A.txt");
        program = Files.lines(path).map(Instruction::new).collect(toList());
    }
    
    private static int solveA() {
        var pcSeen = new HashSet<Integer>();
        PC = 0;
        accumulator = 0;
        while (!pcSeen.contains(PC)) {
            pcSeen.add(PC);
            program.get(PC).execute();
        }
        System.out.println("PC at end: " + PC);
        return accumulator;
    }
    
    private static int solveB() {
        var toChange = IntStream.range(0, program.size())
            .filter(i -> program.get(i).command.equals("nop") || program.get(i).command.equals("jmp"))
            .boxed()
            .collect(toList())
        ;
        for (int i: toChange) {
            var code = changeCode(i);
            if (executesSuccesfully(code)) return accumulator;
        }
        return -1;
    }
    
    private static List<Instruction> changeCode(int i) {
        var code = new ArrayList<>(program);
        var instruction = program.get(i);
        var newInstruction = instruction.command.equals("nop") ? 
            new Instruction("jmp", instruction.arg) :
            new Instruction("nop", instruction.arg)
        ;
        code.set(i, newInstruction);
        return code;
    }
    
    private static boolean executesSuccesfully(List<Instruction> code) {
        var pcSeen = new HashSet<Integer>();
        PC = 0;
        accumulator = 0;
        while(!pcSeen.contains(PC)) {
            pcSeen.add(PC);
            code.get(PC).execute();
            if (PC >= code.size()) return true;
        }
        return false;
    }
    
    private static class Instruction {
        final String command;
        final int arg;
        
        Instruction(String s) {
            var arr = s.split(" ");
            command = arr[0];
            arg = Integer.parseInt(arr[1]);
        }
        
        Instruction(String s, int val) {
            command = s;
            arg = val;
        }
        
        public void execute() {
            switch (command) {
                case "acc": 
                    accumulator += arg;
                    PC += 1;
                    break;
                case "jmp":
                    PC += arg;
                    break;
                case "nop":
                    PC += 1;
                    break;
                default:
                    throw new RuntimeException("unknown instruction: " + command);
            }
        }
        
        @Override
        public String toString() {
            return String.format("%s: %d", command, arg);
        }
    }
}

