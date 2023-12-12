import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

public class Second {
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        int sum = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String springs = line.split(" ")[0];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(springs);
            }
            springs = sb.toString();
            List<Integer> conditions = 
                    Arrays.asList(line.split(" ")[1].split(",")).stream()
                    .map(Integer::parseInt)
                    .toList();
            List<Integer> trueConditions = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                trueConditions.addAll(conditions);
            }

            // List<String> possibleSprings = generatePossibleSprings(springs);
            // sum += possibleSprings.stream().filter(p -> isValid(p, trueConditions)).count();
            sum += countPossibleSprings(springs, springs.length(), trueConditions);
                   
        }
        System.out.println(sum);
    }

    // https://github.com/morgoth1145/advent-of-code/blob/54c79b33cd38f77240d7133bc4458755cefc2ce3/2023/12/solution.py
    private static int countPossibleSprings(String spring, int len, List<Integer> conditions) {
        if (len == 0 || conditions.size() == 0) {
            return spring.chars().allMatch(c -> c == '.' || c == '#') ? 1 : 0;
        }

        int a = conditions.get(0);
        List<Integer> rest = conditions.stream().skip(1).toList();
        int after = rest.stream().mapToInt(x -> x).sum() + rest.size();

        int count = 0;

        for (int before = 0; before < len - after - a + 1; before++) {
            StringBuilder cand = new StringBuilder(new String(new char[before]).replace('\0', '.'));
            cand.append(new String(new char[a]).replace('\0', '#'));
            cand.append(".");
            boolean cond = true;
            for (int i = 0; i < cand.length() && i < spring.length(); i++) {
                cond &= (cand.charAt(i) == spring.charAt(i) || spring.charAt(i) == '?');
            }
            if (cond) {
                String restS = cand.length() < spring.length() ? 
                        spring.substring(cand.length(), spring.length() - 1)
                        : "";
                count += countPossibleSprings(restS, len - a - before - 1, rest);
            }
        }
        return count;
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
