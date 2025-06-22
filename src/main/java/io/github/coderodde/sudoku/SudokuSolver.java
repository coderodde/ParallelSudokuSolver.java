package io.github.coderodde.sudoku;

/**
 * This interface defines the API of sudoku solvers.
 * 
 * @version 1.0.0 (Dec 4, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public interface SudokuSolver {
    
    /**
     * Solves the input sudoku, which becomes modified. 
     * 
     * @param sudokuBoard the sudoku board to solve.
     * @return a solved board.
     */
    public SudokuBoard solve(final SudokuBoard sudokuBoard);
}
