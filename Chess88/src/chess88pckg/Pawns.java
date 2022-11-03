package chess88pckg;

import java.util.ArrayList;

/**
 * Pawns is a subclass of Pieces.
 * In addition to the information provided from Pieces, it also contains information for
 * determining if a pawn has moved two spaces on its first turn, if a pawn's move was its
 * first, and a count on what turn the pawn moved two spaces.
 * These are used for determining if enpassant is a valid move.
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
 */
public class Pawns extends Pieces
{
    /**
     * Used for keeping track of whether a pawn has moved two spaces on its first turn.
     */
    boolean move2FromStart;
    /**
     * Used for keeping track of whether a pawn is on its first move or not.
     */
    boolean firstMove;
    /**
     * Used for keeping track on what turn a pawn has made a two step advance.
     */
    int move2Count;
    /**
     * The constructor for Pawns objects.
     * 
     * @param c         the color of a piece
     * @param t         the type of a piece
     * @param moves     the list of available moves for a piece
     * @param loc       the location of a piece on the chess board
     * @param move2     whether a pawn has made a two step advance
     * @param moveF     whether a pawn is on its first move
     * @param count     what turn a pawn made a two step advance
     */
    Pawns(char c, char t, ArrayList<String> moves, String loc, boolean move2, boolean moveF, int count)
    {
        super(c, t, moves, loc);
        this.move2FromStart = move2;
        this.firstMove = moveF;
        this.move2Count = count;
    }
}
