package com.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.misc.SudokuBoardVerifier;
import io.github.coderodde.sudoku.misc.RandomSudokuBoardGenerator;
import io.github.coderodde.sudoku.SudokuBoard;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomSudokuBoardGeneratorTest {
    @Test
    public void generatesValidRandomSudokuBoard() {
        final SudokuBoard board = 
                new RandomSudokuBoardGenerator(16, new Random())
                        .generateRandomSudokuBoard();
        
        System.out.println(board);
        
        assertTrue(SudokuBoardVerifier.isValid(board));
    }
}
