package chess88pckg;

import java.util.ArrayList;

/**
 * Queens is a subclass of Pieces.
 * Queens has no additional information than the Pieces class.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Queens extends Pieces
{
    /**
     * The constructor for Queens objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     */
    public Queens(char c, char t, ArrayList<String> moves, String loc)
    {
        super(c, t, moves, loc);
    }
}