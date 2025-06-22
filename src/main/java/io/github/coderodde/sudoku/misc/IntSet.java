package io.github.coderodde.sudoku.misc;

import java.util.Arrays;

/**
 * This class implements a simple set data structures for small integers.
 * 
 * @version 1.0.0 (Dec 4, 2024)
 * @since 1.0.0 (Dec 4, 2024)
 */
public final class IntSet {
    
    /**
     * The actual data array.
     */
    private final boolean[] data;
    
    /**
     * Constructs a new integer set that can accommodate {@code dataCapacity}
     * elements.
     * 
     * @param capacity data array capacity.
     */
    public IntSet(final int capacity) {
        this.data = new boolean[capacity];
    }
    
    /**
     * Adds the integer {@code integer} to this integer set.
     * 
     * @param integer the integer to set.
     */
    public void add(final int integer) {
        data[integer] = true;
    }
    
    /**
     * Queries whether the integer {@code integer} is in this integer set.
     * 
     * @param integer the integer to query for inclusion.
     * 
     * @return {@code true} if and only if the integer {@code integer} is in 
     *         this integer set.
     */
    public boolean contains(final int integer) {
        return data[integer];
    }
    
    /**
     * Removes the integer {@code integer} from this integer set.
     * 
     * @param integer the integer to remove from this integer set.
     */
    public void remove(final int integer) {
        data[integer] = false;
    }
    
    /**
     * Removes all integers from this integer set.
     */
    public void clear() {
        Arrays.fill(data, false);
    }
}
