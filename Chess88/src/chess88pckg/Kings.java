package chess88pckg;

import java.util.ArrayList;

/**
 * Kings is a subclass of Pieces. 
 * In addition to what has been inherited from Pieces, Kings also contain information
 * on whether or not a king piece has moved.
 * This is used for determining if castling is possible.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Kings extends Pieces
{
    /**
     * Used for keeping track of whether or not the king has moved.
     */
    boolean hasMoved;

    /**
     * The constructor for Kings objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     * @param moved     whether or not the king has moved
     */
    public Kings(char c, char t, ArrayList<String> moves, String loc, boolean moved)
    {
        super(c, t, moves, loc);
        this.hasMoved = moved;
    }
}