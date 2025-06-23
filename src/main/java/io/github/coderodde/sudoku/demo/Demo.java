package io.github.coderodde.sudoku.demo;

import io.github.coderodde.sudoku.ParallelSudokuSolver;
import io.github.coderodde.sudoku.SudokuBoard;
import io.github.coderodde.sudoku.misc.RandomSudokuBoardGenerator;
import io.github.coderodde.sudoku.misc.RandomSudokuBoardPruner;
import io.github.coderodde.sudoku.misc.SudokuBoardVerifier;
import io.github.coderodde.sudoku.misc.Utils;
import java.util.Random;

/**
 * This class implements a demonstration program comparing the parallel sudoku
 * search with varying number of threads.
 * 
 * @version 1.1.0 (Jun 23, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public class Demo {

    private static final int WIDTH_HEIGHT = 16;
    private static final int NUMBER_OF_CELLS_TO_PRUNE = 150;
    
    public static void main(String[] args) {
        final Random random = new Random();
        int threads = Runtime.getRuntime().availableProcessors() * 4;
        
        System.out.println("Generating a random full sudoku board...");
        
        final SudokuBoard sourceSudokuBoard = 
                new RandomSudokuBoardGenerator(WIDTH_HEIGHT, random)
                        .generateRandomSudokuBoard();
        
        System.out.println("Board generated. Pruning...");
        
        RandomSudokuBoardPruner.prune(sourceSudokuBoard, 
                                      NUMBER_OF_CELLS_TO_PRUNE,
                                      random);
        
        while (threads > 0) {
            benchmark(threads, new SudokuBoard(sourceSudokuBoard));
            threads /= 2;
        }
    }
    
    private static void benchmark(final int threads,
                                  final SudokuBoard sourceSudokuBoard) {
        
        final long ta = System.currentTimeMillis();
        
        final SudokuBoard solution = 
                new ParallelSudokuSolver()
                        .solve(sourceSudokuBoard,
                               threads);
        
        final long tb = System.currentTimeMillis();
        final long duration = tb - ta;
        
        final boolean isValid = SudokuBoardVerifier.isValid(solution) &&
                                Utils.isCompleteSudokuBoard(solution);
        
        System.out.println(solution);
        System.out.printf("Threads: %2d, duration: %d ms, complete = %b.\n\n", 
                          threads, 
                          duration,
                          isValid);
    }
}
