package lab11.graphs;
import java.util.PriorityQueue;
import java.util.Queue;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        marked[s] = true;
        announce();

        if (s == t) {
            return;
        }

        Queue<Integer> fringe = new PriorityQueue<Integer>();

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
                    if (w == t) {
                        return;
                    }
                }
            }
        }

    }


    @Override
    public void solve() {
        bfs();
    }
}

