package hw2;

import org.junit.Test;

public class TestPercolationStats {

    @Test
    public void testPercolationStats() {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats myPercolationStats = new PercolationStats(3, 2, pf);
        System.out.println("Percolation mean: " + myPercolationStats.mean());
        System.out.println("Percolation standard variation:" + myPercolationStats.stddev());
        System.out.println("95% confidence threshold: [" + myPercolationStats.confidenceLow() + "," + myPercolationStats.confidenceHigh() + "]");
    }
}
