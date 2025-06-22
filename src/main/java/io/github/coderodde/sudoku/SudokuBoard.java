package io.github.coderodde.sudoku;

/**
 * This class implements a sudoku board.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class SudokuBoard {
    
    public static final int UNUSED_CELL = 0;
    private static final int MINIMUM_WIDTH_HEIGHT = 4;
    
    private final int[][] data;
    
    public SudokuBoard(final int widthHeight) {
        checkWidthHeight(widthHeight);
        this.data = new int[widthHeight]
                           [widthHeight];
    }
    
    public boolean isValidCellValue(final int x,
                                    final int y) {
        final int cellValue = get(x, 
                                  y);
        
        return 1 <= cellValue && cellValue <= data.length;
    }
    
    public int getWidthHeight() {
        return data.length;
    }
    
    public void set(final int x,
                    final int y,
                    final int value) {
        data[y][x] = value;
    }
    
    public int get(final int x, 
                   final int y) {
        return data[y][x];
    }
    
    private static void checkWidthHeight(final int widthHeight) {
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
