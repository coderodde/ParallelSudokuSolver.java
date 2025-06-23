package io.github.coderodde.sudoku.misc;

import io.github.coderodde.sudoku.SudokuBoard;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public final class RandomSudokuBoardSeedProviderTest {

    @Test
    public void getRandomSeed1() {
        final SudokuBoard board = new SudokuBoard(4);
        board.set(0, 0, 1);
        board.set(3, 3, 4);
        final List<SudokuBoard> seeds = 
                RandomSudokuBoardSeedProvider.computeSeeds(board, 100);
        
        int boardNumber = 1;
        
        for (final SudokuBoard seed : seeds) {
            System.out.println("--- Board " + (boardNumber++));
            System.out.println(seed);
            assertTrue(SudokuBoardVerifier.isValid(seed));
        }
    }
}
