import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

public class Second {

    public static class MapRange {
        BigInteger srcStart;
        BigInteger dstStart;
        BigInteger length;

        BigInteger srcEnd;
        public MapRange(BigInteger start, BigInteger end, BigInteger length) {
            this.srcStart = start;
            this.dstStart = end;
            this.length = length;

            this.srcEnd = start.add(length).subtract(BigInteger.ONE);
        }
        boolean contains(BigInteger value) {
            return srcStart.compareTo(value) <= 0 && srcEnd.compareTo(value) >= 0;
        }

        BigInteger transform(BigInteger value) {
            BigInteger diff = value.subtract(srcStart);
            return dstStart.add(diff);
        }


        boolean includes(SeedRange value) {
            return contains(value.start) && contains(value.end);
        }

        @Override
        public String toString() {
            return "Range [src=" + srcStart + ", dst=" + dstStart + ", length=" + length + "]";
        } 
    }

    public static class SeedRange {
        BigInteger start;
        BigInteger end;
        public SeedRange(BigInteger start, BigInteger end) {
            this.start = start;
            this.end = end;
        }

        boolean contains(BigInteger value) {
            return start.compareTo(value) <= 0 && end.compareTo(value) >= 0;
        }

        List<SeedRange> intersect(MapRange mr) {
            List<SeedRange> res = new ArrayList<>();
            BigInteger split = start;
            if (contains(mr.srcStart) && !start.equals(mr.srcStart) && !start.equals((mr.srcStart.subtract(BigInteger.ONE)))) {
                res.add(new SeedRange(start, mr.srcStart.subtract(BigInteger.ONE)));
                split = mr.srcStart;
            }
            if (contains(mr.srcEnd) && !end.equals(mr.srcEnd) && !split.equals(mr.srcEnd.subtract(BigInteger.ONE))) {
                res.add(new SeedRange(split, mr.srcEnd.subtract(BigInteger.ONE)));
                split = mr.srcEnd;
            }
            res.add(new SeedRange(split, end));
            return res;
        }

        @Override
        public String toString() {
            return "SeedRange [start=" + start + ", end=" + end + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((start == null) ? 0 : start.hashCode());
            result = prime * result + ((end == null) ? 0 : end.hashCode());
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
            SeedRange other = (SeedRange) obj;
            if (start == null) {
                if (other.start != null)
                    return false;
            } else if (!start.equals(other.start))
                return false;
            if (end == null) {
                if (other.end != null)
                    return false;
            } else if (!end.equals(other.end))
                return false;
            return true;
        }


    }


    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("tests/Second");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        List<SeedRange> seedsJourney = new ArrayList<>();
        List<BigInteger> tmp = new ArrayList<>();
        List<List<MapRange>> rangeJourney= new ArrayList<>();

        String currentLine;

        if (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            tmp = tokeniseNums(currentLine, 1);
            for (int i = 0; i < tmp.size(); i+= 2) {
                seedsJourney.add(new SeedRange(tmp.get(i), tmp.get(i).add(tmp.get(i + 1)).subtract(BigInteger.ONE)));
            }
        }

        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            if (currentLine.indexOf(':') != -1) {
                rangeJourney.add(new ArrayList<>());
                continue;
            }
            if (currentLine.length() == 0) {
                continue;
            }
            tmp = tokeniseNums(currentLine);
            rangeJourney.get(rangeJourney.size() - 1).add(new MapRange(tmp.get(1), tmp.get(0), tmp.get(2))); // misunderstood input
        }

        for (int i = 0; true; i++) {
            BigInteger cur = BigInteger.valueOf(i);
            for (List<MapRange> map : rangeJourney) {
                for (MapRange m : map) {
                    if (cur.compareTo(m.dstStart) >= 0 && cur.compareTo(m.dstStart.add(m.length)) < 0) {
                        cur = m.srcStart.add(cur).subtract(m.dstStart);
                        break;
                    }
                }
            }
            for (SeedRange sr : seedsJourney) {
                if (cur.compareTo(sr.start) >= 0 && cur.compareTo(sr.end) < 0) {
                    System.out.println(i);
                }
            }

        }

        // List<SeedRange> res = new ArrayList<>();
        // for (SeedRange sr : seedsJourney) {
        //     List<SeedRange> candidates = new ArrayList<>();
        //     candidates.add(sr);
        //
        //     for (List<MapRange> mr : rangeJourney) {
        //         candidates = transformSeeds(candidates, mr);
        //     }
        //
        //     res.addAll(candidates);
        // }
        //
        // BigInteger ans = res.stream()
        //         .map(x -> x.start)
        //         .min(BigInteger::compareTo)
        //         .orElseThrow(NoSuchElementException::new);
        // System.out.println(ans);
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

    // this is so fucking ugly, but i need to update array for each mapping
    static Set<SeedRange> splitSeed(List<SeedRange> seedsJourney, List<MapRange> rangeJourney) {
        Set<SeedRange> intersected = new HashSet<>();
        Set<SeedRange> tmp = new HashSet<>();
        Set<SeedRange> tmp2 = new HashSet<>();
        List<SeedRange> intersectionRes = new ArrayList<>();
        for (SeedRange sr: seedsJourney) {
tmp = new HashSet<>();
            tmp.add(sr);
            for (MapRange mr : rangeJourney) {
                tmp2 = new HashSet<>(); 
                tmp2.addAll(tmp);
                for (SeedRange t : tmp2) {
                    intersectionRes = t.intersect(mr);
                    if (intersectionRes.size() > 1) {
                        tmp.addAll(t.intersect(mr));
                        tmp.remove(t);
                    }
                }
            }
            intersected.addAll(tmp);
        }

        return intersected;
    }


    static List<SeedRange> transformSeeds(List<SeedRange> seedsJourney, List<MapRange> rangeJourney) {
        if (rangeJourney.size() == 0) {
            return seedsJourney;
        }
        Set<SeedRange> intersected = splitSeed(seedsJourney, rangeJourney);
        Set<SeedRange> transformed = new HashSet<>();
        for (SeedRange sr : intersected) {
            SeedRange tmp = null;
            for (MapRange mr : rangeJourney) {
                if (mr.includes(sr)) {
                    tmp = new SeedRange(mr.transform(sr.start), mr.transform(sr.end));
                    break;
                }
            }
            transformed.add(tmp != null ? tmp : sr);
        }
        return new ArrayList<>(transformed); 
    }

}


