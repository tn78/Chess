package chess88pckg;

import java.util.ArrayList;

/**
 * Pieces is the abstract base class for all pieces contexts.
 * For any piece it contains the color of the piece, the type of piece it is,
 * the list of available moves for the piece, and the location on the board of the piece.
 * The class also has a method for printing out a Pieces object.
 * <p>
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public abstract class Pieces
{
    /**
     * This is the color of a piece.
     */
    char color;
    /**
     * This is the type of a piece.
     */
    char type;
    /**
     * This is the list of moves that a piece has based on its board position and
     * the position of other pieces.
     */
    ArrayList<String> availableMoves;
    /**
     * This is the location of the piece on the chess board.
     */
    String location;
    
    /**
     * The constructor for Pieces objects.
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     */
    public Pieces(char c, char t, ArrayList<String> moves, String loc)
    {
        this.color = c;
        this.type = t;
        this.availableMoves = moves;
        this.location = loc;
    }

    /**
     * Used for printing out Pieces objects.
     * Prints the color and type of a specific piece, in that order.
     */
    public String toString()
    {
        return Character.toString(this.color) + Character.toString(this.type);
    }
}