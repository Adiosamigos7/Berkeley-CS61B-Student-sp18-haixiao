package hw4.puzzle;


import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;


public class Solver {

    private int moves;
    private Stack<WorldState> solution = new Stack<WorldState>();

    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode prev;
        private int moves;
        private WorldState state;
        private int estDist;

        public SearchNode(WorldState s, int m, SearchNode p) {
            state = s;
            prev = p;
            moves = m;
            estDist = s.estimatedDistanceToGoal();
        }


        @Override
        public int compareTo(SearchNode a) {
            return this.moves + this.estDist
                    - a.moves - a.estDist;
        }
    }


    public Solver(WorldState initial) {
        moves = -1;
        SearchNode initNode = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> worlds = new MinPQ<SearchNode>();

        worlds.insert(initNode);
        while (!worlds.isEmpty()) {
            SearchNode world = worlds.delMin();
            if (world.state.isGoal()) {
                while (world != null) {
                    solution.push(world.state);
                    moves += 1;
                    world = world.prev;
                }
                return;
            }
            for (WorldState neighbor : world.state.neighbors()) {
                SearchNode n = new SearchNode(neighbor, world.moves + 1, world);
                if (!neighbor.equals(world.state) || n.compareTo(world) < 0) {
                    if (world.prev != null) {
                        if (!neighbor.equals(world.prev.state) || n.compareTo(world.prev) < 0) {
                            worlds.insert(n);
                        }
                    } else {
                        worlds.insert(n);
                    }
                }
            }
        }

    }
    public int moves() {
        return moves;
    }
    public Iterable<WorldState> solution() {
        return solution;
    }
}
