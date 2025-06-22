package io.github.coderodde.sudoku;

/**
 *
 * @version 1.0.0 (Dec 4, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public class ParallelSudokuSolverJava {

    public static void main(String[] args) {
        int threads = Runtime.getRuntime().availableProcessors();
        
        while (threads > 0) {
            benchmark(threads);
            threads /= 2;
        }
    }
    
    private static void benchmark(final int threads) {
        
    }
}
