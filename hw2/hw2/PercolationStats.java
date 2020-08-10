package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] isPercolate;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        isPercolate = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation x = pf.make(N);
            while (!x.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                x.open(row, col);
            }
            isPercolate[i] = (double) x.numberOfOpenSites() / (double) (N * N);
        }
    }
    public double mean() {
        return StdStats.mean(isPercolate);
    }
    public double stddev() {
        return StdStats.stddev(isPercolate);
    }
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(isPercolate.length);
    }
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(isPercolate.length);
    }

    /*
    public static void main(String[] args) {
        int T = 10;
        int N = 20;
        PercolationStats t = new PercolationStats(N, T, new PercolationFactory());
        System.out.println("mean: " + t.mean());
        System.out.println("stdevp: " + t.stddev());
        System.out.println("confidence: " + t.confidenceLow() + ", " + t.confidenceHigh());
    }*/
}
