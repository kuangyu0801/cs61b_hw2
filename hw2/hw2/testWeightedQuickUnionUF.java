package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.Test;

import static org.junit.Assert.*;


public class testWeightedQuickUnionUF {

    @Test
    public void testWQUBasic() {
        WeightedQuickUnionUF WQU = new WeightedQuickUnionUF(10);
        WQU.union(0, 7);
        assertEquals(WQU.count(), 9);
    }
}
