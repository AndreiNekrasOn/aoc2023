import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Second {
    enum State {
        CARD,
        wINNER,
        OWNED
    }

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("input.txt");
        Scanner sc = new Scanner(input);
        // Scanner sc = new Scanner(System.in);


        State state = State.CARD;
        Set<Integer> winNumbers = new HashSet<>();
        
        int curCopiesWon = 0;

        // 1-indexed
        int[] copiesCounter = new int[1024]; // hard code >= 214=number of total lines
        // otherwise would do array list
        for (int i = 0; i < 1024; i++) {
            copiesCounter[i] = 1;
        }
        copiesCounter[0] = 0;


        int cardNum = 0;
        while(sc.hasNext()) {
            String token = sc.next();
            if (token.charAt(0) == 'C') {
                // System.out.printf("%d | ", curCopiesWon);
                // for (int i = 1; i <= 6; i++) {
                //     System.out.printf("%d ", copiesCounter[i]);
                // }
                // System.out.println();
                //
                if (curCopiesWon == 0 && copiesCounter[cardNum + 1] == 0) {
                    break;
                }
                for (int i = 1; i <= curCopiesWon; i++) {
                    copiesCounter[cardNum + i] += copiesCounter[cardNum];
                }
                cardNum++;
                curCopiesWon = 0;
                winNumbers = new HashSet<>();

                state = State.CARD;
                continue;
            }
            if (state == State.CARD) {
                state = token.charAt(token.length() - 1) == ':' ? State.wINNER : State.CARD;
                continue;
            }

            if (token.charAt(0) == '|')  {
                state = State.OWNED;
                continue;
            }

            int num = Integer.parseInt(token);
            switch(state) {
            case wINNER:
                winNumbers.add(num);
                break;
            case OWNED:
                if (winNumbers.contains(num)) {
                    curCopiesWon++;
                }
                break;
            default:
                throw new RuntimeException("shouldn't reach");

            }
        }
        int totalSum = 0;
        for (int i = 1; i <= cardNum; i++) {
            // System.out.printf("%d ", copiesCounter[i]);
            totalSum += copiesCounter[i];
        }
        // System.out.println();
        System.out.println(totalSum);
    }
}
