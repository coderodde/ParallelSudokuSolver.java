package io.github.coderodde.sudoku.misc;

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
}
