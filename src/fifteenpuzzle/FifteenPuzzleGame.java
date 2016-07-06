/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author JJ
 */
public class FifteenPuzzleGame implements Serializable {

    public static final int NUM_ROWS = 4;
    public static final int NUM_COLS = 4;
    public static final int BOARD_SIZE = NUM_ROWS * NUM_COLS;

    BoardTile[][] board = new BoardTile[NUM_ROWS][NUM_COLS];

    public static final BoardTile[][] goalBoard = {
        { BoardTile.EMPTY_TILE,  BoardTile.PIECE_1,  BoardTile.PIECE_2,  BoardTile.PIECE_3 },
        { BoardTile.PIECE_4,     BoardTile.PIECE_5,  BoardTile.PIECE_6,  BoardTile.PIECE_7 },
        { BoardTile.PIECE_8,     BoardTile.PIECE_9,  BoardTile.PIECE_10, BoardTile.PIECE_11 },
        { BoardTile.PIECE_12,    BoardTile.PIECE_13, BoardTile.PIECE_14, BoardTile.PIECE_15 } };

    public static final FifteenPuzzleGame goal = new FifteenPuzzleGame( goalBoard );

    public FifteenPuzzleGame() {

    }

    private FifteenPuzzleGame(BoardTile[][] board) {
        for (int i = 0; i < NUM_ROWS; i++)
            for (int j = 0; j < NUM_COLS; j++)
                this.board[i][j] = board[i][j];
    }

    public BoardTile getTile(BoardLocation location) {
        return board[ location.getArrayRow() ][ location.getArrayCol() ];
    }

    public void setTile(BoardTile tile, BoardLocation location) {
        board[ location.getArrayRow() ][ location.getArrayCol() ] = tile;
    }

    public BoardLocation findTileLocation(BoardTile tile)
    {
        BoardLocation loc = new BoardLocation();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (board[i][j] == tile) {
                    loc.setArrayRow(i);
                    loc.setArrayCol(j);
                    return loc;
                }
            }
        }
        return null;
    }

    public BoardLocation findEmptyTileLocation()
    {
        return findTileLocation(BoardTile.EMPTY_TILE);
    }

    public void performMove(Move move)
    {
        BoardLocation from = move.from;
        BoardLocation to = move.to;
        if ( getTile(to) != BoardTile.EMPTY_TILE )
        {
            System.out.println("Invalid move received (does not go to empty tile). Exiting.");
            System.exit(1);
        }
        /* swap the "to" and "from" tiles */
        BoardTile temp = board[to.row][to.col]; // copy the "to" value in a temp variable
        board[to.row][to.col] = board[from.row][from.col]; // copy the "from" value into the "to" (destination) location
        board[from.row][from.col] = temp; // copy the original "to" value into the "from" location
    }

    public void performMoveSequence(MoveSequence movesequence)
    {
        Iterator<Move> iterator = movesequence.iterator();
        while (iterator.hasNext())
        {
            Move m = iterator.next();
            performMove(m);
        }
    }

    public MoveList findPossibleMoves()
    {
        /* Find the empty location */

        BoardLocation emptyLocation = findEmptyTileLocation();

        /* From where can a piece move into the empty space? */

        boolean fromLeft = true, fromRight = true,
                fromAbove = true, fromBelow = true;

        if (emptyLocation.col == 0) fromLeft = false;
        if (emptyLocation.col == 3) fromRight = false;
        if (emptyLocation.row == 0) fromAbove = false;
        if (emptyLocation.row == 3) fromBelow = false;

        /* Now build the list of moves we can make */

        MoveList moves = new MoveList();

        Move move;
        BoardLocation from = new BoardLocation();
        BoardLocation to = new BoardLocation();

        // we always move into the empty space
        to.row = emptyLocation.row;
        to.col = emptyLocation.col;

        if (fromLeft)
        {
            from.row = emptyLocation.row;
            from.col = emptyLocation.col - 1;
            move = new Move(from, to);
            moves.add( move );
        }

        if (fromRight)
        {
            from.row = emptyLocation.row;
            from.col = emptyLocation.col + 1;
            move = new Move(from, to);
            moves.add( move );
        }

        if (fromAbove)
        {
            from.row = emptyLocation.row - 1;
            from.col = emptyLocation.col;
            move = new Move(from, to);
            moves.add( move );
        }

        if (fromBelow)
        {
            from.row = emptyLocation.row + 1;
            from.col = emptyLocation.col;
            move = new Move(from, to);
            moves.add( move );
        }

        return moves;
    }

    /* Test to see if the board is valid */
    public boolean isValid()
    {
        /* A board is valid if all the values are in bounds and
           there are no repeats. Since we are using the BoardTile
           enum the values cannot be out of bounds. Thus, we
           just need to check that there are no repeats. */

        return hasNoRepeats();
    }

    public boolean hasNoRepeats()
    {
        /* Make sure each value 0...15 is found
           once and only once (no repeats) */
        boolean[] found = new boolean[16];

        // init all to false
        for (int i = 0; i < found.length; i++)
            found[i] = false;

        for (int i = 0; i < BOARD_SIZE; i++)
        {
            BoardLocation loc = new BoardLocation();
            loc.setArrayIndex( i );

            int boardTileValue = getTile(loc).getValue();

            if ( found[ boardTileValue ] == false )
                found[ boardTileValue ] = true; // found
            else // values[ boardPieceValue ] == true ... already found
                return false; // repeated value, invalid
        }

        return true;
    }

    /* Is the board state a valid solution? */
    public boolean isSolution()
    {
        return this.equals(goal);
    }

    public boolean equals(FifteenPuzzleGame game)
    {
        for (int i = 0; i < NUM_ROWS; i++)
            for (int j = 0; j < NUM_COLS; j++)
                if ( this.board[i][j] != game.board[i][j] ) return false;
        return true;
    }

    @Override
    public FifteenPuzzleGame clone()
    {
        FifteenPuzzleGame gameCopy = new FifteenPuzzleGame();
        for (int i = 0; i < NUM_ROWS; i++)
            for (int j = 0; j < NUM_COLS; j++)
                gameCopy.board[i][j] = board[i][j];
        //gameCopy.goal = goal; // reference copy since the goal state is not modified
        return gameCopy;
    }

    @Override
    public String toString()
    {
        String s = new String();
        for (int i = 0; i < NUM_ROWS; i++)
        {
            s = s + "Row " + (i+1) + ": ";
            for (int j = 0; j < NUM_COLS; j++)
                s = s + board[i][j] + "\t";
            s = s + "\n";
        }
        return s;
    }

}
