package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class provides a method for pruning sudoku boards.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class RandomSudokuBoardPruner {
    
    /**
     * Prunes {@code cellsToPrune} cells away from the input sudoku board by 
     * setting them to {@link Utils#UNUSED_CELL}.
     * 
     * @param board        the target sudoku board from which to prune cells.
     * @param cellsToPrune the number of cells to prune.
     */
    public static void prune(final SudokuBoard board, 
                             final int cellsToPrune,
                             final Random random) {
        
        final List<Point> pointList = new ArrayList<>(board.getWidthHeight() *
                                                      board.getWidthHeight());
        
        // Load all the coordinate points:
        for (int y = 0; y < board.getWidthHeight(); ++y) {
            for (int x = 0; x < board.getWidthHeight(); ++x) {
                pointList.add(new Point(x, y));
            }
        }
        
        // Shuffle randomly:
        Collections.shuffle(pointList, random);
        
        // Prune the sudoku board cells randomly according to pointList:
        for (int i = 0; i < cellsToPrune && !pointList.isEmpty(); ++i) {
            final int index = pointList.size() - 1;
            final Point p = pointList.get(index);
            
            // Prune cell:
            board.set(p.x, 
                      p.y, 
                      Utils.UNUSED_CELL);
            
            // Update pointList state:
            pointList.remove(index);
        }
    }
}
