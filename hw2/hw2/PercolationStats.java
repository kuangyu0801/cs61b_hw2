package hw2;

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.introcs.StdStats.*;
import static edu.princeton.cs.introcs.StdRandom.*;

/**
 * Repeat the following until the system percolates:
 * 1.Choose a site uniformly at random among all blocked sites.
 * 2.Open the site.
 * 3.The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.
*/
public class PercolationStats {
    private static final double CONFIDENT_CONSTANT = 1.96;
    private Percolation perc;
    private double[] thredhold;
    private int size;
    private int iteration;
    private int totalSite;
    private double mean;
    private double stddev;
    private double confidenceLow;
    private double confidenceHigh;

    private int toRow(int index) {
        return index / size;
    }

    private int toCol(int index) {
        return index % size;
    }

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if( N < 0 || T < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        size = N;
        iteration = T;
        thredhold = new double[iteration];
        totalSite = N * N;

        for (int i = 0; i < iteration; i += 1) {
            perc = pf.make(N);
            while (!perc.percolates()) {
                int randomIndex = uniform(totalSite);
                perc.open(toRow(randomIndex), toCol(randomIndex));
            }
            thredhold[i] = (double) perc.numberOfOpenSites() / totalSite;
        }
        mean = StdStats.mean(thredhold);
        stddev = StdStats.stddev(thredhold);
        confidenceLow = mean - CONFIDENT_CONSTANT * stddev / Math.sqrt(iteration);
        confidenceHigh = mean + CONFIDENT_CONSTANT * stddev / Math.sqrt(iteration);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return confidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return confidenceHigh;
    }

}
