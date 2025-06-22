//package io.github.coderodde.sudoku.impl;
//
//import io.github.coderodde.sudoku.SudokuGrid;
//import io.github.coderodde.sudoku.SudokuSolver;
//import com.github.coderodde.sudoku.misc.IntSet;
//import java.awt.Point;
//import java.util.Deque;
//import java.util.concurrent.ConcurrentLinkedDeque;
//
///**
// * This class implements the parallel sudoku solver.
// * 
// * @version 1.0.0 (Dec 4, 2024)
// * @since 1.0.0 (Dec 4, 2024)
// */
//public class ParallelSudokuSolver implements SudokuSolver {
//    
//    private static final int DEFAULT_DEPTH = 3;
//    
//    private final int numberOfThreads;
//    private final int depth;
//    
//    public ParallelSudokuSolver(final int threads,
//                                final int depth) {
//        this.numberOfThreads = threads;
//        this.depth = checkDepth(depth);
//    }
//    
//    public ParallelSudokuSolver(final int threads) {
//        this(threads, DEFAULT_DEPTH);
//    }
//    
//    public ParallelSudokuSolver() {
//        this(Runtime.getRuntime().availableProcessors());
//    }
//
//    @Override
//    public SudokuSolver solve(final SudokuGrid sudokuGrid) {
//        
//        final ParallelSudokuSolverThread[] threads = 
//                new ParallelSudokuSolverThread[numberOfThreads];
//        
//        final Deque<ParallelSudokuSolverThreadTask> workQueue = 
//                new ConcurrentLinkedDeque<>();
//        
//        loadWorkQueue(workQueue, 
//                      depth,
//                      sudokuGrid);
//        
//        for (int i = 0; i < numberOfThreads; i++) {
//            threads[i] = new ParallelSudokuSolverThread(sudokuGrid.dimension(),
//                                                        workQueue);
//            
//            threads[i].start();
//        }
//        
//        for (int i = 0; i < numberOfThreads; i++) {
//            try {
//                threads[i].join();
//            } catch (final InterruptedException ex) {
//                throw new IllegalStateException("Joining a thread failed.", ex);
//            }
//        }
//    }
//    
//    private void loadWorkQueue(
//            final Deque<ParallelSudokuSolverThreadTask> workQueue,
//            final int depth,
//            final SudokuGrid sudokuGrid) {
//        
//        int x = 0;
//        int y = 0;
//        int tentativeDepth = 0;
//        
//        
//    }
//    
//    private Point getStartPoint(final SudokuGrid sudokuGrid) {
//        int x = 0;
//        int y = 0;
//        
//        for (int i = 0; i < depth; i++) {
//            final int number = sudokuGrid.get(x, y);
//            
//            if (sudokuGrid.isValidNumber(number)) {
//                // Valid number at (x, y), omit:
//                i--;
//                
//                x++;
//                
//                if (x == sudokuGrid.dimension()) {
//                    x = 0;
//                    y++;
//                }
//                
//                continue;
//            }
//        }
//        
//        return new Point(x, y);
//    }
//
//    private int checkDepth(final int depth) {
//        if (depth < 1) {
//            final String exceptionMessage =
//                    String.format("depth = %d < 1", depth);
//            
//            throw new IllegalArgumentException(exceptionMessage);
//        }
//        
//        return depth;
//    }
//}
//
//final class ParallelSudokuSolverThreadTask {
//    
//    private final SudokuGrid sudokuGrid;
//    private final int x;
//    private final int y;
//
//    ParallelSudokuSolverThreadTask(final SudokuGrid sudokuGrid,
//                                   final int x,
//                                   final int y) {
//        this.sudokuGrid = sudokuGrid;
//        this.x = x;
//        this.y = y;
//    }
//
//    SudokuGrid getSudokuGrid() {
//        return sudokuGrid;
//    }
//    
//    int getX() {
//        return x;
//    }
//    
//    int getY() {
//        return y;
//    }
//}
//
//final class ParallelSudokuSolverThread extends Thread {
//    
//    /**
//     * The dimension of the solved sudoku grid. For example, 4, 9 or 16.
//     */
//    private final int dimension;
//    
//    /**
//     * A flag indicating that a solution is found. Search threads read this flag
//     * once in a while and it is {@code true}, the threads exit.
//     */
//    private volatile boolean solutionFound = false;
//    
//    /**
//     * The task queue.
//     */
//    private final Deque<ParallelSudokuSolverThreadTask> workQueue;
//    
//    /**
//     * The array of all the search threads. Used for notifying upon solution.
//     */
//    private ParallelSudokuSolverThread[] threads;
//    
//    /**
//     * The filters for rows.
//     */
//    private final IntSet[] rowIntSet;
//    
//    /**
//     * The filters for columns.
//     */
//    private final IntSet[] colIntSet;
//    
//    /**
//     * The filters for the minisquares.
//     */
//    private final IntSet[][] minisquareIntMatrix;
//    
//    /**
//     * The solution sudoku grid.
//     */
//    private SudokuGrid solution;
//    
//    private final Point point = new Point();
//    
//    /**
//     * Constructs this thread.
//     * '
//     * @param dimension the dimension of the sudoku being solved.
//     * @param workQueue the queue of work descriptor.
//     */
//    ParallelSudokuSolverThread(
//            final int dimension,
//            final Deque<ParallelSudokuSolverThreadTask> workQueue) {
//        
//        this.dimension = dimension;
//        this.workQueue = workQueue;
//        
//        final int capacity = dimension + 1;
//        
//        rowIntSet = new IntSet[dimension];
//        colIntSet = new IntSet[dimension];
//        minisquareIntMatrix = new IntSet[dimension]
//                                        [dimension];
//        
//        for (int i = 0; i < dimension; i++) {
//            rowIntSet[i] = new IntSet(capacity);
//            colIntSet[i] = new IntSet(capacity);
//            minisquareIntMatrix[i] = new IntSet[dimension];
//            
//            for (int j = 0; j < dimension; j++) {
//                minisquareIntMatrix[i][j] = new IntSet(capacity);
//            }
//        }   
//    }
//    
//    void setThreads(final ParallelSudokuSolverThread[] threads) {
//        this.threads = threads;
//    }
//    
//    @Override
//    public void run() {
//        
//        while (!solutionFound) {
//            if (workQueue.isEmpty()) {
//                return;
//            }
//
//            final ParallelSudokuSolverThreadTask task = workQueue.removeFirst();
//            final SudokuGrid sudokuGrid = task.getSudokuGrid();
//            
//            solution = sudokuGrid.clone();
//            
//            final int x = task.getX();
//            final int y = task.getY();
//
//            solve(sudokuGrid,
//                  solution, 
//                  x,
//                  y);
//        }
//    }
//    
//    private boolean solve(final SudokuGrid source,
//                          final SudokuGrid solution,
//                          int x,
//                          int y) {
//        
//        if (solutionFound) {
//            return true;
//        }
//        
//        if (x == source.dimension()) {
//            x = 0;
//            y++;
//        }
//        
//        if (y == source.dimension()) {
//            halt();
//            return true;
//        }
//        
//        final int number = source.get(x, y);
//        
//        if (source.isValidNumber(number)) {
//            // Once here, we have a predefined number. Omit it:
//            solution.set(x, y, number);
//            
//            return solve(source,
//                         solution, 
//                         x + 1,
//                         y);
//        }
//        
//        for (int i = 1; i <= source.dimension(); ++i) {
//            if (!colIntSet[x].contains(i) && !rowIntSet[y].contains(i)) {
//                
//                loadMinisquareCoordinates(x, y);
//                
//                if (!minisquareIntMatrix[point.y]
//                                        [point.x].contains(i)) {
//                    
//                    solution.set(x, y, i);
//                    rowIntSet[y].add(i);
//                    colIntSet[x].add(i);
//                    minisquareIntMatrix[point.y]
//                                       [point.x].add(i);
//                    
//                    if (solve(source, solution, x + 1, y)) {
//                        return true;
//                    }
//                    
//                    rowIntSet[y].remove(i);
//                    colIntSet[x].remove(i);
//                    
//                    loadMinisquareCoordinates(x, y);
//                    minisquareIntMatrix[point.y]
//                                       [point.x].remove(i);
//                }
//            }
//        }
//        
//        return false;
//    }
//    
//    void halt() {
//        solutionFound = true;
//        
//        for (final ParallelSudokuSolverThread thread : threads) {
//            if (!thread.equals(Thread.currentThread())) {
//                thread.halt();
//            }
//        }
//    }
//    
//    private void loadMinisquareCoordinates(final int x, final int y) {
//        point.x = x / minisquareIntMatrix.length;
//        point.y = y / minisquareIntMatrix.length;
//    }
//}