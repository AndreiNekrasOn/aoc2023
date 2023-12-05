package olymp;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class First {

    public static class Range {
        BigInteger start;
        BigInteger end;
        BigInteger length;
        public Range(BigInteger start, BigInteger end, BigInteger length) {
            this.start = start;
            this.end = end;
            this.length = length;
        }
        private boolean contains(BigInteger value) {
            return start.compareTo(value) <= 0 && start.add(length).compareTo(value) >= 0;
        }

        BigInteger transform(BigInteger value) {
            BigInteger diff = value.subtract(start);
            return end.add(diff);
        }

        @Override
        public String toString() {
            return "Range [start=" + start + ", end=" + end + ", length=" + length + "]";
        } 

    }



    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        List<BigInteger> seedsJourney = new ArrayList<>();
        List<BigInteger> tmp = new ArrayList<>();
        List<Range> rangeJourney= new ArrayList<>();

        String currentLine;

        if (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            seedsJourney = tokeniseNums(currentLine, 1);
        }

        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            if (currentLine.indexOf(':') != -1) {
                seedsJourney = transformSeeds(seedsJourney, rangeJourney);
                rangeJourney = new ArrayList<>();
                continue;
            }
            if (currentLine.length() == 0) {
                continue;
            }
            tmp = tokeniseNums(currentLine);
            rangeJourney.add(new Range(tmp.get(1), tmp.get(0), tmp.get(2))); // misunderstood input
        }
        seedsJourney = transformSeeds(seedsJourney, rangeJourney);
        BigInteger ans = seedsJourney.stream().min(BigInteger::compareTo).orElseThrow(NoSuchElementException::new);
        System.out.println(ans);
    }

    static List<BigInteger> tokeniseNums(String line) {
        return tokeniseNums(line, 0);
    }
    static List<BigInteger> tokeniseNums(String line, int start) {
        String[] snums = line.split(" ");
        List<BigInteger> res = new ArrayList<>();
        for (int i = start; i < snums.length; i++) {
            res.add(new BigInteger(snums[i]));
        }
        return res;
    }

    static List<BigInteger> transformSeeds(List<BigInteger> seedsJourney, List<Range> rangeJourney) {
        if (rangeJourney.size() == 0) {
            return seedsJourney;
        }
        List<BigInteger> transformed = new ArrayList<>();
        for (BigInteger seed : seedsJourney) {
            BigInteger tmp = null;
            for (int i = 0; i < rangeJourney.size(); i++) {
                if (rangeJourney.get(i).contains(seed)) {
                    tmp = rangeJourney.get(i).transform(seed);
                    break;
                }
            }
            transformed.add(tmp != null ? tmp : seed);
        }
        return transformed;
    }

}


