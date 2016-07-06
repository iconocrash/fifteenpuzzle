/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public class SearchSolution {
    public MoveSequence solutionSequence;
    public int solutionDepth() { return solutionSequence.size(); }
    public int numTreeNodes;
    public long timeNs;
    public double timeMicrosecs() { return (double)timeNs / (double)Math.pow(10, 3); }
    public double timeMillisecs() { return (double)timeNs / (double)Math.pow(10, 6); }
    public double timeSecs() { return (double)timeNs / (double)Math.pow(10, 9); }
    public String time() {
        String s = new String();
        if ( timeSecs() > 1 )
            s = timeSecs() + "secs";
        else if ( timeMillisecs() > 1 )
            s = timeMillisecs() + "ms";
        else if ( timeMicrosecs() > 1 )
            s = timeMicrosecs() + "µs";
        else // < 1µs, ie. less than 1000 ns
            s = timeNs + "ns";
        return s;
    }
    public void printTime() {
        System.out.println(time());
    }
    public String toStringTiny() {
        return new String( "[" + solutionDepth() + "d/" + numTreeNodes + "n/" + time() + "]");
    }
    @Override
    public String toString() {
        return new String( "[depth " + solutionDepth() + ", " + numTreeNodes + " nodes, " + time() + "]");
    }
}
