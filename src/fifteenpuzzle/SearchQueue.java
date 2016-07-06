/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public interface SearchQueue {

    public boolean add(SearchNode node);
    public SearchNode remove();
    public boolean isEmpty();

}
