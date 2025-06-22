//package io.github.coderodde.sudoku;
//
//import java.util.Arrays;
//
///**
// * This class implements the actual {@code N}-sudoku grid with {@code N} rows 
// * and columns.
// * 
// * @version 1.0.0 (Dec 4, 2024)
// * @since 1.0.0 (Dec 4, 2024)
// */
//public final class SudokuGrid {
//    
//    private final int[][] gridData;
//    
//    public SudokuGrid(final int n) {
//        checkNisSquareOfInteger(n);
//        gridData = new int[n][n];
//    }
//    
//    public int dimension() {
//        return gridData.length;
//    }
//    
//    public int get(final int x, final int y) {
//        return gridData[y][x];
//    }
//    
//    public void set(final int x, final int y, final int value) {
//        gridData[y][x] = value;
//    }
//    
//    /**
//     * Returns an independent copy of this sudoku grid.
//     * 
//     * @return a copy of this grid.
//     */
//    public SudokuGrid clone() {
//        final SudokuGrid copy = new SudokuGrid(dimension());
//        
//        for (int y = 0; y < dimension(); y++) {
//            for (int x = 0; x < dimension(); x++) {
//                copy.set(x, y, this.get(x, y));
//            }
//        }
//        
//        return copy;
//    }
//    
//    public boolean isValidNumber(final int number) {
//        return 1 <= number && number <= dimension();
//    }
//    
//    /**
//     * Checks that the input number {@code n} is a square of a positive integer.
//     * 
//     * @param n the number to check.
//     */
//    static void checkNisSquareOfInteger(final int n) {
//        int number = 1;
//        
//        while (true) {
//            if (number * number == n) {
//                return;
//            } else if (number * number > n) {
//                final String exceptionMessage = 
//                        String.format(
//                                "n = %d is not a square of a positive integer.",
//                                number);
//                
//                throw new IllegalArgumentException(exceptionMessage);
//            }
//            
//            number++;
//        }
//    }
//}
