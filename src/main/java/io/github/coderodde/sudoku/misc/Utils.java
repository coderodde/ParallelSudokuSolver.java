package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import java.util.Random;

/**
 * This class provides common utilities.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class Utils {
    
    public static final int UNUSED_CELL = 0;
    private static final int MINIMUM_WIDTH_HEIGHT = 4;
    
    private Utils() {
        
    }
    
    public static void checkWidthHeight(final int widthHeight) {
        if (widthHeight < 4) {
            final String exceptionMessage =
                    String.format(
                            "The widthHeight(%d) < MINIMUM_WIDTH_HEIGHT(%d)",
                            widthHeight,
                            MINIMUM_WIDTH_HEIGHT);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        int n;
        
        for (n = MINIMUM_WIDTH_HEIGHT / 2; ; ++n) {
            if (n * n >= widthHeight) {
                break;
            }
        }
        
        if (n * n != widthHeight) {
            final String exceptionMessage = 
                    String.format(
                            "Abnormal widthHeight(%d)",
                            widthHeight);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    
    /**
     * This static method implements the 
     * <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">
     * Fisher-Yates shuffle</a>.
     * 
     * @param array the array to shuffle.
     */
    public static void shuffle(final int[] array, final Random random) {
        for (int i = array.length - 1; i > 0; --i) {
            final int j = random.nextInt(i + 1);
            final int tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }   
    
    /**
     * Passes to {@link #shuffle(int[], java.util.Random)}.
     * 
     * @param array the array to shuffle.
     */
    public static void shuffle(final int[] array) {
        shuffle(array, new Random());
    }
    
    public static boolean isCompleteSudokuBoard(final SudokuBoard board) {
        for (int y = 0; y < board.getWidthHeight(); ++y) {
            for (int x = 0; x < board.getWidthHeight(); ++x) {
                if (board.get(x, y) == Utils.UNUSED_CELL) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
