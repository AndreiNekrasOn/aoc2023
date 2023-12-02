package olymp;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class First {

    static final int[] limitsRGB = {12, 13, 14};
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
            Boolean isPossible = true;
            for (String game: gameScores) {
                isPossible &= isGamePossible(game);
            }
            if (isPossible) {
                sum += gameId;
            }
        }
        System.out.println(sum);
    }

    static boolean isGamePossible(String game) {
        String[] colorScores = game.split(",");
        Boolean isPossible = true;
        for (int i = 0; i < colorScores.length; i++) {
            // indices because of data format, hackish way
            String color = colorScores[i].split(" ")[2];
            isPossible = Integer.valueOf(colorScores[i].split(" ")[1]) <= 
                    limitsRGB[order.indexOf(color)];
            if (!isPossible) {
                return false;
            }
        }
        return true;

    }



}
