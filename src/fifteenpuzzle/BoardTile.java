/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public enum BoardTile {
    EMPTY_TILE,
    PIECE_1,
    PIECE_2,
    PIECE_3,
    PIECE_4,
    PIECE_5,
    PIECE_6,
    PIECE_7,
    PIECE_8,
    PIECE_9,
    PIECE_10,
    PIECE_11,
    PIECE_12,
    PIECE_13,
    PIECE_14,
    PIECE_15;

    public int getValue()
    {
        return ordinal();
    }

    public static BoardTile getTileFromValue(int value)
    {
        BoardTile[] tiles = BoardTile.class.getEnumConstants();
        return tiles[value];
    }

    @Override
    public String toString()
    {
        return Integer.toString(getValue());
    }
}