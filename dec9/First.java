package olymp;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

public class First {
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);
        List<List<Integer>> history = new ArrayList<>();
        while (sc.hasNextLine()) {
            String histLine = sc.nextLine();
            history.add(
                Arrays.asList(histLine.split(" ")).stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())
            );
        }
        System.out.println(history.stream().mapToInt(h -> getPrediction(h)).sum());
    }

    static int getPrediction(List<Integer> hist) {
        boolean hasNonZeros = true;
        List<Integer> diff = new ArrayList<>(hist);
        List<List<Integer>> allDiffs = new ArrayList<>();
        while (hasNonZeros) {
            allDiffs.add(diff);
            diff = getDiffs(diff);
            hasNonZeros = diff.stream().anyMatch(x -> x != 0);
        }
        return allDiffs.stream().mapToInt(d -> getLast(d)).sum();
    }

    static List<Integer> getDiffs(List<Integer> h) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 1; i < h.size(); i++) {
            diff.add(h.get(i) - h.get(i - 1));
        }
        return diff;
    }

    static Integer getLast(List<Integer> a) {
        return a.get(a.size() - 1);
    }


}
