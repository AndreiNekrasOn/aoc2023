package olymp;

import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class First {
    static class GalaxyPair {
        int x;
        int y;
        int i;
        int j;
        public GalaxyPair(int x, int y, int i, int j) {
            if (x < i) {
                this.x = x;
                this.y = y;
                this.i = i;
                this.j = j;
            } else if (x > i) {
                this.x = i;
                this.y = j;
                this.i = x;
                this.j = y;

            } else {
                this.x = x;
                this.i = i;
                this.y = Math.min(y, j);
                this.j = Math.max(y, j);
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + i;
            result = prime * result + j;
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GalaxyPair other = (GalaxyPair) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (i != other.i)
                return false;
            if (j != other.j)
                return false;
            return true;
        }

    }
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        // Scanner sc = new Scanner(in);
        Scanner sc = new Scanner(System.in);

        List<List<Integer>> sky = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            sky.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                sky.get(sky.size() - 1).add(line.charAt(i) == '#' ? 1 : 0);
            }
        }
        sky = verticalExpansion(sky);
        sky = horisontalExpansion(sky);
        int res = getSumMinDist(sky);
        System.out.println(res);

    }

    private static void printArr(List<List<Integer>> arr) {
        for (List<Integer> list : arr) {
            for (Integer i : list) {
                System.out.print(i);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int getSumMinDist(List<List<Integer>> sky) {
        Set<GalaxyPair> visited = new HashSet<>();
        int[][] s = sky.stream()
                .map(l -> l.stream().mapToInt(Integer::intValue).toArray())
                .toArray(int[][]::new);
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                if (s[i][j] == 0) {
                    continue;
                }
                for (int x = 0; x < s.length; x++) {
                    for (int y = 0; y < s[0].length; y++) {
                        if (s[x][y] == 0) {
                            continue;
                        }
                        if (x == i && y == j) {
                            continue;
                        }
                        visited.add(new GalaxyPair(x, y, i, j));
                    }
                }
            }
        }
        return visited.stream().mapToInt(g -> manhDist(g.x, g.y, g.i, g.j)).sum();
    }

    private static List<List<Integer>> verticalExpansion(List<List<Integer>> sky) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < sky.size(); i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < sky.get(i).size(); j++) {
                result.get(i).add(sky.get(i).get(j));
                if (isZeroColumn(sky, j)) {
                    result.get(i).add(sky.get(i).get(j));
                }
            }
        }
        return result;
    }

    private static boolean isZeroColumn(List<List<Integer>> sky, int j) {
        boolean isZero = true;
        for (int i = 0; i < sky.size(); i++) {
            isZero &= sky.get(i).get(j) == 0;
        }
        return isZero;
    }

    private static List<List<Integer>> horisontalExpansion(List<List<Integer>> sky) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < sky.size(); i++) {
            result.add(sky.get(i));
            if (isZeroRow(sky, i)) {
                result.add(sky.get(i));
            }
        }
        return result;
    }

    private static boolean isZeroRow(List<List<Integer>> sky, int i) {
        return sky.get(i).stream().allMatch(x -> x == 0);
    }

    private static int manhDist(int x, int y, int i, int j) {
        return Math.abs(x - i) + Math.abs(y - j);
    }

}
