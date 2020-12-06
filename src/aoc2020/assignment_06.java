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
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 */
public class assignment_06 {
    
    private static List<String> input;
    private static final List<Group> groups = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        readInput();
        createGroups();
        int resA = solveA();
        System.out.println("resA = " + resA);
        int resB = solveB();
        System.out.println("resB = " + resB);
    }
    
    private static void readInput() throws IOException {
        var path = Paths.get(AoC2020.INVOERMAP, "assignment_06A.txt");
        input = Files.readAllLines(path);
        if (!input.get(input.size() - 1).isEmpty()) input.add("");
    }
    
    private static void createGroups() {
        var group = new Group();
        for (var s: input) {
            if (s.isEmpty()) {
                groups.add(group);
                group = new Group();
                continue;
            }
            group.addYesQuestions(s);
        }
    }
    
    private static int solveA() {
        return groups.stream().mapToInt(Group::getNrOfYesquestionsA).sum();
    }
    
    private static int solveB() {
        return groups.stream().mapToInt(Group::getNrOfYesquestionsB).sum();
    }
}

class Group {
    List<List<Integer>> list = new ArrayList<>();
    
    public void addYesQuestions(String s) {
        list.add(s.chars().boxed().collect(toList()));
    }
    
    public int getNrOfYesquestionsA() {
        var set = new HashSet<Integer>();
        list.forEach(l -> set.addAll(l));
        return set.size();
    }
    
    public int getNrOfYesquestionsB() {
        var k = list.stream().reduce((l,m) -> {l.retainAll(m); return l;});
        return k.get().size();
    }
    
    @Override
    public String toString() {
        return "Not needed";
    }
}
