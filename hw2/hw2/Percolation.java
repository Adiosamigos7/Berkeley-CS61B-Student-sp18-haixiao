package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF cells;
    private WeightedQuickUnionUF cells2;
    private int[] isopen;
    private int opennum;
    private int dimension;
    private int xyTo1D(int row, int col) {
        return row * dimension + col + 1;
    }
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        dimension = N;
        cells = new WeightedQuickUnionUF(N * N + 2);
        cells2 = new WeightedQuickUnionUF(N * N + 1);
        isopen = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            isopen[i] = 0;
        }
        opennum = 0;
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= dimension || col >= dimension) {
            throw new IndexOutOfBoundsException();
        }
        int celnum = xyTo1D(row, col);
        if (!isOpen(row, col)) {
            isopen[celnum - 1] = 1;
            opennum += 1;
            if (row > 0) {
                if (isOpen(row - 1, col)) {
                    cells.union(celnum, celnum - dimension);
                    cells2.union(celnum, celnum - dimension);
                }
            } else {
                cells.union(celnum, 0);
                cells2.union(celnum, 0);
            }
            if (row < dimension - 1) {
                if (isOpen(row + 1, col)) {
                    cells.union(celnum, celnum + dimension);
                    cells2.union(celnum, celnum + dimension);
                }
            } else {
                cells.union(celnum, dimension * dimension + 1);
            }
            if (col > 0 && isOpen(row, col - 1)) {
                cells.union(celnum, celnum - 1);
                cells2.union(celnum, celnum - 1);
            }
            if (col < dimension - 1 && isOpen(row, col + 1)) {
                cells.union(celnum, celnum + 1);
                cells2.union(celnum, celnum + 1);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        return isopen[xyTo1D(row, col) - 1] == 1;
    }
    public boolean isFull(int row, int col) {
        int celnum = xyTo1D(row, col);
        return cells2.connected(celnum, 0);
    }
    public int numberOfOpenSites() {
        return opennum;
    }
    public boolean percolates() {
        return cells.connected(0, dimension * dimension + 1);
    }
    public static void main(String[] args) {

    }
}
