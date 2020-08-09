package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] isPercolate;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        PercolationFactory f = new PercolationFactory();
        isPercolate = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation x = f.make(N);
            while(!x.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                x.open(row, col);
            }
            isPercolate[i] = x.numberOfOpenSites() / N / N;
        }
    }
    public double mean() {
        return StdStats.mean(isPercolate);
    }
    public double stddev() {
        return StdStats.stddevp(isPercolate);
    }
    public double confidenceLow() {
        return mean() + 1.96 * stddev() / isPercolate.length;
    }
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / isPercolate.length;
    }
}
