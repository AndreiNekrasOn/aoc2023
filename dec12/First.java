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

        int sum = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String springs = line.split(" ")[0];
            List<Integer> conditions = 
                    Arrays.asList(line.split(" ")[1].split(",")).stream()
                    .map(Integer::parseInt)
                    .toList();

            List<String> possibleSprings = generatePossibleSprings(springs);
            sum += possibleSprings.stream().filter(p -> isValid(p, conditions)).count();
                   
        }
        System.out.println(sum);
    }

    private static boolean isValid(String p, List<Integer> conditions) {
        List<String> splited = Arrays.asList(p.split("\\.")).stream()
                .filter(l -> l.length() != 0).toList();
        if (splited.size() != conditions.size()) {
            return false;
        }
        boolean res = true;
        for (int i = 0; i < splited.size(); i++) {
            res &= splited.get(i).length() == conditions.get(i);
        }
        return res;
    }

    private static List<String> generatePossibleSprings(String spring) {
        List<String> res = new ArrayList<>();
        long questions = spring.chars().filter(c -> c == '?').count();
        int possibilities = (int) Math.pow(2, questions);
        for (int i = 0; i < possibilities; i++) {
            res.add(generateSingleSpring(spring, i));
        }
        return res;
    }

    private static String generateSingleSpring(String spring, int p) {
        int qmCnt = 0;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < spring.length(); j++) {
            if (spring.charAt(j) == '?') {
                sb.append(checkOne(p, qmCnt) ? '#': '.');
                qmCnt++;
            } else {
                sb.append(spring.charAt(j));
            }
        }
        return sb.toString();
    }

    private static boolean checkOne(int number, int pos) {
        return ((number >> pos) & 1) == 1;
    }
}
