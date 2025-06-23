package io.github.coderodde.sudoku;

import io.github.coderodde.sudoku.misc.IntSet;
import io.github.coderodde.sudoku.misc.RandomSudokuBoardSeedProvider;
import io.github.coderodde.sudoku.misc.SudokuBoardVerifier;
import io.github.coderodde.sudoku.misc.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class implements a parallel sudoku solver.
 * 
 * @version 1.0.0 (Dec 4, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public final class ParallelSudokuSolver {
    
    /**
     * The width/height of the minisquare.
     */
    private int sqrtn;
    
    /**
     * The row-wise filters.
     */
    private IntSet[] rowIntSets;
    
    /**
     * The column-wise filters.
     */
    private IntSet[] colIntSets;
    
    /**
     * The minisquare filters.
     */
    private IntSet[][] minisquareIntSets;
    
    /**
     * Solves the input sudoku, which becomes modified. 
     * 
     * @param sudokuBoard the sudoku board to solve.
     * @return a solved board.
     */
    public SudokuBoard solve(final SudokuBoard sudokuBoard) {
        return solve(sudokuBoard, 
                     Runtime.getRuntime().availableProcessors());
    }
    /**
     * Solves the input sudoku, 
     * 
     * @param sudokuBoard the sudoku board to solve.
     * @param numberOfProcessors the number of processors to use.
     * @return a solved board.
     */
    public SudokuBoard solve(final SudokuBoard sudokuBoard,
                             int numberOfProcessors) {
        
        if (!SudokuBoardVerifier.isValid(sudokuBoard)) {
            // Don't process invalid sudoku boards:
            return null;
        }
        
        // Once here, sudokuBoard is valid.
        if (Utils.isCompleteSudokuBoard(sudokuBoard)) {
            // Once here, the sudokuBoard is both valid and complete. Just 
            // return it!
            return sudokuBoard;
        }
        
        // Preload all the filters basing on present values in the sudokuBoard:
        loadIntSets(sudokuBoard);
        
        // The solver thread list:
        final List<SudokuSolverThread> threads =
                new ArrayList<>(numberOfProcessors);
        
        // The seeds of the search. Each seed will be passes as initial board to
        // each solver thread:
        final List<SudokuBoard> seeds = 
                RandomSudokuBoardSeedProvider
                        .computeSeeds(sudokuBoard,
                                      numberOfProcessors);
        
        // Used for halting all the threads when a solution is found:
        final SharedThreadState sharedThreadState = 
                new SharedThreadState();
        
        // Spawn the threads:
        for (int i = 0; i < Math.min(numberOfProcessors, seeds.size()); ++i) {
            threads.add(
                    new SudokuSolverThread(new SudokuBoard(seeds.get(i)),
                                           sudokuBoard, 
                                           sharedThreadState));
            threads.get(i).start();
        }
        
        // Wait for all the solver threads to exit:
        for (final SudokuSolverThread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        // Fetch and return the solution:
        return sharedThreadState.getSolution(); 
    }
    
    /**
     * Preloads all the filters from the input board.
     * 
     * @param board the board from which to fetch all the cell values for the
     *              filters.
     */
    private void loadIntSets(final SudokuBoard board) {
        final int n = board.getWidthHeight();
        final int capacity = n + 1;
        sqrtn = (int) Math.sqrt(n);
        
        // BEGIN: Create filters.
        rowIntSets = new IntSet[n];
        colIntSets = new IntSet[n];
        minisquareIntSets = new IntSet[sqrtn][sqrtn];
        
        for (int i = 0; i < n; ++i) {
            rowIntSets[i] = new IntSet(capacity);
            colIntSets[i] = new IntSet(capacity);
        }
        
        for (int y = 0; y < sqrtn; ++y) {
            minisquareIntSets[y] = new IntSet[sqrtn];
            
            for (int x = 0; x < sqrtn; ++x) {
                minisquareIntSets[y][x] = new IntSet(capacity);
            }
        }
        // END: Create filters.
        
        // BEGIN: Preload filter values.
        for (int y = 0; y < board.getWidthHeight(); ++y) {
            for (int x = 0; x < board.getWidthHeight(); ++x) {
                final int cellValue = board.get(x, y);
                
                if (cellValue == Utils.UNUSED_CELL) {
                    continue;
                }
                
                rowIntSets[y].add(cellValue);
                colIntSets[x].add(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].add(cellValue);
            }
        }
        // END: Preload filter values.
    }
    
    /**
     * This inner class implements sudoku solver threads.
     */
    private final class SudokuSolverThread extends Thread {
        
        /**
         * The seeding sudoku board.
         */
        private final SudokuBoard seed;
        
        /**
         * The original task sudoku board.
         */
        private final SudokuBoard original;
        
        /**
         * The shared thread state. Used for communicating that a solution is 
         * found and all the threads must exit.
         */
        private final SharedThreadState sharedThreadState;
        
        /**
         * The random number generator.
         */
        private final Random random = new Random();
        
        /**
         * Constructs this thread.
         * 
         * @param seed              the seed sudoku board.
         * @param original          the original sudoku board.
         * @param sharedThreadState the shared thread state.
         */
        SudokuSolverThread(final SudokuBoard seed,
                           final SudokuBoard original,
                           final SharedThreadState sharedThreadState) {
            this.seed = seed;
            this.original = original;
            this.sharedThreadState = sharedThreadState;
        }
        
        @Override
        public void run() {
            solveImpl(0, 0);
        }
        
        /**
         * Returns an {@code int} array of cell values in random order.
         * 
         * @return cell values.
         */
        private int[] getCellValues() {
            final int[] cellValues = new int[seed.getWidthHeight()];
            
            for (int i = 0; i < seed.getWidthHeight(); ++i) {
                cellValues[i] = i + 1;
            }
            
            Utils.shuffle(cellValues, random);
            return cellValues;
        }
        
        /**
         * The actual solution method.
         * 
         * @param x the {@code x}-coordinate of the cell to process next.
         * @param y the {@code y}-coordinate of the cell to process next.
         * 
         * @return {@code true} if a solution was found deeper in the search.
         */
        private boolean solveImpl(int x,
                                  int y) {
            
            if (sharedThreadState.getSolution() != null) {
                // Once here, we have a solution and we simple exit with true:
                return true;
            }
            
            if (x == seed.getWidthHeight()) {
                // Once here, we have reached the right border. Go to the
                // beginning of the next row:
                x = 0;
                ++y;
            }
            
            if (y == seed.getWidthHeight()) {
                // Once here, we have a solution. Record it and exit with true:
                sharedThreadState.setSolution(seed);
                return true;
            }
            
            if (original.get(x, y) != Utils.UNUSED_CELL) {
                // Once here, we need just to copy a cell value from the 
                // original sudoku board to the seed:
                seed.set(x,
                         y,
                         original.get(x, y));
                
                // Process further:
                return solveImpl(x + 1, y);
            }
            
            // Get an array of cell values in random order:
            final int[] cellValues = getCellValues();
            
            for (final int cellValue : cellValues) {
                
                if (rowIntSets[y].contains(cellValue)) {
                    // Once here, the row already contains cellValue:
                    continue;
                }
                
                if (colIntSets[x].contains(cellValue)) {
                    // Once here, the column already contains cellValue:
                    continue;
                }
                
                if (minisquareIntSets[y / sqrtn]
                                     [x / sqrtn].contains(cellValue)) {
                    // Once here, the minisquare already contains cellValue:
                    continue;
                }
                
                // Write the cell value to seed:
                seed.set(x, y, cellValue);
                
                // BEGIN: Mark in filters.
                rowIntSets[y].add(cellValue);
                colIntSets[x].add(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].add(cellValue);
                // END: Mark in filters.
                
                if (solveImpl(x + 1, y)) {
                    // Recur further:
                    return true;
                }
                
                // BEGIN: Unmark in filters.
                rowIntSets[y].remove(cellValue);
                colIntSets[x].remove(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].remove(cellValue);
                // END: Unmark in filters.
            }

            // Once here, we could not set to (x, y). Backtrack a little:
            return false;
        }
    }
    
    /**
     * This class implements a simple shared thread state.
     */
    private static final class SharedThreadState {
        
        /**
         * Solution so far. Starts with {@code null}.
         */
        private final AtomicReference<SudokuBoard> solution = 
                  new AtomicReference(null);
        
        public SudokuBoard getSolution() {
            return solution.get();
        }
        
        public void setSolution(final SudokuBoard board) {
            solution.set(board);
        }
    }
}
