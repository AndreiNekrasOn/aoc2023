package olymp;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class First {

    static class HB {
        String hand;
        long bid;
        public HB(String hand, long bid) {
            this.hand = hand;
            this.bid = bid;
        }
        @Override
        public String toString() {
            return "HB [hand=" + hand + "]";
        }

    }

    enum Type {
        Five,
        Four,
        Full,
        Three,
        TP,
        Pair,
        High
    }

    static final Map<Type, Integer> typeStrength = new HashMap<>() {{
        put(Type.Five, 6);
        put(Type.Four,5);
        put(Type.Full, 4);
        put(Type.Three, 3);
        put(Type.TP, 2);
        put(Type.Pair, 1);
        put(Type.High, 0);
    }};

    static final String cardStrengths = "23456789TJQKA";

    public static void main(String[] args) throws FileNotFoundException {
        // parse the input
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        Map<String, Integer> hand2bid = new HashMap<>();
        List<HB> hbs = new ArrayList<>();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] hb = input.split(" ");
            hbs.add(new HB(hb[0], Integer.parseInt(hb[1])));
        }

        Collections.sort(hbs, new Comparator<HB>() {
            public int compare(HB l, HB r) { return compareHbs(l, r); }
        });

        int n = hbs.size();
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans += hbs.get(i).bid * (i + 1);
        }
        out.println(ans);
    }

    public static Type getHandType(String hand) {
        Map<Character, Integer> uniqCnt = new HashMap<>();

        for (int i = 0; i < hand.length(); i++) {
            if (uniqCnt.containsKey(hand.charAt(i))) {
                uniqCnt.put(hand.charAt(i), uniqCnt.get(hand.charAt(i)) + 1);
            }
            uniqCnt.putIfAbsent(hand.charAt(i), 1);
        }

        if (uniqCnt.values().contains(5)) {
            return Type.Five;
        } else if (uniqCnt.values().contains(4)) {
            return Type.Four;
        } else if (uniqCnt.values().contains(3) && uniqCnt.values().contains(2)) {
            return Type.Full;
        } else if (uniqCnt.values().contains(3)) {
            return Type.Three;
        } else if (uniqCnt.values().stream().filter(v -> v == 2).count() == 2) {
            return Type.TP;
        } else if (uniqCnt.values().contains(2)) {
            return Type.Pair;
        }
        return Type.High;

    }


    // cardStrengths are reversed
    static int compareCards(Character l, Character r) {
        return cardStrengths.indexOf(l) - cardStrengths.indexOf(r);
    }


    static int compareHbs(HB l, HB r) {
        int typeDiff = typeStrength.get(getHandType(l.hand)) - typeStrength.get(getHandType(r.hand));
        if (typeDiff == 0) {
            for (int i = 0; i < l.hand.length(); i++) {
                if (compareCards(l.hand.charAt(i), r.hand.charAt(i)) < 0) {
                    return -1;
                } else if (compareCards(l.hand.charAt(i), r.hand.charAt(i)) > 0) {
                    return 1;
                }
            }
        }
        return typeDiff;
    }
}
