package chess88pckg;

import java.util.ArrayList;
import java.util.Scanner;

/** 
 * Chess is the main class that runs the entire program.
 * The class creates a chess board using a two dimensional array of Pieces objects.
 * The class also houses the methods for printing the board, updating the board,
 * recognizing a check, recognizing a checkmate, getting the available moves for all
 * remaining pieces on the board.
 * <p>
 * 
 * @author Alexander Nocciolo
 * @author Tejas Nimkar
 * @version %I%, %G%
*/
public class Chess
{
    /**
     * A variable used for turning enabling various print statements that assist
     * with debugging the code.
     */
    static boolean debug = false;
    /**
     * A variable used for determining which color has the current move.
     */
    static boolean wTurn = true;
    /**
     * The location on the board of the black king.
     */
    static String bKing;
    /**
     * The location on the board of the white king.
     */
    static String wKing;
    /**
     * A variable that keeps track of how many turns have passed, used for determining
     * if an enpassant is available for a pawn.
     */
    static int count;

    /**
     * The main method for the program.
     * This method will create the chessboard and all of the pieces.
     * It will then populate the board with the pieces and update all of the pieces to
     * have the correct information based on thier location.
     * The method will also continually take inputs for moving pieces and commit the moves
     * after determining they are legal moves.
     * Once a checkmate, resignation, or draw has been reached the method finishes and
     * the program terminates.
     * <p>
     * 
     * @param args          arguments passed in via the command line
     */
    public static void main(String[] args)
    {
        
        Pieces[][] board = new Pieces[8][8];
        //create pawns
        for(int col = 0; col < 8; col++)
        {
            int wRow = 1;
            int bRow = 6;
            String wLoc = cordToString(wRow, col);
            String bLoc = cordToString(bRow, col);
            //System.out.println("This is wLoc: " + wLoc);
            board[1][col] = new Pawns('w', 'p', new ArrayList<String>(), wLoc, false, true, 0);
            board[6][col] = new Pawns('b', 'p', new ArrayList<String>(), bLoc, false, true, 0);
        }
        //create rooks
        board[0][0] = new Rooks('w', 'R', new ArrayList<String>(), cordToString(0, 0), false);
        board[0][7] = new Rooks('w', 'R', new ArrayList<String>(), cordToString(0, 7), false);
        board[7][0] = new Rooks('b', 'R', new ArrayList<String>(), cordToString(7, 0), false);
        board[7][7] = new Rooks('b', 'R', new ArrayList<String>(), cordToString(7, 7), false);
        //create knights
        board[0][1] = new Knights('w', 'N', new ArrayList<String>(), cordToString(0, 1));
        board[0][6] = new Knights('w', 'N', new ArrayList<String>(), cordToString(0, 6));
        board[7][1] = new Knights('b', 'N', new ArrayList<String>(), cordToString(7, 1));
        board[7][6] = new Knights('b', 'N', new ArrayList<String>(), cordToString(7, 6));
        //create bishops
        board[0][2] = new Bishops('w', 'B', new ArrayList<String>(), cordToString(0, 2));
        board[0][5] = new Bishops('w', 'B', new ArrayList<String>(), cordToString(0, 5));
        board[7][2] = new Bishops('b', 'B', new ArrayList<String>(), cordToString(7, 2));
        board[7][5] = new Bishops('b', 'B', new ArrayList<String>(), cordToString(7, 5));
        //create queens
        board[0][3] = new Queens('w', 'Q', new ArrayList<String>(), cordToString(0, 3));
        board[7][3] = new Queens('b', 'Q', new ArrayList<String>(), cordToString(7, 4));
        //create kings
        board[0][4] = new Kings('w', 'K', new ArrayList<String>(), cordToString(0, 4), false);
        board[7][4] = new Kings('b', 'K', new ArrayList<String>(), cordToString(7, 3), false);
        wKing = board[0][4].location;
        bKing = board[7][3].location;
        updateBoard(board);
        printBoard(board);

        Scanner sc = new Scanner(System.in);
        count = 0;
        boolean checkPrinted = false;
        while(true)
        {
            //System.out.println("This is count: " + count);
            if(!inCheckmate(board))
            {
                System.out.println("Checkmate");
                if(wTurn)
                {
                    System.out.println("Black wins");
                }
                else 
                {
                    System.out.println("White wins");
                }
                break;
            }
            if(!checkPrinted)
            {
                inCheck(board);
                checkPrinted = true;
            }
            if(wTurn == true)
            {
                System.out.print("White's move: ");
            }
            else
            {
                System.out.print("Black's move: ");
            }
            String input = sc.nextLine();
            if(debug == true)
            {
                System.out.println();
                System.out.println("This is input: " + input);
            }
            if(input.charAt(0) == 'r' || input.charAt(0) == 'R')
            {
                String resign = input.substring(0, 6);
                if(resign.toLowerCase().equals("resign"))
                {
                    if(wTurn)
                    {
                        System.out.println("Black wins");
                    }
                    else 
                    {
                        System.out.println("White wins");
                    }
                    break;
                }
            }
            if(input.equals("draw"))
            {
                break;
            }
            String oldLoc = input.substring(0, 2);
            String newLoc;
            String extra;
            if(input.length() > 5)
            {
                newLoc = input.substring(3, 5);
                extra = input.substring(6);
            }
            else
            {
                newLoc = input.substring(3);
                extra = null;
            }
            if(debug == true)
            {
                System.out.println("This is oldLoc: " + oldLoc);
                System.out.println("This is newLoc: " + newLoc);
                System.out.println("This is extra: " + extra);
            }
            int oldRow = getRow(oldLoc);
            int oldCol = getCol(oldLoc);
            int newRow = getRow(newLoc);
            int newCol = getCol(newLoc);
            Pieces temp =  board[newRow][newCol];
            if(wTurn == true)
            {
                if(board[oldRow][oldCol] == null || board[oldRow][oldCol].color != 'w' || !board[oldRow][oldCol].availableMoves.contains(newLoc))
                {
                    System.out.println("Illegal move, try again");
                    continue;
                }
                boolean enpassantCheck = (board[newRow][newCol] == null);
                board[newRow][newCol] = board[oldRow][oldCol];
                board[oldRow][oldCol] = null;
                updateBoard(board);
                for(int rows = 0; rows < 8; rows++)
                {
                    for(int cols = 0; cols < 8; cols ++)
                    {
                        if(board[rows][cols] != null)
                        {
                            if(board[rows][cols].type == 'p')
                            {
                                Pawns current = (Pawns) board[rows][cols];
                                // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                // System.out.println("This is count: " + count);
                                // System.out.println("This is move2Count: " + current.move2Count);
                                if(current.move2FromStart && current.move2Count != count)
                                {
                                    current.move2FromStart = false;
                                }
                            }
                        }
                    }
                }
                if(!checkValid(board))
                {
                    board[oldRow][oldCol] = board[newRow][newCol];
                    board[newRow][newCol] = temp;
                    updateBoard(board);
                    for(int rows = 0; rows < 8; rows++)
                    {
                        for(int cols = 0; cols < 8; cols ++)
                        {
                            if(board[rows][cols] != null)
                            {
                                if(board[rows][cols].type == 'p')
                                {
                                    Pawns current = (Pawns) board[rows][cols];
                                    // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                    // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                    // System.out.println("This is count: " + count);
                                    // System.out.println("This is move2Count: " + current.move2Count);
                                    if(current.move2FromStart && current.move2Count != count)
                                    {
                                        current.move2FromStart = false;
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("Illegal move, try again");
                    continue;
                }
                if(board[newRow][newCol].type == 'p')
                {
                    Pawns current = (Pawns) board[newRow][newCol];
                    //enpassant
                    if(current.firstMove && newRow == 3)
                    {
                        current.move2FromStart = true;
                        current.move2Count = count;
                    }
                    current.firstMove = false;
                    if(enpassantCheck && newCol != oldCol)
                    {
                        board[newRow - 1][newCol] = null;
                    }
                    //promotion (may need to change to make new piece at pawn loc)
                    if(newRow == 7)
                    {
                        if(input.length() > 5)
                        {
                            current.type = input.charAt(6);
                        }
                        else 
                        {
                            current.type = 'Q';
                        }
                    }
                }
                if(board[newRow][newCol].type == 'K')
                {
                    Kings current = (Kings) board[newRow][newCol];
                    //castling
                    if(!current.hasMoved)
                    {
                        if(newCol == oldCol + 2)
                        {
                            board[newRow][newCol - 1] = board[0][7];
                            board[0][7] = null;
                            Rooks rook = (Rooks) board[newRow][newCol - 1];
                            rook.hasMoved = true;
                        }
                        else if(newCol == oldCol - 2)
                        {
                            board[newRow][newCol + 1] = board[0][0];
                            board[0][0] = null;
                            Rooks rook = (Rooks) board[newRow][newCol + 1];
                            rook.hasMoved = true;
                        }
                        current.hasMoved = true;
                    }
                }
                updateBoard(board);
                for(int rows = 0; rows < 8; rows++)
                {
                    for(int cols = 0; cols < 8; cols ++)
                    {
                        if(board[rows][cols] != null)
                        {
                            if(board[rows][cols].type == 'p')
                            {
                                Pawns current = (Pawns) board[rows][cols];
                                // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                // System.out.println("This is count: " + count);
                                // System.out.println("This is move2Count: " + current.move2Count);
                                if(current.move2FromStart && current.move2Count != count)
                                {
                                    current.move2FromStart = false;
                                }
                            }
                        }
                    }
                }
                wTurn = false;
                checkPrinted = false;
                printBoard(board);
                count++;
                continue;
            }
            else 
            {
                if(board[oldRow][oldCol] == null || board[oldRow][oldCol].color != 'b' || !board[oldRow][oldCol].availableMoves.contains(newLoc))
                {
                    System.out.println("Illegal move, try again");
                    continue;
                }
                boolean enpassantCheck = (board[newRow][newCol] == null);
                board[newRow][newCol] = board[oldRow][oldCol];
                board[oldRow][oldCol] = null;
                updateBoard(board);
                for(int rows = 0; rows < 8; rows++)
                {
                    for(int cols = 0; cols < 8; cols ++)
                    {
                        if(board[rows][cols] != null)
                        {
                            if(board[rows][cols].type == 'p')
                            {
                                Pawns current = (Pawns) board[rows][cols];
                                // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                // System.out.println("This is count: " + count);
                                // System.out.println("This is move2Count: " + current.move2Count);
                                if(current.move2FromStart && current.move2Count != count)
                                {
                                    current.move2FromStart = false;
                                }
                            }
                        }
                    }
                }
                if(!checkValid(board))
                {
                    board[oldRow][oldCol] = board[newRow][newCol];
                    board[newRow][newCol] = temp;
                    updateBoard(board);
                    for(int rows = 0; rows < 8; rows++)
                    {
                        for(int cols = 0; cols < 8; cols ++)
                        {
                            if(board[rows][cols] != null)
                            {
                                if(board[rows][cols].type == 'p')
                                {
                                    Pawns current = (Pawns) board[rows][cols];
                                    // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                    // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                    // System.out.println("This is count: " + count);
                                    // System.out.println("This is move2Count: " + current.move2Count);
                                    if(current.move2FromStart && current.move2Count != count)
                                    {
                                        current.move2FromStart = false;
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("Illegal move, try again");
                    continue;
                }
                if(board[newRow][newCol].type == 'p')
                {
                    Pawns current = (Pawns) board[newRow][newCol];
                    //enpassant
                    if(current.firstMove && newRow == 4)
                    {
                        current.move2FromStart = true;
                        current.move2Count = count;
                    }
                    current.firstMove = false;
                    if(enpassantCheck && newCol != oldCol)
                    {
                        board[newRow + 1][newCol] = null;
                    }
                    //promotion
                    if(newRow == 0)
                    {
                        if(input.length() > 5)
                        {
                            current.type = input.charAt(6);
                        }
                        else 
                        {
                            current.type = 'Q';
                        }
                    }
                }
                if(board[newRow][newCol].type == 'K')
                {
                    Kings current = (Kings) board[newRow][newCol];
                    //castling
                    if(!current.hasMoved)
                    {
                        if(newCol == oldCol + 2)
                        {
                            board[newRow][newCol - 1] = board[7][7];
                            board[7][7] = null;
                            Rooks rook = (Rooks) board[newRow][newCol - 1];
                            rook.hasMoved = true;
                        }
                        else if(newCol == oldCol - 2)
                        {
                            board[newRow][newCol + 1] = board[7][0];
                            board[7][0] = null;
                            Rooks rook = (Rooks) board[newRow][newCol + 1];
                            rook.hasMoved = true;
                        }
                        current.hasMoved = true;
                    }
                }
                updateBoard(board);
                for(int rows = 0; rows < 8; rows++)
                {
                    for(int cols = 0; cols < 8; cols ++)
                    {
                        if(board[rows][cols] != null)
                        {
                            if(board[rows][cols].type == 'p')
                            {
                                Pawns current = (Pawns) board[rows][cols];
                                // System.out.println("This is rows: " + rows + " This is cols: " + cols);
                                // System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                                // System.out.println("This is count: " + count);
                                // System.out.println("This is move2Count: " + current.move2Count);
                                if(current.move2FromStart && current.move2Count != count)
                                {
                                    current.move2FromStart = false;
                                }
                            }
                        }
                    }
                }
                wTurn = true;
                checkPrinted = false;
                printBoard(board);
                count++;
                continue;
            }
        }
        sc.close();
    }

    /**
     * Takes the current status of the chess board and prints it out.
     * <p>
     * 
     * @param board         the chess board
     */
    public static void printBoard(Pieces[][] board)
    {
        System.out.println();
        for(int rows = 7; rows >= 0; rows--)
        {
            for(int cols = 0; cols < 8; cols++)
            {
                if(board[rows][cols] == null)
                {
                    if((rows % 2 == 0 && cols % 2 != 0) || (rows % 2 != 0 && cols % 2 == 0))
                    {
                        System.out.print("## ");
                    }
                    else
                    {
                        System.out.print("   ");
                    }
                }
                else
                {
                    System.out.print(board[rows][cols] + " ");
                }
            }
            System.out.print(rows + 1);
            System.out.println();
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println();
    }
    
    //Used for getting available moves at any point in game
    /**
     * Takes a specific piece and the chess board and the iterates through all of
     * that pieces possible moves based on what type of move it is.
     * If a move is determined to be one that the piece can make the string location of
     * the move is added to that piece's available moves which is an ArrayList of strings.
     * The string location is based on the chess board as opposed to the array coordinates.
     * For example, the location of the bottom left of the board would be "a1" instead of
     * (0, 0).
     * The method then returns that pieces available moveset.
     * <p>
     * 
     * @param piece         the piece the needs its moveset checked
     * @param board         the chess board
     * @return              the ArrayList of strings that contains all of the available moves
     *                      for the inputted piece
     */
    public static ArrayList<String> checkMoves(Pieces piece, Pieces[][] board)
    {
        int row = getRow(piece.location);
        int col = getCol(piece.location);
        if(debug == true)
        {
            System.out.println("This is row: " + row + " This is col: " + col);
        }
        char type = piece.type;
        char color = piece.color;
        piece.availableMoves.clear();
        if(type == 'p')
        {
            if(color == 'w')
            {
                if(row == 7)
                {
                    return piece.availableMoves;
                }
                if(board[row + 1][col] == null)
                {
                    piece.availableMoves.add(cordToString(row + 1, col));
                }
                if(row == 1)
                {
                    if(board[row + 2][col] == null && board[row + 1][col] == null)
                    {
                        piece.availableMoves.add(cordToString(row + 2, col));
                    }
                }
                if(col != 0 && board[row + 1][col - 1] != null && board[row + 1][col - 1].color == 'b')
                {
                    piece.availableMoves.add(cordToString(row + 1, col - 1));
                }
                if(col != 7 && board[row + 1][col + 1] != null && board[row + 1][col + 1].color == 'b')
                {
                    piece.availableMoves.add(cordToString(row + 1, col + 1));
                }
                if(row == 4)
                {
                    Pawns right;
                    Pawns left;
                    if(col < 7 && board[row][col + 1] != null && board[row][col + 1].color != color && board[row][col + 1].type == 'p')
                    {
                        right = (Pawns) board[row][col + 1];
                        if(right.move2FromStart)
                        {
                            piece.availableMoves.add(cordToString(row + 1, col + 1));
                        }
                    }
                    if(col > 0 && board[row][col - 1] != null && board[row][col - 1].color != color && board[row][col - 1].type == 'p')
                    {
                        left = (Pawns) board[row][col - 1];
                        if(left.move2FromStart)
                        {
                            piece.availableMoves.add(cordToString(row + 1, col - 1));
                        }
                    }
                }
            }
            else
            {
                if(row == 0)
                {
                    return piece.availableMoves;
                }
                if(board[row - 1][col] == null)
                {
                    piece.availableMoves.add(cordToString(row - 1, col));
                }
                if(row == 6)
                {
                    if(board[row - 2][col] == null && board[row - 1][col] == null)
                    {
                        piece.availableMoves.add(cordToString(row - 2, col));
                    }
                }
                if(col != 0 && board[row - 1][col - 1] != null && board[row - 1][col - 1].color == 'w')
                {
                    piece.availableMoves.add(cordToString(row - 1, col - 1));
                }
                if(col != 7 && board[row - 1][col + 1] != null && board[row - 1][col + 1].color == 'w')
                {
                    piece.availableMoves.add(cordToString(row - 1, col + 1));
                }
                if(row == 3)
                {
                    Pawns right;
                    Pawns left;
                    if(col < 7 && board[row][col + 1] != null && board[row][col + 1].color != color && board[row][col + 1].type == 'p')
                    {
                        right = (Pawns) board[row][col + 1];
                        if(right.move2FromStart)
                        {
                            piece.availableMoves.add(cordToString(row - 1, col + 1));
                        }
                    }
                    if(col > 0 && board[row][col - 1] != null && board[row][col - 1].color != color && board[row][col - 1].type == 'p')
                    {
                        left = (Pawns) board[row][col - 1];
                        if(left.move2FromStart)
                        {
                            piece.availableMoves.add(cordToString(row - 1, col - 1));
                        }
                    }
                }
            }
        }
        else if(type == 'R')
        {
            int tempRow = row;
            int tempCol = col;
            while(tempRow > 0)
            {
                tempRow--;
                if(board[tempRow][col] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                }
                else if(board[tempRow][col].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            while(tempRow < 7)
            {
                tempRow++;
                if(board[tempRow][col] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                }
                else if(board[tempRow][col].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                    break;
                }
                else 
                {
                    break;
                }
            }
            while(tempCol > 0)
            {
                tempCol--;
                if(board[row][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                }
                else if(board[row][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempCol = col;
            while(tempCol < 7)
            {
                tempCol++;
                if(board[row][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                }
                else if(board[row][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
        }
        else if(type == 'N')
        {
            //up 2 left 1
            if(row + 2 < 8 && col - 1 >= 0 && (board[row + 2][col - 1] == null || board[row + 2][col - 1].color != color))
            {
                piece.availableMoves.add(cordToString(row + 2, col - 1));
            }
            //up 2 right 1
            if(row + 2 < 8 && col + 1 < 8 && (board[row + 2][col + 1] == null || board[row + 2][col + 1].color != color))
            {
                piece.availableMoves.add(cordToString(row + 2, col + 1));
            }
            //up 1 left 2
            if(row + 1 < 8 && col - 2 >= 0 && (board[row + 1][col - 2] == null || board[row + 1][col - 2].color != color))
            {
                piece.availableMoves.add(cordToString(row + 1, col - 2));
            }
            //up 1 right 2
            if(row + 1 < 8 && col + 2 < 8 && (board[row + 1][col + 2] == null || board[row + 1][col + 2].color != color))
            {
                piece.availableMoves.add(cordToString(row + 1, col + 2));
            }
            //down 2 left 1
            if(row - 2 >= 0 && col - 1 >= 0 && (board[row - 2][col - 1] == null || board[row - 2][col - 1].color != color))
            {
                piece.availableMoves.add(cordToString(row - 2, col - 1));
            }
            //down 2 right 1
            if(row - 2 >= 0 && col + 1 < 8 && (board[row - 2][col + 1] == null || board[row - 2][col + 1].color != color))
            {
                piece.availableMoves.add(cordToString(row - 2, col + 1));
            }
            //down 1 left 2
            if(row - 1 >= 0 && col - 2 >= 0 && (board[row - 1][col - 2] == null || board[row - 1][col - 2].color != color))
            {
                piece.availableMoves.add(cordToString(row - 1, col - 2));
            }
            //down 1 right 2
            if(row - 1 >= 0 && col + 2 < 8 && (board[row - 1][col + 2] == null || board[row - 1][col + 2].color != color))
            {
                piece.availableMoves.add(cordToString(row - 1, col + 2));
            }
        }
        else if(type == 'B')
        {
            int tempRow = row;
            int tempCol = col;
            //up-left
            while(tempRow < 7 && tempCol > 0)
            {
                tempRow++;
                tempCol--;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //up-right
            while(tempRow < 7 && tempCol < 7)
            {
                tempRow++;
                tempCol++;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //down-left
            while(tempRow > 0 && tempCol > 0)
            {
                tempRow--;
                tempCol--;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //down-right
            while(tempRow > 0 && tempCol < 7)
            {
                tempRow--;
                tempCol++;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
        }
        else if(type == 'Q')
        {
            int tempRow = row;
            int tempCol = col;
            //up-left
            while(tempRow < 7 && tempCol > 0)
            {
                tempRow++;
                tempCol--;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //up-right
            while(tempRow < 7 && tempCol < 7)
            {
                tempRow++;
                tempCol++;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //down-left
            while(tempRow > 0 && tempCol > 0)
            {
                tempRow--;
                tempCol--;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            //down-right
            while(tempRow > 0 && tempCol < 7)
            {
                tempRow--;
                tempCol++;
                if(board[tempRow][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                }
                else if(board[tempRow][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            tempCol = col;
            while(tempRow != 0)
            {
                tempRow--;
                if(board[tempRow][col] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                }
                else if(board[tempRow][col].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempRow = row;
            while(tempRow != 7)
            {
                tempRow++;
                if(board[tempRow][col] == null)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                }
                else if(board[tempRow][col].color != color)
                {
                    piece.availableMoves.add(cordToString(tempRow, col));
                    break;
                }
                else 
                {
                    break;
                }
            }
            while(tempCol != 0)
            {
                tempCol--;
                if(board[row][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                }
                else if(board[row][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
            tempCol = col;
            while(tempCol != 7)
            {
                tempCol++;
                if(board[row][tempCol] == null)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                }
                else if(board[row][tempCol].color != color)
                {
                    piece.availableMoves.add(cordToString(row, tempCol));
                    break;
                }
                else 
                {
                    break;
                }
            }
        }
        else
        {
            if(row < 7)
            {
                if(col > 0 && (board[row + 1][col - 1] == null || board[row + 1][col - 1].color != color))
                {
                    piece.availableMoves.add(cordToString(row + 1, col - 1));
                }
                if(board[row + 1][col] == null || board[row + 1][col].color != color)
                {
                    piece.availableMoves.add(cordToString(row + 1, col));
                }
                if(col < 7 && (board[row + 1][col + 1] == null || board[row + 1][col + 1].color != color))
                {
                    piece.availableMoves.add(cordToString(row + 1, col + 1));
                }
            }
            if(col > 0 && (board[row][col - 1] == null || board[row][col - 1].color != color))
            {
                piece.availableMoves.add(cordToString(row, col - 1));
            }
            if(col < 7 && (board[row][col + 1] == null || board[row][col + 1].color != color))
            {
                piece.availableMoves.add(cordToString(row, col + 1));
            }
            if(row > 0)
            {
                if(col > 0 && (board[row - 1][col - 1] == null || board[row - 1][col - 1].color != color))
                {
                    piece.availableMoves.add(cordToString(row - 1, col - 1));
                }
                if(board[row - 1][col] == null || board[row - 1][col].color != color)
                {
                    piece.availableMoves.add(cordToString(row - 1, col));
                }
                if(col < 7 && (board[row - 1][col + 1] == null || board[row - 1][col + 1].color != color))
                {
                    piece.availableMoves.add(cordToString(row - 1, col + 1));
                }
            }
            //Castling
            Kings current = (Kings) piece;
            for(int rows = 0; rows < 8; rows++)
            {
                for(int cols = 0; cols < 8; cols++)
                {
                    //if King is in Check, Castling not allowed
                    if(board[rows][cols] != null && board[rows][cols].availableMoves.contains(current.location))
                    {
                        return piece.availableMoves;
                    }
                }
            }
            if(current.hasMoved == false)
            {
                if(current.color == 'w')
                {
                    if(board[0][0] != null && board[0][0].type == 'R' && board[0][0].color == 'w')
                    {
                        Rooks leftR = (Rooks) board[0][0];
                        if(board[0][1] == null && board[0][2] == null && board[0][3] == null && leftR.hasMoved == false)
                        {
                            piece.availableMoves.add("c1");
                            for(int rows = 0; rows < 8; rows++)
                            {
                                for(int cols = 0; cols < 8; cols++)
                                {
                                    if(board[rows][cols] != null)
                                    {
                                        if(board[rows][cols].availableMoves.contains("d1") || board[rows][cols].availableMoves.contains("c1"))
                                        {
                                            piece.availableMoves.remove("c1");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(board[0][7] != null && board[0][7].type == 'R' && board[0][7].color == 'w')
                    {
                        Rooks RightR = (Rooks) board[0][7];
                        if(board[0][5] == null && board[0][6] == null && RightR.hasMoved == false)
                        {
                            piece.availableMoves.add("g1");
                            for(int rows = 0; rows < 8; rows++)
                            {
                                for(int cols = 0; cols < 8; cols++)
                                {
                                    //System.out.println(board[rows][cols] != null);
                                    //System.out.println(board[rows][cols].availableMoves.contains("f1"));
                                    //System.out.println(board[rows][cols].availableMoves.contains("g1"));
                                    if(board[rows][cols] != null)
                                    {
                                        if(board[rows][cols].availableMoves.contains("f1") || board[rows][cols].availableMoves.contains("g1"))
                                        {
                                            piece.availableMoves.remove("g1");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else 
                {
                    if(board[7][0] != null && board[7][0].type == 'R' && board[7][0].color == 'b')
                    {
                        Rooks leftR = (Rooks) board[7][0];
                        if(board[7][1] == null && board[7][2] == null && board[7][3] == null && leftR.hasMoved == false)
                        {
                            piece.availableMoves.add("c8");
                            for(int rows = 0; rows < 8; rows++)
                            {
                                for(int cols = 0; cols < 8; cols++)
                                {
                                    if(board[rows][cols] != null)
                                    {
                                        if(board[rows][cols].availableMoves.contains("d8") || board[rows][cols].availableMoves.contains("c8"))
                                        {
                                            piece.availableMoves.remove("c8");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(board[7][7] != null && board[7][7].type == 'R' && board[7][7].color == 'b')
                    {
                        Rooks RightR = (Rooks) board[7][7];
                        if(board[7][5] == null && board[7][6] == null && RightR.hasMoved == false)
                        {
                            piece.availableMoves.add("g8");
                            for(int rows = 0; rows < 8; rows++)
                            {
                                for(int cols = 0; cols < 8; cols++)
                                {
                                    if(board[rows][cols] != null)
                                    {
                                        if(board[rows][cols].availableMoves.contains("f8") || board[rows][cols].availableMoves.contains("g8"))
                                        {
                                            piece.availableMoves.remove("g8");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return piece.availableMoves;
    }

    /**
     * Takes a string location and returns the row coordinate for Pieces[][] board.
     * <p>
     * 
     * @param location      the string representation of a piece's location
     * @return              the integer corresponding to the row of the string location
     */
    public static int getRow(String location)
    {
        if(debug == true)
        {
            System.out.println("This is getRow location: " + location + " This is substring: " + location.substring(1));
        }
        return Integer.parseInt(location.substring(1)) - 1;
    }

    /**
     * Takes a string location and returns the column coordinate for Pieces[][] board.
     * <p>
     * 
     * @param location      the string representation of a piece's location
     * @return              the integer corresponding to the column of the string location
     */
    public static int getCol(String location)
    {
        char col = location.charAt(0);
        if(col == 'a')
        {
            return 0;
        }
        else if(col == 'b')
        {
            return 1;
        }
        else if(col == 'c')
        {
            return 2;
        }
        else if(col == 'd')
        {
            return 3;
        }
        else if(col == 'e')
        {
            return 4;
        }
        else if(col == 'f')
        {
            return 5;
        }
        else if(col == 'g')
        {
            return 6;
        }
        else
        {
            return 7;
        }
    }

    //converts array indexes into location string (EX: [0][0] == a1)
    /**
     * Takes the row and column coordinates corresponding to a location on the chessboard
     * and converts it into the string representation of the coordinates.
     * For example, the bottom left of the board is the coordinates (0, 0).
     * Passing 0 in for row and 0 in for column would return the string "a1".
     * <p>
     * 
     * @param row           the integer corresponding to the row of a board location
     * @param col           the integer corresponding to the column of a board location
     * @return              the corresponding string of the inputted row and column
     */
    public static String cordToString(int row, int col)
    {
        char column;
        if(col == 0)
        {
            column = 'a';
        }
        else if(col == 1)
        {
            column = 'b';
        }
        else if(col == 2)
        {
            column = 'c';
        }
        else if(col == 3)
        {
            column = 'd';
        }
        else if(col == 4)
        {
            column = 'e';
        }
        else if(col == 5)
        {
            column = 'f';
        }
        else if(col == 6)
        {
            column = 'g';
        }
        else
        {
            column = 'h';
        }
        return Character.toString(column) + Integer.toString(row + 1);
    }

    /**
     * Takes the chess board and updates all of the pieces with the correct information.
     * In essence the method updates each piece to have the accurate available moves
     * based on the current position of all pieces on the board.
     * <p>
     * 
     * @param board         the chess board
     */
    public static void updateBoard(Pieces[][] board)
    {
        for(int rows = 0; rows < 8; rows++)
        {
            for(int cols = 0; cols < 8; cols ++)
            {
                if(board[rows][cols] != null)
                {
                    board[rows][cols].location = cordToString(rows, cols);
                    board[rows][cols].availableMoves = checkMoves(board[rows][cols], board);
                    // if(board[rows][cols].type == 'p')
                    // {
                    //     Pawns current = (Pawns) board[rows][cols];
                    //     System.out.println("This is rows: " + rows + " This is cols: " + cols);
                    //     System.out.println("This is current.move2FromStart: " + current.move2FromStart);
                    //     System.out.println("This is count: " + count);
                    //     System.out.println("This is move2Count: " + current.move2Count);
                    //     if(current.move2FromStart && current.move2Count != count)
                    //     {
                    //         current.move2FromStart = false;
                    //     }
                    // }
                    if(board[rows][cols].type == 'K')
                    {
                        if(board[rows][cols].color == 'w')
                        {
                            wKing = board[rows][cols].location;
                        }
                        else 
                        {
                            bKing = board[rows][cols].location;
                        }
                    }
                    if(debug == true)
                    {
                        System.out.println("Loc: " + board[rows][cols].location + " Type: " + board[rows][cols].type + " Moves: " + board[rows][cols].availableMoves.toString());
                    }
                }
            }
        }
        return;
    }

    /**
     * Takes the chess board and determines if a particular move is valid.
     * If the most recent move now allows an opposing piece to attack your king
     * the method returns false, indicating that the proposed move is not valid.
     * <p>
     * 
     * @param board         the chess board
     * @return              <code>true</code> if the proposed move is valid;
     *                      <code>false</code> otherwise.
     */
    public static boolean checkValid(Pieces[][] board)
    {
        String opKing = wKing;
        if(!wTurn)
        {
            opKing = bKing;
        }
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                if(board[row][col] != null && board[row][col].availableMoves.contains(opKing))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines based on the current position of the pieces if the current color's
     * king is in check. 
     * In essence the method checks all opposing pieces' available moves
     * for the current color's king's location.
     * If the king is in check it prints out check and returns.
     * <p>
     * 
     * @param board         the chess board
     */
    public static void inCheck(Pieces[][] board)
    {
        String king = wKing;
        if(!wTurn)
        {
            king = bKing;
        }
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                if(board[row][col] != null && board[row][col].availableMoves.contains(king))
                {
                    System.out.println("Check");
                    break;
                }
            }
        }
        return;
    }

    /**
     * Determines based on the current position of the pieces if the current color's
     * king is in checkmate.
     * In essence the method finds if the current color has any possible moves that
     * would allow its king to no longer be in any opposing pieces' available moves.
     * <p>
     * 
     * @param board         the chess board
     * @return              <code>true</code> if the current color has an available move;
     *                      <code>false</code> otherwise.
     */
    public static boolean inCheckmate(Pieces[][] board)
    {
        char turn = 'w';
        boolean moveAvailable = false;
        boolean debug2 = false;
        if(!wTurn)
        {
            turn = 'b';
        }
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                if(board[row][col] != null && board[row][col].color == turn)
                {
                    Pieces current = board[row][col];
                    if(debug2)
                    {
                        System.out.println("This is row: " + row + " This is col: " + col);
                        System.out.println("This is current moves: " + current.availableMoves);
                    }
                    for(int i = 0; i < current.availableMoves.size(); i++)
                    {
                        String s = current.availableMoves.get(i);
                        if(debug2)
                        {
                            System.out.println("This is s: " + s);
                        }
                        int newRow = getRow(s);
                        int newCol = getCol(s);
                        Pieces temp = board[newRow][newCol];
                        board[newRow][newCol] = current;
                        board[row][col] = null;
                        updateBoard(board);
                        moveAvailable = checkValid(board);
                        if(debug2)
                        {
                            System.out.println("This is moveAvailable " + moveAvailable);
                        }
                        board[row][col] = current;
                        board[newRow][newCol] = temp;
                        updateBoard(board);
                        if(moveAvailable)
                        {
                            break;
                        }
                    }
                    if(moveAvailable)
                    {
                        break;
                    }
                }
            }
            if(moveAvailable)
            {
                break;
            }
        }
        return moveAvailable;
    }
}