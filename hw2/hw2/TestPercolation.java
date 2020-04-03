package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPercolation {

    @Test
    public void testPercolationCase0() {
        Percolation myPercolation = new Percolation(10);
        myPercolation.open(2, 4);
        assertTrue(myPercolation.isOpen(2, 4));
        assertFalse(myPercolation.isFull(2, 4));
        myPercolation.open(0, 4);
        assertTrue(myPercolation.isOpen(0, 4));
        assertTrue(myPercolation.isFull(0, 4));
        myPercolation.open(1, 4);
        assertTrue(myPercolation.isOpen(1, 4));
        assertTrue(myPercolation.isFull(1, 4));
        assertTrue(myPercolation.isFull(2, 4));

        myPercolation.open(4, 4);
        assertTrue(myPercolation.isOpen(4, 4));
        assertFalse(myPercolation.isFull(4, 4));
    }

    /** test*/
    @Test
    public void testUpdatePercolate() {
        Percolation myPercolation = new Percolation(5);
        myPercolation.open(0, 2);
        myPercolation.open(1, 2);
        myPercolation.open(2, 2);
        myPercolation.open(3, 2);
        myPercolation.open(4, 2);
        assertTrue(myPercolation.percolates());
    }
}
