package com.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import java.util.Random;

/**
 * This class is responsible for randomly generating full sudoku boards.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class RandomSudokuBoardGenerator {
    
    private final SudokuBoard board;
    private final RandomCellValueProvider[][] providers;
    
    public RandomSudokuBoardGenerator(final int widthHeight) {
        this.board = new SudokuBoard(widthHeight);
        this.providers = new RandomCellValueProvider[widthHeight]
                                                    [widthHeight];
        
        for (int y = 0; y < widthHeight; ++y) {
            for (int x = 0; x < widthHeight; ++x) {
                this.providers[y][x] = new RandomCellValueProvider(widthHeight);
            }
        }
    }
    
    public SudokuBoard generateRandomSudokuBoard() {
        generateRandomSudokuBoardImpl(board, 0, 0);
        return board;
    }
    
    private boolean generateRandomSudokuBoardImpl(final SudokuBoard board, 
                                                  int x,
                                                  int y) {
        if (x == board.getWidthHeight()) {
            x = 0;
            ++y;
        }
        
        if (y == board.getWidthHeight()) {
            return true;
        }
        
        if (providers[y][x].hasMoreNextCellValues()) {
            board.set(x, y, providers[y][x].getNextCellValue());
            
            return generateRandomSudokuBoardImpl(board,
                                                 x + 1,
                                                 y);
        }
        
        return false;
    }
    
    /**
     * This inner static class implements a random cell value provider.
     */
    private static final class RandomCellValueProvider {
        
        private int cursor = 0;
        private final int[] randomCellValues;

        /**
         * Constructs this random cell value provider.
         * 
         * @param n the width/height of the target sudoku board.
         */
        RandomCellValueProvider(final int n) {
            this.randomCellValues = new int[n];
            
            for (int i = 0; i < n; ++i) {
                randomCellValues[i] = i + 1;
            }
            
            shuffle(randomCellValues);
        }
        
        int getNextCellValue() {
            return randomCellValues[cursor++];
        }
        
        boolean hasMoreNextCellValues() {
            return cursor < randomCellValues.length - 1;
        }
        
        /**
         * This static method implements the 
         * <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">
         * Fisher-Yates shuffle</a>.
         * 
         * @param array the array to shuffle.
         */
        private static void shuffle(final int[] array) {
            final Random random = new Random();
            
            for (int i = array.length - 1; i > 0; --i) {
                final int j = random.nextInt(i + 1);
                final int tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
            }
        }
    }
    
    
}
