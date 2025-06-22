package com.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomSudokuBoardGeneratorTest {
    @Test
    public void generatesValidRandomSudokuBoard() {
        final SudokuBoard board = 
                new RandomSudokuBoardGenerator(4)
                        .generateRandomSudokuBoard();
        
        System.out.println(board);
        
        assertTrue(SudokuBoardVerifier.verify(board));
    }
}
