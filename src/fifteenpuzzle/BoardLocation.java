/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

/**
 *
 * @author JJ
 */
public class BoardLocation {

    /* Internally the row and column values are base-zero,
     * corresponding the board array.
     */

    protected int row;
    protected int col;

    public int getArrayRow() { return row; }
    public int getArrayCol() { return col; }
    public void setArrayRow(int row) { this.row = row; }
    public void setArrayCol(int col) { this.col = col; }

    public int getArrayIndex() { return row * FifteenPuzzleGame.NUM_ROWS + col; }
    public void setArrayIndex(int index) {
        this.row = index / FifteenPuzzleGame.NUM_ROWS;
        this.col = index % FifteenPuzzleGame.NUM_ROWS;
    }

    public int getNormalizedRow() { return row + 1; }
    public int getNormalizedCol() { return col + 1; }
    public void setNormalizedRow(int row) { this.row = row - 1; }
    public void setNormalizedCol(int col) { this.col = col - 1; }

    @Override
    public BoardLocation clone() {
        BoardLocation loc = new BoardLocation();
        loc.row = this.row;
        loc.col = this.col;
        return loc;
    }
    public boolean equals(BoardLocation loc) {
        if (this.row == loc.row && this.col == loc.col)
            return true;
        else
            return false;
    }

}
