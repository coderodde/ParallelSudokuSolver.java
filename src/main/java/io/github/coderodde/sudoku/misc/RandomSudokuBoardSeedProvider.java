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
    
    /**
     * Computes a list of seed sudoku boards.
     * 
     * @param sourceBoard    the source sudoku board.
     * @param requestedSeeds the requested number of seeds.
     * 
     * @return a list of seeds.
     */
    public static List<SudokuBoard> computeSeeds(final SudokuBoard sourceBoard,
                                                 final int requestedSeeds) {
        
        // Get the list of all empty cells:
        final List<Point> emptyCellPoints = getEmptyCellPoints(sourceBoard);
        
        // Fix the seed list capacity:
        final int seedsListCapacity = 
                Math.min(emptyCellPoints.size(), 
                         requestedSeeds);
        
        final Random random = new Random();
        
        final List<SudokuBoard> seeds = new ArrayList<>(seedsListCapacity);
        
        // Actual seed generatoin:
        for (int i = 0; i < seedsListCapacity; ++i) {
            seeds.add(computeRandomSeed(sourceBoard, 
                                        emptyCellPoints, 
                                        random));
        }
        
        return seeds;
    }
    
    /**
     * Computes another random seed.
     * 
     * @param board                the current board.
     * @param emptyCellCoordinates the list of empty cell coordinates.
     * @param random               the random number generator.
     * @return a random seed.
     */
    private static SudokuBoard computeRandomSeed(
            final SudokuBoard board,
            final List<Point> emptyCellCoordinates,
            final Random random) {
        
        // Get a copy:
        final SudokuBoard seed = new SudokuBoard(board);
        
        // Get the index of the target point containing no valid cell value:
        final int targetPointIndex = 
                random.nextInt(emptyCellCoordinates.size());
        
        // Get the target point:
        final Point targetPoint = emptyCellCoordinates.get(targetPointIndex);
        
        // Get an array of randomly arranged cell values:
        final int[] cellValues = getRandomCellValues(board.getWidthHeight());
        
        for (final int cellValue : cellValues) {
            // Set the cell value:
            seed.set(targetPoint.x, 
                     targetPoint.y, 
                     cellValue);
            
            // Check that after new cell value the seed remains valid:
            if (SudokuBoardVerifier.isValid(seed)) {
                // Once here, we have found a seed. Return it:
                emptyCellCoordinates.remove(targetPointIndex);
                return seed;
            }
        }
        
        throw new IllegalStateException("Should not get here");
    }
    
    /**
     * Computes the list of point coordinates pointing to empty cells.
     * 
     * @param board the board to process.
     * @return the list of point coordinates pointing to empty cells.
     */
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
    
    /**
     * Computes a randomly ordered array of cell values.
     * 
     * @param widthHeight the width/height of the target board.
     * @return a randomly ordered array of cell values.
     */
    private static int[] getRandomCellValues(final int widthHeight) {
        final int[] cellValues = new int[widthHeight];
        
        for (int i = 0; i < widthHeight; ++i) {
            cellValues[i] = i + 1;
        }
        
        Utils.shuffle(cellValues);
        return cellValues;
    }
}
