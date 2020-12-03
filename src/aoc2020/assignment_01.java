/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aoc2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Piet
 */
public class assignment_01 {
    public static void main(String... args) {
        var path = Paths.get("D:\\JavaProgs\\AoC2020\\src\\Resources", "assignment_01A.txt");
        int result = ass01A(path);
        System.out.println("result A = " + result);
        var res = ass01B(path);
        System.out.println("result B = " + res);
    }
    
    private static int ass01A(Path path) {
        try (var stream = Files.lines(path)) {
            var freqs = stream.map(Integer::valueOf).collect(groupingBy(i -> i, counting()));
            if (freqs.containsKey(1010)) {
                if (freqs.get(1010) > 1) return 1010 * 1010;
                freqs.remove(1010);
            }
            for (int x: freqs.keySet()) {
                if (freqs.containsKey(2020 - x)) return x * (2020 - x);
            }
            return -1;
        } 
        catch (Exception e) {
            throw new RuntimeException("Can't open inputfile!!!!");
        }
    }
    
    private static long ass01B(Path path) {
        try (var stream = Files.lines(path)) {
            var set = stream.map(Long::valueOf).collect(toSet());
            var arr = set.stream().mapToLong(i -> i).sorted().toArray();
            beginloop:
            for (int i = 0; i < arr.length - 2; i++) {
                for (int j = i + 1; j < arr.length - 1; j++) {
                    if (arr[i] + arr[j] >= 2020L - arr[j]) continue beginloop;
                    var x = 2020L - arr[i] - arr[j];
                    if (set.contains(x)) return arr[i] * arr[j] * x;
                }
            }
            return -1;
        }
        catch (Exception e) {
            throw new RuntimeException("Can't open inputfile!!!!");
        }
    }
}
