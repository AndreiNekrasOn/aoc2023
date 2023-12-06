import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Second {
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);
        
        StringBuilder totalTimeBuilder = new StringBuilder();
        StringBuilder totalDistBuilder = new StringBuilder();

        int i = 0;
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.indexOf(":") == -1) {
                if (i == 1) {
                    totalTimeBuilder.append(token);
                } else {
                    totalDistBuilder.append(token);
                }
            } else {
                i++;
            }
        }

        int totalTime = Integer.parseInt(totalTimeBuilder.toString());
        long totalDist = Long.parseLong(totalDistBuilder.toString());

        int beatCnt = 0;
        for (int timeWait = 0; timeWait < totalTime; timeWait++) {
            if (calcDist(timeWait, totalTime) > totalDist) {
                beatCnt++;
            }
        }
        System.out.println(beatCnt);
    }

    static long calcDist(long timeWait, long timeTotal) {
        return timeWait * (timeTotal - timeWait); 
    }
}
