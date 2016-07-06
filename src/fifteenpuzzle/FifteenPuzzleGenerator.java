/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author JJ
 */
public class FifteenPuzzleGenerator {

    static int SEED_1 = 42;
    static int SEED_2 = 51;
    static int SEED_3 = 101;
    static int SEED_4 = 303;
    static int SEED_5 = 808;
    static int RANDOM_SEED = (int)System.currentTimeMillis();

    static Random random = new Random( RANDOM_SEED );

    public static void setSeed(int seed) {
        random = new Random(seed);
        System.out.println("Puzzle generator random seed set to " + seed);
    }
    
    public static FifteenPuzzleGame generatePuzzleUserInput()
    {
        System.out.println("Enter values 1 through 15 for each location on the board.");
        System.out.println("Enter each value only once.");
        System.out.println("Enter 0 for the empty space.");
        System.out.println();
        FifteenPuzzleGame game = new FifteenPuzzleGame();
        BoardLocation loc = new BoardLocation();
        for (int i = 0; i < FifteenPuzzleGame.NUM_ROWS; i++) {
            loc.row = i;
            System.out.println("[Row " + loc.getNormalizedRow() + "]");
            for (int j = 0; j < FifteenPuzzleGame.NUM_COLS; j++) {
                loc.col = j;
                System.out.print("Enter row " + loc.getNormalizedRow() + ", column " + loc.getNormalizedCol() + ": ");
                int inputInt = Console.getIntConsoleInputWithBounds(0, 15);
                game.setTile( BoardTile.getTileFromValue(inputInt), loc );
            }
            System.out.println();
        }

        if ( game.hasNoRepeats() == false ) {
            System.out.println("Board has repeats. Try again.");
            System.out.println();
            return generatePuzzleUserInput();
        }

        return game;
    }

    /* Generate a puzzle with the given solution depth */
    public static FifteenPuzzleGame generatePuzzle(int depth) {
        FifteenPuzzleGame game = FifteenPuzzleGame.goal.clone(); // depth 0
        Move prevMove = null;
        for (int i = 1; i <= depth; i++) // i = 1, 2, 3, ... , depth
        {
            prevMove = generateNextDepthVerified(depth, game, prevMove);
            if ( prevMove == null ) // failed to produce puzzle at given depth due to running out of memory verify depth
                return null;
        }
        return game;
    }

    public static Move generateNext(int desiredDepth, FifteenPuzzleGame game, Move prevMove)
    {
        Move m;
        MoveList possibleMoves = game.findPossibleMoves();

        /* If there was not a previous move simply choose a random move */
        if ( prevMove == null )
            m = possibleMoves.get( random.nextInt( possibleMoves.size() ) );

        /* Otherwise choose a random move that won't send
           us back to the previous state. */
        else {
            Move dontDoThis = prevMove.getReverseMove(); // don't go back to the previous state
            while (true) {
                if ( possibleMoves.isEmpty() ) {
                    System.out.println("All moves do not lead to a higher depth. Performing random move.");
                    m = game.findPossibleMoves().get(random.nextInt(possibleMoves.size()));
                    // could try calling itself after this too to try to get desired depth (perform move here and return)
                    break;
                }
                int index = random.nextInt( possibleMoves.size() );
                m = possibleMoves.get( index );
                possibleMoves.remove(index);
                System.out.println("Trying to obtain next puzzle with next depth...");
                if ( m.equals( dontDoThis ) )
                    continue;
                else
                    break;
            }
        }

        //System.out.println("...verfied next depth puzzle obtained.");

        game.performMove(m);
        return m;
    }

    /* Peform a random move to *try* to make the solution depth of a game one
     * level deeper. Returns the move made to do so. The parameter prevMove is
     * used to make sure the random move chosen is will not lead back to the
     * previous game state. Note that a sequence of moves can still lead to a
     * previous state--and hence a lower solution depth--such as by "rotating"
     * the empty position in a "loop" a number of times.
     */
    public static Move generateNextDepthAttempt(FifteenPuzzleGame game, Move prevMove)
    {
        Move m;
        MoveList possibleMoves = game.findPossibleMoves();
        
        /* If there was not a previous move simply choose a random move */
        if ( prevMove == null )
            m = possibleMoves.get( random.nextInt( possibleMoves.size() ) );

        /* Otherwise choose a random move that won't send
           us back to the previous state. */
        else {
            Move dontDoThis = prevMove.getReverseMove(); // don't go back to the previous state
            do {
                m = possibleMoves.get( random.nextInt( possibleMoves.size() ) );
                System.out.println("Trying to obtain puzzle with next verified depth...");
            } while ( m.equals( dontDoThis ) );
        }

        System.out.println("...verfied next depth puzzle obtained.");
        
        game.performMove(m);
        return m;
    }

    /* Try to generate a puzzle that has a +1 solution depth relative to
     * the "game" input. Verify depth with A*h1 or A*h2
     */
    public static Move generateNextDepthVerified(int depth, FifteenPuzzleGame game, Move prevMove)
    {
        Move m = null;
        boolean h1Failed = false;

        /* Try using heuristic 1, if that fails try heuristic 2 */

        try {
            m = generateNextDepthVerifiedH1(depth, game, prevMove);
        } catch (OutOfMemoryError oome) {
            System.out.println("Ran out memory using A*h1 to verify depth!");
            h1Failed = true;
        }

        System.gc();

        /* If A*h1 fails try A*h2 */
        
        if (h1Failed) {
            try {
                m = generateNextDepthVerifiedH2(depth, game, prevMove);
            } catch (OutOfMemoryError oome) {
                System.out.println("Ran out memory using A*h2 to verify depth!");
                return null;  /* can't reach next depth verifiably */
            }
        }

        return m;

    }

    public static Move generateNextDepthVerifiedH1(int depth, FifteenPuzzleGame game, Move prevMove) {
        Move m;
        while (true) {
            m = generateNextDepthAttempt(game, prevMove);
            if ( depth == FifteenPuzzleSearch.findSolutionAStarH1(game).solutionDepth() )
                break;
            prevMove = m;
        }
        return m;
    }

    public static Move generateNextDepthVerifiedH2(int depth, FifteenPuzzleGame game, Move prevMove) {
        Move m;
        while (true) {
            m = generateNextDepthAttempt(game, prevMove);
            if ( depth == FifteenPuzzleSearch.findSolutionAStarH2(game).solutionDepth() )
                break;
            prevMove = m;
        }
        return m;
    }

    static FifteenPuzzleGame generateRandomPuzzle() {

        LinkedList<BoardTile> tiles = new LinkedList<BoardTile>();
        FifteenPuzzleGame game = new FifteenPuzzleGame();
        BoardLocation loc = new BoardLocation();

        BoardTile[] tilesArray = BoardTile.class.getEnumConstants(); // get all the board tile constants

        /* convert array to linked list */
        for (int i = 0; i < tilesArray.length; i++)
            tiles.add(tilesArray[i]);

        /* for each location on the board, randomly remove a tile from
           our tile list and add it to the location */
        for (int i = 0; i < FifteenPuzzleGame.NUM_ROWS; i++) {
            loc.setArrayRow(i);
            for(int j = 0; j < FifteenPuzzleGame.NUM_COLS; j++) {
                loc.setArrayCol(j);
                int index = random.nextInt( tiles.size() );
                BoardTile tile = tiles.get(index);
                tiles.remove(index);
                game.setTile( tile , loc );
            }
        }

        return game;

    }
    
}
