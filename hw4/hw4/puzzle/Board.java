package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int dimension;
    private int[][] board;
    private int moves;
    private Board prev;
    private int BLANK = 0;

    public Board(int[][] tiles) {
        dimension = tiles.length;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = tiles[i][j];
            }
        }
        moves = 0;
        prev = null;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i >= dimension || j >= dimension) {
            throw new IndexOutOfBoundsException();
        }
        return board[i][j];
    }

    public int size() {
        return dimension;
    }

    /**
     * Returns neighbors of this board.
     * @Source: Josh Hug.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int wrong = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != i * dimension + j + 1) {
                    if (!(board[i][j] == BLANK)) {
                        wrong += 1;
                    }
                }
            }
        }
        return wrong;
    }

    public int manhattan() {
        int man = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int x = board[i][j];
                if (x != 0) {
                    man += Math.abs((board[i][j] - 1) / dimension - i)
                            + Math.abs((board[i][j] - 1) % dimension - j);
                }
            }
        }
        return man;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan() + hamming();
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board x = (Board) y;
        if (size() != x.size()) {
            return false;
        }
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.board[i][j] != x.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
