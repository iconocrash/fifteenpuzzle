/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.util.LinkedList;

/**
 *
 * @author JJ
 */
public class SearchNode implements Comparable<SearchNode> {

    public SearchNode parent = null;
    public LinkedList<SearchNode> children = new LinkedList<SearchNode>();

    int depth = FifteenPuzzleSearch.INVALID_DEPTH;  // initialize with an invalid (negative) number

    public FifteenPuzzleGame state = null;  // the state of the game
    public Move prevMove = null;            // the move that brought the game to this state

    // TODO: save h rather than recalculating it each time the priority queues is being reordered
    FifteenPuzzleSearch.SearchHeuristic heuristic = null;   // delegate our search heuristic
    public int g() { return depth; }                // number of moves it took to get here (exact cost)
    public int h() { return heuristic.h(this); }    // estimated cost of getting to goal from here
    public int f() { return g() + h(); }            // f() = g() + h(), the total estimated cost to goal
    public int compareTo(SearchNode n) {
        return this.f() - n.f();                    // allows f() ordering in the priority queue
    }
    
}
