package chess88pckg;

import java.util.ArrayList;
/**
 * Rooks is a subclass of Pieces. 
 * In addition to what has been inherited from Pieces, Rooks also contain information
 * on whether or not a rook piece has moved.
 * This is used for determining if castling with a given rook is possible.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Rooks extends Pieces
{
    /**
     * Used for keeping track of whether or not a rook has moved.
     */
    boolean hasMoved;

    /**
     * The constructor for Rooks objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     * @param moved     whether or not a rook has moved
     */
    public Rooks(char c, char t, ArrayList<String> moves, String loc, boolean moved)
    {
        super(c, t, moves, loc);
        this.hasMoved = moved;
    }
}