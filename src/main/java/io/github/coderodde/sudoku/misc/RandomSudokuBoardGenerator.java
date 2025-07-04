package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import static io.github.coderodde.sudoku.misc.Utils.shuffle;
import java.util.Random;

/**
 * This class is responsible for randomly generating full sudoku boards.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class RandomSudokuBoardGenerator {
    
    /**
     * The generated sudoku board.
     */
    private final SudokuBoard board;
    
    /**
     * The matrix of random cell value providers.
     */
    private final RandomCellValueProvider[][] providers;
    
    /**
     * The row-wise filters.
     */
    private final IntSet[] rowIntSets;
    
    /**
     * The column-wise filters.
     */
    private final IntSet[] colIntSets;
    
    /**
     * The minisquare filters.
     */
    private final IntSet[][] minisquareIntSets;
    
    /**
     * The minisquare width/height.
     */
    private final int nsqrt;
    
    /**
     * Construct this sudoku board generator.
     * 
     * @param widthHeight the width/height of the resulting sudoku board.
     * @param random      the random number generator.
     */
    public RandomSudokuBoardGenerator(final int widthHeight,
                                      final Random random) {
        this.board = new SudokuBoard(widthHeight);
        this.providers = new RandomCellValueProvider[widthHeight]
                                                    [widthHeight];
        
        for (int y = 0; y < widthHeight; ++y) {
            for (int x = 0; x < widthHeight; ++x) {
                this.providers[y][x] = new RandomCellValueProvider(widthHeight,
                                                                   random);
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
    
    /**
     * The actual generation method.
     * 
     * @return a randomly  built sudoku board.
     */
    public SudokuBoard generateRandomSudokuBoard() {
        generateRandomSudokuBoardImpl(board, 0, 0);
        return board;
    }
    
    /**
     * Implements the random sudoku board generation.
     * 
     * @param board the board to work on.
     * @param x the {@code x}-coordinate of the next cell to process.
     * @param y the {@code y}-coordinate of the next cell to process.
     * @return {@code true} if and only if a valid sudoku board is constructed.
     *         (Will end up in {code board}.)
     */
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
        
        for (final int cellValue : providers[y][x].getCellValues()) {
            
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
            minisquareIntSets[y / nsqrt]
                             [x / nsqrt].add(cellValue);
            
            board.set(x, y, cellValue);
            
            if (generateRandomSudokuBoardImpl(board,
                                              x + 1,
                                              y)) {
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
        
        private final int[] randomCellValues;

        /**
         * Constructs this random cell value provider.
         * 
         * @param n the width/height of the target sudoku board.
         */
        RandomCellValueProvider(final int n, final Random random) {
            this.randomCellValues = new int[n];
            
            for (int i = 0; i < n; ++i) {
                randomCellValues[i] = i + 1;
            }
            
            shuffle(randomCellValues, random);
        }
        
        int[] getCellValues() {
            return this.randomCellValues;
        }
    }
}
