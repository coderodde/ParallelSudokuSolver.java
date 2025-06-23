package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;

/**
 * This class provides a static method for checking that an input sudoku board 
 * is valid, i.e., does not break rules of sudoku. For example, the method in
 * question must return {@code false} if a column contains duplicate cell 
 * values.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class SudokuBoardVerifier {
   
    private SudokuBoardVerifier() {
        
    }
    
    /**
     * Verifies the input sudoku board.
     * 
     * @param board the sudoku board to verify.
     * 
     * @return {@code true} if and only if the input sudoku board is valid.
     */
    public static boolean isValid(final SudokuBoard board) {
        final int n = board.getWidthHeight();
        
        final IntSet[] rowIntSets = new IntSet[n];
        final IntSet[] colIntSets = new IntSet[n];
        
        for (int i = 0; i < n; ++i) {
            rowIntSets[i] = new IntSet(n + 1);
            colIntSets[i] = new IntSet(n + 1);
        }
        
        final int sqrtn = (int) Math.sqrt(n);
        final IntSet[][] minisquareIntSets = new IntSet[sqrtn]
                                                       [sqrtn];
        
        for (int y = 0; y < sqrtn; ++y) {
            for (int x = 0; x < sqrtn; ++x) {
                minisquareIntSets[y][x] = new IntSet(n + 1);
            }
        }
        
        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                final int cellValue = board.get(x, y);
                
                if (cellValue == Utils.UNUSED_CELL) {
                    continue;
                }
                
                if (!board.isValidCellValue(x, y)) {
                    return false;
                }
                
                final int minisquareCellX = x / sqrtn;
                final int minisquareCellY = y / sqrtn;
                
                if (minisquareIntSets[minisquareCellY]
                                     [minisquareCellX].contains(cellValue)) {
                    return false;
                }
                
                minisquareIntSets[minisquareCellY]
                                 [minisquareCellX].add(cellValue);
                
                if (rowIntSets[x].contains(cellValue)) {
                    return false;
                }
                
                rowIntSets[x].add(cellValue);
                
                if (colIntSets[y].contains(cellValue)) {
                    return false;
                }
                
                colIntSets[y].add(cellValue);
            }
        }
        
        return true;
    }
}
