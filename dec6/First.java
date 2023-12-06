package olymp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class First {
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(Sjystem.in);
        
        List<Integer> times = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();

        int i = 0;
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.indexOf(":") == -1) {
                if (i == 1) {
                    times.add(Integer.parseInt(token));
                } else {
                    distances.add(Integer.parseInt(token));
                }
            } else {
                i++;
            }
        }

        int n = times.size();

        List<Integer> totalBeat = new ArrayList<>();
        int product = 1;
        for (int raceIdx = 0; raceIdx < n; raceIdx++) {
            int beatCnt = 0;
            for (int timeWait = 1; timeWait < times.get(raceIdx) - 1; timeWait++) {
                if (calcDist(timeWait, times.get(raceIdx)) > distances.get(raceIdx)) {
                    beatCnt++;
                }
            }
            product *= beatCnt;
        }
        System.out.println(product);
    }

    static int calcDist(int timeWait, int timeTotal) {
        return timeWait * (timeTotal - timeWait); 
    }
}
