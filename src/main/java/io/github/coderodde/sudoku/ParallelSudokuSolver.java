package io.github.coderodde.sudoku;

import io.github.coderodde.sudoku.misc.IntSet;
import io.github.coderodde.sudoku.misc.Utils;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This interface defines the API of sudoku solvers.
 * 
 * @version 1.0.0 (Dec 4, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public final class ParallelSudokuSolver {
    
    private int sqrtn;
    private IntSet[] rowIntSets;
    private IntSet[] colIntSets;
    private IntSet[][] minisquareIntSets;
    
    /**
     * Solves the input sudoku, which becomes modified. 
     * 
     * @param sudokuBoard the sudoku board to solve.
     * @return a solved board.
     */
    public SudokuBoard solve(final SudokuBoard sudokuBoard) {
        loadIntSets(sudokuBoard);
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
                             final int numberOfProcessors) {
        return null;
    }
    
    private void loadIntSets(final SudokuBoard board) {
        final int n = board.getWidthHeight();
        final int capacity = n + 1;
        sqrtn = (int) Math.sqrt(n);
        
        rowIntSets = new IntSet[n];
        colIntSets = new IntSet[n];
        minisquareIntSets = new IntSet[n][n];
        
        for (int i = 0; i < n; ++i) {
            rowIntSets[i] = new IntSet(capacity);
            colIntSets[i] = new IntSet(capacity);
        }
        
        for (int y = 0; y < sqrtn; ++y) {
            minisquareIntSets[y] = new IntSet[n];
            
            for (int x = 0; x < sqrtn; ++x) {
                minisquareIntSets[y][x] = new IntSet(capacity);
            }
        }
    }
    
    private final class SudokuSolverThread extends Thread {
        
        private final SudokuBoard seed;
        private final SudokuBoard original;
        private final SharedThreadState sharedThreadState;
        
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
        
        private boolean solveImpl(int x,
                                  int y) {
            
            if (sharedThreadState.isSolutionFound()) {
                return true;
            }
            
            if (x == seed.getWidthHeight()) {
                x = 0;
                ++y;
            }
            
            if (y == seed.getWidthHeight()) {
                sharedThreadState.setSolutionFound();
                return true;
            }
            
            if (original.get(x, y) != Utils.UNUSED_CELL) {
                seed.set(x,
                         y,
                         original.get(x, y));
                
                return solveImpl(x + 1, y);
            }
            
            for (int cellValue = 1; 
                     cellValue <= seed.getWidthHeight(); 
                     cellValue++) {
                
                if (rowIntSets[y].contains(cellValue)) {
                    continue;
                }
                
                if (colIntSets[x].contains(cellValue)) {
                    continue;
                }
                
                if (minisquareIntSets[y / sqrtn]
                                     [x / sqrtn].contains(y)) {
                    continue;
                }
                
                seed.set(x, y, cellValue);
                
                rowIntSets[y].add(cellValue);
                colIntSets[x].add(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].add(cellValue);
                
                if (solveImpl(x + 1, y)) {
                    sharedThreadState.setSolutionFound();
                    return true;
                }
                
                rowIntSets[y].remove(cellValue);
                colIntSets[x].remove(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].remove(cellValue);
            }

            return false;
        }
    }
    
    private static final class SharedThreadState {
        
        private final AtomicBoolean solutionFoundFlag = new AtomicBoolean(false);
        
        public boolean isSolutionFound() {
            return solutionFoundFlag.get();
        }
        
        public void setSolutionFound() {
            solutionFoundFlag.set(true);
        }
    }
}
