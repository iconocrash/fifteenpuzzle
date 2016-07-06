/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public class Move {
    
    public BoardLocation from = new BoardLocation();
    public BoardLocation to = new BoardLocation();
    public Move(BoardLocation from, BoardLocation to)
    {
        /* copy (don't assign) the locations
           to create a new move */
        this.from = from.clone();
        this.to = to.clone();
    }
    public Move getReverseMove()
    {
        return new Move(this.to, this.from);
    }
    @Override
    public String toString()
    {
        return "[Row " + from.getNormalizedRow() + ", Col " + from.getNormalizedCol() + " --> " + "Row " + to.getNormalizedRow() + ", Col " + to.getNormalizedCol() + "]";
    }
    public boolean equals(Move move)
    {
        if (from.equals(move.from) && to.equals(move.to))
            return true;
        else
            return false;
    }

}
