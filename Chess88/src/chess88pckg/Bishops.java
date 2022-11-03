package chess88pckg;

import java.util.ArrayList;

/**
 * Bishops is a subclass of Pieces.
 * Bishops has no additional information than the Pieces class.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Bishops extends Pieces
{
    /**
     * The constructor for Bishops objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     */
    public Bishops(char c, char t, ArrayList<String> moves, String loc)
    {
        super(c, t, moves, loc);
    }
}