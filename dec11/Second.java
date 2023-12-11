import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class Second {
    static class GalaxyPair {
        long x;
        long y;
        long i;
        long j;
        public GalaxyPair(long x, long y, long i, long j) {
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
        public String toString() {
            return "[x=" + x + ", y=" + y + ", i=" + i + ", j=" + j + "]\n";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (x ^ (x >>> 32));
            result = prime * result + (int) (y ^ (y >>> 32));
            result = prime * result + (int) (i ^ (i >>> 32));
            result = prime * result + (int) (j ^ (j >>> 32));
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
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        List<List<Integer>> sky = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            sky.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                sky.get(sky.size() - 1).add(line.charAt(i) == '#' ? 1 : 0);
            }
        }
        List<Long> verticalIndices = verticalExpansion(sky);
        List<Long> horisontalIndices = horisontalExpansion(sky);
        long res = getSumMinDist(sky, verticalIndices, horisontalIndices);
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

    private static long getSumMinDist(List<List<Integer>> sky, List<Long> verticalIndices, List<Long> horisontalIndices) {
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
        Set<GalaxyPair> fixed = new HashSet<>();
        for (GalaxyPair g : visited) {
            long[] coords = getRealCoords(g, verticalIndices, horisontalIndices);
            fixed.add(new GalaxyPair(coords[0], coords[1], coords[2], coords[3]));
        }
        return fixed.stream().mapToLong(g -> manhDist(g.x,g.y,g.i,g.j)).sum();
    }

    private static long[] getRealCoords(GalaxyPair g, List<Long> verticalIndices, List<Long> horisontalIndices) {
        return new long[] {
            horisontalIndices.get((int) g.x),
            verticalIndices.get((int) g.y),
            horisontalIndices.get((int) g.i),
            verticalIndices.get((int) g.j)
        };
    }

    private static List<Long> verticalExpansion(List<List<Integer>> sky) {
        List<Long> result = new ArrayList<>();
        long carry = 0;
        for (int j = 0; j < sky.get(0).size(); j++) {
            result.add(j + carry);
            if (isZeroColumn(sky, j)) {
                carry += 1000000 - 1;
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

    private static List<Long> horisontalExpansion(List<List<Integer>> sky) {
        List<Long> result = new ArrayList<>();
        long carry = 0;
        for (int i = 0; i < sky.size(); i++) {
            result.add(i + carry);
            if (isZeroRow(sky, i)) {
                carry += 1000000 - 1;
            }
        }
        return result;
    }

    private static boolean isZeroRow(List<List<Integer>> sky, int i) {
        return sky.get(i).stream().allMatch(x -> x == 0);
    }

    private static long manhDist(long[] coords) {
        return manhDist(coords[0], coords[1], coords[2], coords[3]);
    }

    private static long manhDist(long x, long y, long i, long j) {
        return Math.abs(x - i) + Math.abs(y - j);
    }

}
