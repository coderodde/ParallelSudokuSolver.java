package com.github.coderodde.sudoku;

import io.github.coderodde.sudoku.misc.SudokuBoardVerifier;
import io.github.coderodde.sudoku.SudokuBoard;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public final class SudokuBoardTest {

    @Test
    public void verifyFalseOnDuplicateMinisquare() {
        final SudokuBoard board = new SudokuBoard(4);
        
        board.set(1, 1, 1);
        board.set(0, 0, 1);
        
        assertFalse(SudokuBoardVerifier.verify(board));
    }

    @Test
    public void verifyFalseOnDuplicateColumn() {
        final SudokuBoard board = new SudokuBoard(4);
        
        board.set(0, 0, 2);
        board.set(0, 3, 2);
        
        assertFalse(SudokuBoardVerifier.verify(board));
    }

    @Test
    public void verifyFalseOnDuplicateRow() {
        final SudokuBoard board = new SudokuBoard(4);
        
        board.set(1, 2, 3);
        board.set(3, 2, 3);
        
        assertFalse(SudokuBoardVerifier.verify(board));
    }
    
    @Test
    public void verifyFalseOnInvalidCellValue1() {
        final SudokuBoard board = new SudokuBoard(4);
        
        board.set(1, 2, -1);
        
        assertFalse(SudokuBoardVerifier.verify(board));
    }
    
    @Test
    public void verifyFalseOnInvalidCellValue2() {
        final SudokuBoard board = new SudokuBoard(4);
        
        board.set(3, 0, 5);
        
        assertFalse(SudokuBoardVerifier.verify(board));
    }
}
