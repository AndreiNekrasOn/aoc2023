package olymp;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class Second {

    static final List<String> order = List.of("red", "green", "blue");

    public static void main(String[] args) throws FileNotFoundException {
        // Scanner sc = new Scanner(System.in);
        File input = new File("input.txt");
        Scanner sc = new Scanner(input);
        int sum = 0;
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            String[] gameInfo = data.split(":");
            int gameId = Integer.valueOf(gameInfo[0].substring("Game ".length()));
            String[] gameScores = gameInfo[1].split(";");
            int[] minlimit = new int[3];
            for (String game: gameScores) {
                int[] limit = getLimit(game);
                for (int i = 0; i < 3; i++) {
                    minlimit[i] = Math.max(minlimit[i], limit[i]);
                }
            }
            sum += getGamePower(minlimit);
        }
        System.out.println(sum);
    }

    static int[] getLimit(String game) {
        int[] limit = new int[3];
        String[] colorScores = game.split(",");
        for (int i = 0; i < colorScores.length; i++) {
            String color = colorScores[i].split(" ")[2];
            limit[order.indexOf(color)] = Integer.valueOf(colorScores[i].split(" ")[1]); 
        }
        return limit;
    }

    static int getGamePower(int[] limit) {
        return limit[0] * limit[1] * limit[2];
    }

}
