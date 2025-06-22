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
    private final IntSet[] rowIntSets;
    private final IntSet[] colIntSets;
    private final IntSet[][] minisquareIntSets;
    private final int nsqrt;
    
    public RandomSudokuBoardGenerator(final int widthHeight) {
        this.board = new SudokuBoard(widthHeight);
        this.providers = new RandomCellValueProvider[widthHeight]
                                                    [widthHeight];
        
        for (int y = 0; y < widthHeight; ++y) {
            for (int x = 0; x < widthHeight; ++x) {
                this.providers[y][x] = new RandomCellValueProvider(widthHeight);
            }
        }
        
        this.rowIntSets = new IntSet[widthHeight];
        this.colIntSets = new IntSet[widthHeight];
        this.minisquareIntSets = new IntSet[widthHeight]
                                           [widthHeight];
        
        for (int y = 0; y < widthHeight; ++y) {
            this.rowIntSets[y] = new IntSet(widthHeight + 1);
        }
        
        for (int x = 0; x < widthHeight; ++x) {
            this.colIntSets[x] = new IntSet(widthHeight + 1);
        }
        
        final int sqrtn = (int) Math.sqrt(widthHeight);;
        
        for (int y = 0; y < widthHeight; ++y) {
            for (int x = 0; x < widthHeight; ++x) {
                this.minisquareIntSets[y / sqrtn]
                                      [x / sqrtn] = new IntSet(widthHeight + 1);
            }
        }
        
        this.nsqrt = (int) Math.sqrt(widthHeight);
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
        
        while (providers[y][x].hasMoreNextCellValues()) {
            
            final int cellValue = providers[y][x].getNextCellValue();
            
            if (rowIntSets[y].contains(cellValue)) {
                continue;
            }
            
            if (colIntSets[x].contains(cellValue)) {
                continue;
            }
            
            if (minisquareIntSets[y / nsqrt]
                                 [x / nsqrt].contains(cellValue)) {
                continue;
            }
            
            rowIntSets[y].add(cellValue);
            colIntSets[x].add(cellValue);
            minisquareIntSets[y / nsqrt][x/ nsqrt].add(cellValue);
            
            board.set(x, y, cellValue);
            
            final boolean solved = generateRandomSudokuBoardImpl(board,
                                                                 x + 1,
                                                                 y);
            if (solved) {
                return true;
            }
            
            rowIntSets[y].remove(cellValue);
            colIntSets[x].remove(cellValue);
            
            minisquareIntSets[y / nsqrt]
                             [x / nsqrt].remove(cellValue);
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
            return cursor < randomCellValues.length;
        }
        
        /**
         * This static method implements the 
         * <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">
         * Fisher-Yates shuffle</a>.
         * 
         * @param array the array to shuffle.
         */
        private static void shuffle(final int[] array) {
            final Random random = new Random(13L);
            
            for (int i = array.length - 1; i > 0; --i) {
                final int j = random.nextInt(i + 1);
                final int tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
            }
        }
    }
}
