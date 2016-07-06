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
public class SearchQueueLIFO extends LinkedList<SearchNode> implements SearchQueue {

    @Override
    public boolean add(SearchNode node) {
        addFirst(node); // add to the front of the queue instead of the end
        return true;    // as per the contract of Collection.add
    }
    
}
