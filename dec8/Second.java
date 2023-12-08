import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class Second {

    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        final String instructions = sc.nextLine();
        sc.nextLine(); // skip one
        Map<String, String[]> nodePath = new HashMap<>();

        // scufed parsing, I know
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String node = line.substring(0, 3);
            String left = line.substring(7,10);
            String right = line.substring(12, 15);
            String[] path = {left, right};
            nodePath.put(node, path);
        }

        List<String> currentNodes = new ArrayList<>();
        for (String key : nodePath.keySet()) {
            if (key.charAt(2) == 'A') {
                currentNodes.add(key);
            }
        }


        int[] cycles = new int[currentNodes.size()];
        for (int j = 0; j < currentNodes.size(); j++) {
            String currentNode = currentNodes.get(j);
            int i = 0;
            int totalSteps = 0;
            while (currentNode.charAt(2) != 'Z') {
                String[] directions = nodePath.get(currentNode);
                currentNode = instructions.charAt(i) == 'R' ? directions[1] : directions[0];
                i++;
                totalSteps++;
                if (i == instructions.length()) {
                    i = 0;
                }
            }
            cycles[j] = totalSteps;
        }
        System.out.println(lcm(cycles));
    }

    static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    static long lcm(int[] arr) {
        long res = arr[0];
        for (int i = 1; i < arr.length; i++) {
            res = (arr[i] * res) / gcd(arr[i], res);
        }
        return res;
    }
}
