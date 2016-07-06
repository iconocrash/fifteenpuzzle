/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author JJ
 */
public class MoveList extends LinkedList<Move> {

    @Override
    public String toString()
    {
        return toStringMultiLine();
    }

    public String toStringMultiLine()
    {
        String s = new String();
        Iterator<Move> iterator = iterator();
        while (iterator.hasNext())
        {
            Move m = iterator.next();
            s = s + m.toString() + "\n";
        }
        return s;
    }

    public String toStringSingleLine() {
        String s = new String();
        Iterator<Move> iterator = iterator();
        Move m;

        /* Get the first element */
        if (iterator.hasNext())
        {
            m = iterator.next();
            s = s + m;
        }
        else
            return "";

        /* Get the rest */
        while (iterator.hasNext())
        {
            m = iterator.next();
            s = s + ", "+ m;
        }

        return s;
    }

}
