/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public class FifteenPuzzleSearch {

    public static boolean verboseSearch = false;

    public static final int INVALID_DEPTH = -1;
    public static final int NO_DEPTH_LIMIT = -2;

    public static enum SearchMethod {
        BFS,
        IDLS,
        ASTAR_H1,
        ASTAR_H2
    }

    public static SearchSolution findSolution(SearchMethod search, FifteenPuzzleGame start)
    {
        SearchSolution solution = null;
        switch (search)
        {
            case BFS:
                solution = findSolutionBFS(start);
                break;
            case IDLS:
                solution = findSolutionIterativeDLS(start);
                break;
            case ASTAR_H1:
                solution = findSolutionAStarH1(start);
                break;
            case ASTAR_H2:
                solution = findSolutionAStarH2(start);
                break;
        }
        return solution;
    }

    public static SearchSolution findSolutionTreeSearch(FifteenPuzzleGame start, SearchQueue fringe,
            int depthLimit, SearchHeuristic H)
    {
        SearchSolution solution = new SearchSolution();
        solution.timeNs = System.nanoTime();

        MoveSequence moveseq = new MoveSequence();
        SearchTree tree = new SearchTree();

        SearchNode n = tree.root;

        n.state = start.clone();    // initialize root of search tree with initial game state
        n.parent = null;            // root node has no parent
        n.depth = 0;                // root has depth zero
        if (H != null)
            n.heuristic = H;        // set the search heursitic
            //newNode.f = newNode.g() + H.h(newNode);
        fringe.add(n);              // add to queue

        while (true)
        {
            if ( fringe.isEmpty() )
            {
                /* FAILURE: No solution
                   found. Return null. */
                return null;
            }

            n = fringe.remove(); // next in line

            if ( n.state.isSolution() )
            {
                /* FOUND SOLUTION:
                 * Trace back the moves we made to construct a
                 * solution move sequence.
                 */
                System.out.println("Found solution.");

                while ( n.parent != null )
                {
                    moveseq.addFirst(n.prevMove); // add to front of list since we are moving *up* the tree
                    n = n.parent;
                }

                break;
            }

            /* EXPAND / KEEP SEARCHING:
             * Expand the tree with possible moves from the
             * current node and add them to the queue.
             */

            if (depthLimit == NO_DEPTH_LIMIT) {
                // do nothing, carry on
            } else if ( n.depth < depthLimit ) {
                // do nothing carry on
            } else if ( n.depth >= depthLimit ) {
                continue; // don't expand, get the next item out of the queue
            } else {
                System.out.println("Invalid depth limit input to findSolutionTreeSearch(): depthLimit = " + depthLimit + ", n.depth = " + n.depth );
                System.exit(1); // depth limit is a negative number here
            }

            MoveList possibleMoves = n.state.findPossibleMoves();
            
            if (verboseSearch) {
                System.out.print("Expanding: \n" + n.state);
                System.out.print("Expanding from depth " + n.depth + " to " + (n.depth + 1) + "\n");
                System.out.print("Found " + possibleMoves.size() + " possible moves from this state: \n" + possibleMoves.toStringSingleLine() + "\n\n");
            }

            while ( possibleMoves.isEmpty() != true )
            {
                Move move = possibleMoves.remove();
                SearchNode newNode = new SearchNode();

                newNode.state = n.state.clone();
                
                newNode.state.performMove(move);
                newNode.prevMove = move; // save the move which led to this state

                newNode.depth = n.depth + 1;
                newNode.parent = n;
                n.children.add( newNode );
                tree.size = tree.size + 1;

                if (H != null)
                    newNode.heuristic = H;
                    //newNode.f = newNode.g() + H.h(newNode);

                fringe.add( newNode ); // add to end
            }
        }

        solution.solutionSequence = moveseq;
        solution.numTreeNodes = tree.size;
        solution.timeNs = System.nanoTime() - solution.timeNs;
        return solution;
    }

    /* Perform a breadth-first tree search to find a solution */
    public static SearchSolution findSolutionBFS(FifteenPuzzleGame start) {
        return findSolutionTreeSearch( start, new SearchQueueFIFO(), NO_DEPTH_LIMIT, null );
    }

    public static SearchSolution findSolutionDFS(FifteenPuzzleGame start) {
        return findSolutionTreeSearch( start, new SearchQueueLIFO(), NO_DEPTH_LIMIT, null );
    }

    public static SearchSolution findSolutionDLS(FifteenPuzzleGame start, int depthLimit) {
        return findSolutionTreeSearch( start, new SearchQueueLIFO(), depthLimit, null );
    }

    public static SearchSolution findSolutionIterativeDLS(FifteenPuzzleGame start)
    {
        SearchSolution solution;
        int depthLimit = 0;
        while (true)
        {
            solution = findSolutionDLS( start, depthLimit );
            if (solution != null)
                break;
            depthLimit++;
        }
        return solution;
    }

    /* delegate our heuristic */
    public interface SearchHeuristic { 
        public int h(SearchNode n);
    }

    /* Heuristic h1 calculates the number of misplaced tiles on the board as
     * an estimated distance to the solution */
    public static class H1 implements SearchHeuristic {

        public int h(SearchNode n) {
            return numMisplacedTiles(n.state, n.state.goal);
        }

        int numMisplacedTiles(FifteenPuzzleGame current, FifteenPuzzleGame goal) {
            int numMisplacedTiles = 0;
            for (int i = 0; i < FifteenPuzzleGame.NUM_ROWS; i++) {
                for (int j = 0; j < FifteenPuzzleGame.NUM_COLS; j++) {
                    if (current.board[i][j] != goal.board[i][j]) {
                        numMisplacedTiles++;
                    }
                }
            }
            return numMisplacedTiles;
        }
    }

    /* Heuristic h2 calculates the sum of the "manhattan distances" of all of the
     * tiles on the board from their current location to their goal location */
    public static class H2 implements SearchHeuristic {

        public int h(SearchNode n) {
            FifteenPuzzleGame current = n.state;
            FifteenPuzzleGame goal = n.state.goal;
            int totalDistance = 0;
            BoardLocation loc1 = new BoardLocation();
            for (int row = 0; row < FifteenPuzzleGame.NUM_ROWS; row++) {
                for (int col = 0; col < FifteenPuzzleGame.NUM_COLS; col++) {
                    loc1.row = row;
                    loc1.col = col;
                    BoardTile tile = current.getTile(loc1);
                    BoardLocation loc2 = goal.findTileLocation(tile);
                    totalDistance += manhattanDistance(loc1, loc2);
                }
            }
            return totalDistance;
        }

        int manhattanDistance(BoardLocation loc1, BoardLocation loc2) {
            int horizDist = Math.abs(loc2.row - loc1.row);
            int vertDist = Math.abs(loc2.col - loc1.col);
            return horizDist + vertDist;
        }
    }

    /* Peform an A* search with heuristic h1 */
    public static SearchSolution findSolutionAStarH1(FifteenPuzzleGame start)
    {
        return findSolutionTreeSearch( start, new SearchQueuePrioritized(), NO_DEPTH_LIMIT, new H1() );
    }

    /* Peform an A* search with heuristic h2 */
    public static SearchSolution findSolutionAStarH2(FifteenPuzzleGame start)
    {
        return findSolutionTreeSearch( start, new SearchQueuePrioritized(), NO_DEPTH_LIMIT, new H2() );
    }

}
