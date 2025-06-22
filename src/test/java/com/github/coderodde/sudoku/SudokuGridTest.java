package com.github.coderodde.sudoku;

import io.github.coderodde.sudoku.SudokuGrid;
import org.junit.Test;

public final class SudokuGridTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis2() {
        SudokuGrid.checkNisSquareOfInteger(2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis3() {
        SudokuGrid.checkNisSquareOfInteger(3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis5() {
        SudokuGrid.checkNisSquareOfInteger(5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis6() {
        SudokuGrid.checkNisSquareOfInteger(6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis7() {
        SudokuGrid.checkNisSquareOfInteger(7);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis8() {
        SudokuGrid.checkNisSquareOfInteger(8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsWhenNis10() {
        SudokuGrid.checkNisSquareOfInteger(10);
    }
    
    @Test
    public void testDoesNotThrowWhenNis1() {
        SudokuGrid.checkNisSquareOfInteger(1);
    }
    
    @Test
    public void testDoesNotThrowWhenNis4() {
        SudokuGrid.checkNisSquareOfInteger(4);
    }
    
    @Test
    public void testDoesNotThrowWhenNis9() {
        SudokuGrid.checkNisSquareOfInteger(9);
    }
    
    @Test
    public void testDoesNotThrowWhenNis16() {
        SudokuGrid.checkNisSquareOfInteger(16);
    }
    
    @Test
    public void testDoesNotThrowWhenNis25() {
        SudokuGrid.checkNisSquareOfInteger(25);
    }
}
