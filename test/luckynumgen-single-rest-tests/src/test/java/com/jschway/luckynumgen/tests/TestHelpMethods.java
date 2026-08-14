package com.jschway.luckynumgen.tests;
/**
 * 
 * @author JonathanSaddler
 */
public class TestHelpMethods {
    
    public  static void mark(int toMark, boolean[][] coverage) { 
        int row = toMark / 10;
        int col = toMark % 10;
        coverage[row][col] = true;
    }
    public  static void mark(int row, int col, boolean[][] coverage) {
        coverage[row][col] = true;
    }
    public static void mark(String input, boolean[][] coverage) {
        int toMark = Integer.parseInt(input);
        int row = toMark / 10;
        int col = toMark % 10;
        coverage[row][col] = true;
    }
}
