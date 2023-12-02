package olymp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class A {
    static String[] spelledDigits = {"one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine"};
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("input.txt");
        Scanner sc = new Scanner(input);
        // Scanner sc = new Scanner(System.in);
        int sum = 0;
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            int firstIdx = Math.min(
                findFirstDigit(data),
                findFirstSpelledDigit(data)
            );
            int lastIdx = Math.max(
                findLastDigit(data),
                findLastSpelledDigit(data)
            );
            sum += getDigitAtIndex(data, firstIdx) * 10 + 
                    getDigitAtIndex(data, lastIdx);
        }
        System.out.println(sum); 
    }

    private static int findFirstDigit(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isDigit(data.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    private static int findLastDigit(String data) {
        for (int i = data.length() - 1; i >= 0; i--) {
            if (Character.isDigit(data.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int findFirstSpelledDigit(String data) {
        int minIdx = data.length() + 1;
        for (String sd : spelledDigits) {
            int idx = data.indexOf(sd);
            if (idx != -1) {
                minIdx = Math.min(idx, minIdx);
            }

        }
        return minIdx;
    }
    private static int findLastSpelledDigit(String data) {
        int maxIdx = -1;
        for (String sd : spelledDigits) {
            int idx = data.lastIndexOf(sd);
            if (idx != -1) {
                maxIdx = Math.max(idx, maxIdx);
            }

        }
        return maxIdx;
    }

    private static Integer getDigitAtIndex(String data, int idx) {
        if (Character.isDigit(data.charAt(idx))) {
            return Integer.valueOf(data.charAt(idx) - '0');
        }
        for (int i = 0; i < spelledDigits.length; i++) {
            String substr = data.substring(idx, 
                    Math.min(data.length(), idx + spelledDigits[i].length()));

            if (spelledDigits[i].equals(substr)) {
                return i + 1; // digit representation
            }
        }
        throw new RuntimeException("bug");
    }
}
