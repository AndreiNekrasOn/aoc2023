import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Second {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("input.txt");
        Scanner sc = new Scanner(input);
        // Scanner sc = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        Map<Integer, List<int[]>> parts = new HashMap<>();
        fillPartsInfo(lines, parts);
        System.out.println(getGearSum(lines, parts));
        
    }

    static int getGearSum(List<String> lines, Map<Integer, List<int[]>> parts) {
        int sum = 0;
        for (int i = 0; i < lines.size(); i++) {
            final String currentLine = lines.get(i);

            for (int j = 0; j < currentLine.length(); j++) {
                if (currentLine.charAt(j) != '*') {
                    continue;
                }
                final int gearPosition = j;

                List<Integer> nearbyPartsCoords = new ArrayList<>();
                for (int candidate = i - 1; candidate <= i + 1; candidate++) {
                    if (parts.containsKey(candidate)) {
                        final int cnd = candidate;
                        nearbyPartsCoords.addAll(parts.get(candidate).stream()
                            .filter(x -> isPartNear(gearPosition, x))
                            .mapToInt(x -> partCoordsToInt(lines.get(cnd), x[0], x[1]))
                            .boxed()
                            .collect(Collectors.toList())
                        );
                    }
                }
                if (nearbyPartsCoords.size() == 2) {
                    sum += nearbyPartsCoords.stream()
                        .reduce(1, (x, y) -> x * y);
                }
            }
        }
        return sum;
    }

    static Integer partCoordsToInt(String line, int start, int end) {
        return Integer.parseInt(line.substring(start, end + 1));
    } 

    static boolean isPartNear(int pos, int[] coords) {
        return (coords[1] == pos - 1) ||
                (coords[0] <= pos && coords[1] >= pos) ||
                (coords[0] == pos + 1); 
    }


    static void fillPartsInfo(List<String> lines, Map<Integer, List<int[]>> parts) {
        for (int i = 0; i < lines.size(); i++) {
            // locate number
            // then check surroundings
            int numStart = 0;
            int numEnd = 0;
            String currentLine = lines.get(i);
            if (currentLine.isEmpty()) {
                continue;
            }
            while(numEnd < currentLine.length()) {
                numStart = nextDigit(currentLine, numEnd);
                if (numStart >= currentLine.length() ||
                        !Character.isDigit(currentLine.charAt(numStart))) {
                    break;
                }
                numEnd = lastDigit(currentLine, numStart);
                if (numEnd > currentLine.length() ||
                        !Character.isDigit(currentLine.charAt(numEnd - 1))) {
                    break;
                }
                if (isSurrounded(lines, i, numStart, numEnd - 1)) {
                    int[] partCoords = {numStart, numEnd - 1};
                    if (!parts.containsKey(i)) {
                        parts.put(i, new ArrayList<>());
                    }
                    parts.get(i).add(partCoords);
                }
            }
        }
    }


    static int nextDigit(String line, int pos) {
        for (; pos < line.length() && !Character.isDigit(line.charAt(pos)); pos++);
        return pos;
    }

    // pos at the simbol right after digit
    static int lastDigit(String line, int pos) {
        for (; pos < line.length() && Character.isDigit(line.charAt(pos)); pos++);
        return pos;
    }

    static boolean isSurrounded(List<String> lines, int row, int colstart, int colend) {
        boolean res = false;
        if (row - 1 >= 0) {
            res |= hasAdjacent(lines.get(row - 1), colstart, colend);
        }
        if (colstart - 1 >= 0) {
            res |= lines.get(row).charAt(colstart - 1) != '.';
        }
        if (colend + 1 < lines.get(row).length()) {
            res |= lines.get(row).charAt(colend + 1) != '.';
        }
        if (row + 1 < lines.size()) {
            res |= hasAdjacent(lines.get(row + 1), colstart, colend);
            // System.out.println(hasAdjacent(lines.get(row + 1), colstart, colend));
            // System.out.println(Math.max(0,  colstart -1 ));
            // System.out.println(Math.min(lines.get(row + 1).length(),  colend + 1));
        }
        return res;
    }

    static boolean hasAdjacent(String line, int colstart, int colend) {
        for (int i = Math.max(0, colstart - 1); i <= Math.min(line.length() - 1, colend + 1); i++) {
            if (line.charAt(i) != '.') {
                return true;
            }
        }
        return false;
    }

}
