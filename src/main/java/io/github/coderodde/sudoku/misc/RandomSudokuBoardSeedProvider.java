package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class provides facilities for generating random sudoku board seeds.
 * 
 * @version 1.0.0 (Jun 23, 2025)
 * @since 1.0.0 (Jun 23, 2025)
 */
public final class RandomSudokuBoardSeedProvider {
    
    public static List<SudokuBoard> computeSeeds(final SudokuBoard sourceBoard,
                                                 final int requestedSeeds) {
        
        final List<Point> emptyCellPoints = getEmptyCellPoints(sourceBoard);
        
        final int seedsListCapacity = 
                Math.min(emptyCellPoints.size(), 
                         requestedSeeds);
        
        final Random random = new Random();
        
        final List<SudokuBoard> seeds = new ArrayList<>(seedsListCapacity);
        
        for (int i = 0; i < seedsListCapacity; ++i) {
            seeds.add(computeRandomSeed(sourceBoard, 
                                        emptyCellPoints, 
                                        random));
        }
        
        return seeds;
    }
    
    private static SudokuBoard computeRandomSeed(
            final SudokuBoard board,
            final List<Point> emptyCellCoordinates,
            final Random random) {
        
        final SudokuBoard seed = new SudokuBoard(board);
        final int targetPointIndex = 
                random.nextInt(emptyCellCoordinates.size());
        
        final Point targetPoint = emptyCellCoordinates.get(targetPointIndex);
        
        final int[] cellValues = getRandomCellValues(board.getWidthHeight());
        
        for (final int cellValue : cellValues) {
            
            seed.set(targetPoint.x, 
                     targetPoint.y, 
                     cellValue);
            
            if (SudokuBoardVerifier.isValid(seed)) {
                emptyCellCoordinates.remove(targetPointIndex);
                return seed;
            }
        }
        
        return null;
    }
    
    private static List<Point> getEmptyCellPoints(final SudokuBoard board) {
        final List<Point> emptyCellPoints = new ArrayList<>();
        
        for (int y = 0; y < board.getWidthHeight(); ++y) {
            for (int x = 0; x < board.getWidthHeight(); ++x) {
                if (board.get(x, y) == Utils.UNUSED_CELL) {
                    emptyCellPoints.add(new Point(x, y));
                }
            }
        }
        
        return emptyCellPoints;
    }
    
    private static int[] getRandomCellValues(final int widthHeight) {
        final int[] cellValues = new int[widthHeight];
        
        for (int i = 0; i < widthHeight; ++i) {
            cellValues[i] = i + 1;
        }
        
        Utils.shuffle(cellValues);
        return cellValues;
    }
}
