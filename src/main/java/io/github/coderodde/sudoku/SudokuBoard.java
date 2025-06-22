package io.github.coderodde.sudoku;

import static io.github.coderodde.sudoku.misc.Utils.checkWidthHeight;

/**
 * This class implements a sudoku board.
 * 
 * @version 1.0.0 (Jun 22, 2025)
 * @since 1.0.0 (Jun 22, 2025)
 */
public final class SudokuBoard {
    
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
    
    /**
     * Returns the ASCII art of this sudoku board.
     * 
     * @return the ASCII art of this sudoku board.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String horizontalBar = getHorizontalBar(data.length);
        
        sb.append(horizontalBar);
        
        for (int y = 0; y < data.length; ++y) {
            final String row = getDataRow(data[y]);
            
            sb.append('\n')
              .append(row)
              .append('\n')
              .append(horizontalBar);
        }
        
        return sb.toString();
    }
    
    /**
     * Computes a string representing a row in a sudoku board.
     * 
     * @param dataRow the data row of a sudoku.
     * 
     * @return a string representation of the data row.
     */
    private static String getDataRow(final int[] dataRow) {
        final String maximumCellWidth = Integer.toString(dataRow.length);
        final int cellWidth = maximumCellWidth.length();
        final String fmt = String.format("|%%%dd", cellWidth);
        final StringBuilder sb = 
                new StringBuilder(
                        1 + (1 + cellWidth) * dataRow.length);
        
        for (int x = 0; x < dataRow.length; ++x) {
            sb.append(String.format(fmt, dataRow[x]));
        }
        
        return sb.append('|').toString();
    }
    
    /**
     * Returns the horizontal separating bar.
     * 
     * @param widthHeight the width/height of the target sudoku board.
     * 
     * @return the string representation of the separating bar.
     */
    private static String getHorizontalBar(final int widthHeight) {
        final String nstr = Integer.toString(widthHeight);
        final String innerCellWidth = getInnerCellWidth(nstr.length());
        final StringBuilder sb = new StringBuilder("+");
        
        for (int x = 0; x < widthHeight; ++x) {
            sb.append(innerCellWidth)
              .append('+');
        }
        
        return sb.toString();
    }
    
    /**
     * Computes the inner cell upper horizontal bar.
     * 
     * @param cellWidth the width of a cell.
     * 
     * @return inner cell upper horizontal bar.
     */
    private static String getInnerCellWidth(final int cellWidth) {
        final StringBuilder sb = new StringBuilder(cellWidth);
        
        for (int x = 0; x < cellWidth; ++x) {
            sb.append('-');
        }
        
        return sb.toString();
    }
}
