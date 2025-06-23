package io.github.coderodde.sudoku;

import io.github.coderodde.sudoku.misc.IntSet;
import io.github.coderodde.sudoku.misc.RandomSudokuBoardSeedProvider;
import io.github.coderodde.sudoku.misc.SudokuBoardVerifier;
import io.github.coderodde.sudoku.misc.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
            return null;
        }
        
        loadIntSets(sudokuBoard);
        
        final List<SudokuSolverThread> threads =
                new ArrayList<>(numberOfProcessors);
        
        final List<SudokuBoard> seeds = 
                RandomSudokuBoardSeedProvider
                        .computeSeeds(sudokuBoard,
                                      numberOfProcessors);
        
        final SharedThreadState sharedThreadState = 
                new SharedThreadState();
        
        for (int i = 0; i < Math.min(numberOfProcessors, seeds.size()); ++i) {
            threads.add(
                    new SudokuSolverThread(new SudokuBoard(seeds.get(i)),
                                           sudokuBoard, 
                                           sharedThreadState));
            threads.get(i).start();
        }
        
        for (final SudokuSolverThread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        return sharedThreadState.getSolution(); 
    }
    
    private static SudokuBoard getCompleteSudokuBoard(
            final List<SudokuSolverThread> threads) {
        
        for (final SudokuSolverThread thread : threads) {
            if (Utils.isCompleteSudokuBoard(thread.seed)) {
                return thread.seed;
            }
        }
        
        throw new IllegalStateException("No complete sudoku boards");
    }
    
    private void loadIntSets(final SudokuBoard board) {
        final int n = board.getWidthHeight();
        final int capacity = n + 1;
        sqrtn = (int) Math.sqrt(n);
        
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
    }
    
    private final class SudokuSolverThread extends Thread {
        
        private final SudokuBoard seed;
        private final SudokuBoard original;
        private SudokuBoard result;
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
        
        public SudokuBoard getResult() {
            return result;
        }
        
        private boolean solveImpl(int x,
                                  int y) {
            
//            System.out.println("x = " + x + ", y = " + y);
            
            if (sharedThreadState.getSolution() != null) {
                return true;
            }
            
            if (x == seed.getWidthHeight()) {
                x = 0;
                ++y;
            }
            
            if (y == seed.getWidthHeight()) {
//                System.out.println("found!");
                sharedThreadState.setSolution(seed);
//                result = seed;
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
                                     [x / sqrtn].contains(cellValue)) {
                    continue;
                }
                
                seed.set(x, y, cellValue);
                
                rowIntSets[y].add(cellValue);
                colIntSets[x].add(cellValue);
                minisquareIntSets[y / sqrtn]
                                 [x / sqrtn].add(cellValue);
                
                if (solveImpl(x + 1, y)) {
//                    sharedThreadState.setSolution(seed);
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
