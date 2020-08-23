package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s = 0;
    private boolean cycleFound = false;
    private Maze maze;


    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        distTo[s] = 0;
        edgeTo[s] = s;
    }



    @Override
    public void solve() {
        searchCycle(0);
    }

    // Helper methods go here.
    private void searchCycle(int v) {

        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (marked[w]) {
                if (edgeTo[v] != w) {
                    return;
                }
            } else {
                edgeTo[w] = v;
                announce();
                distTo[w] = distTo[v] + 1;
                searchCycle(w);
            }
        }
    }
}

