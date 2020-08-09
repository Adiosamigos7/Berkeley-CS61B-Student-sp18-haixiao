package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;


public class Percolation {
    private WeightedQuickUnionUF cells;
    private int[] isopen;
    private int[] percolate;
    private int opennum;
    private int dimension;
    private boolean isPercolate;
    private int xyTo1D(int row, int col) {
            return row * dimension + col + 1;
    }
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        dimension = N;
        isPercolate = false;
        cells = new WeightedQuickUnionUF(N * N + 2);
        percolate = new int[N * N + 2];
        isopen = new int[N * N + 2];
        percolate[1] = 0;
        for (int i = 1; i < N * N + 1; i++) {
            percolate[i] = 0;
            isopen[i] = 0;
        }
        opennum = 0;
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= dimension || col >= dimension) {
            throw new IndexOutOfBoundsException();
        }
        int celnum = xyTo1D(row, col);
        isopen[celnum] = 1;
        if (row > 0) {
            if (isOpen(row - 1, col)) {
                cells.union(celnum, celnum - dimension);
            }
        } else {
            cells.union(celnum, 0);
        }
        if (row < dimension - 1) {
            if (isOpen(row + 1, col)) {
                cells.union(celnum, celnum + dimension);
            }
        } else {
            if (!isPercolate) {
                cells.union(celnum, dimension * dimension + 1);
            }
        }
        if (col > 0 && isOpen(row, col - 1)) {
            cells.union(celnum, celnum - 1);
        }
        if (col< dimension - 1 && isOpen(row, col + 1)) {
            cells.union(celnum, celnum + 1);
        }
        opennum += 1;
    }

    public boolean isOpen(int row, int col) {
        return isopen[xyTo1D(row, col)] == 1;
    }
    public boolean isFull(int row, int col) {
        int celnum = xyTo1D(row,col);
//        if (cells.connected(celnum, 0)) {
//            percolate[celnum] = 1;
//        }
        return cells.connected(celnum, 0);
    }
    public int numberOfOpenSites() {
        return opennum;
    }
    public boolean percolates() {
        if (!isPercolate) {
            isPercolate = cells.connected(0, dimension * dimension + 1);
        }
        return isPercolate;
    }
  //  public static void main(String[] args)   // use for unit testing (not required)
}