package olymp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class First {
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
        
        int totalSum = 0;
        int curPoints = 0;

        while(sc.hasNext()) {
            String token = sc.next();
            if (token.charAt(0) == 'C') {
                totalSum += curPoints;
                curPoints = 0;
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
                    curPoints = curPoints == 0 ? 1 : curPoints * 2;
                }
                break;
            default:
                throw new RuntimeException("shouldn't reach");

            }
        }
        System.out.println(totalSum);
    }
}
