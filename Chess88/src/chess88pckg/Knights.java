package chess88pckg;

import java.util.ArrayList;
/**
 * Knights is a subclass of Pieces.
 * Knights has no additional information than the Pieces class.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Knights extends Pieces
{
    /**
     * The constructor for Knights objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     */
    public Knights(char c, char t, ArrayList<String> moves, String loc)
    {
        super(c, t, moves, loc);
    }
}
