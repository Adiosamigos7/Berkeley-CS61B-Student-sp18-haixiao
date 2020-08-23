package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int source;
    private int target;
    private boolean targetFound = false;
    private Maze maze;
    PriorityQueue<Integer> fringe = new PriorityQueue<Integer>(new SortbyAstar());

    private class SortbyAstar implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return h(a) + distTo[a] - h(b) - distTo[b];
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        distTo[source] = 0;
        edgeTo[source] = source;

    }

    private int[] oneDToxy(int m) {
        int[] xy = new int[2];
        xy[0] = m / maze.N() + 1;
        xy[1] = m % maze.N() + 1;
        return xy;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int[] vPos = oneDToxy(v);
        int[] tPos = oneDToxy(target);
        return Math.abs(vPos[0] - tPos[0]) + Math.abs(vPos[1] - tPos[1]);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {

        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        announce();

        if (s == target) {
            return;
        }

        fringe.add(s);
        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    fringe.add(w);
                    edgeTo[w] = v;
                    announce();
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    if (w == target) {
                        return;
                    }
                }
            }
        }


    }

    @Override
    public void solve() {
        astar(source);
    }

}

