import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

/*
	This class can be used as a starting point for creating your Chess game project. The only piece that 
	has been coded is a white pawn...a lot done, more to do!
*/

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
    int startX;
    int startY;
    int initialX;
    int initialY;
    JPanel panels;
    JLabel pieces;

    Boolean validMove = false;
    String pieceName;
    Boolean success;
    Boolean turn = true;
    AIAgent agent = new AIAgent();
    String rand = "rand";


    public ChessProject() {
        Dimension boardSize = new Dimension(600, 600);

        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane 
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground(i % 2 == 0 ? Color.white : Color.gray);
            else
                square.setBackground(i % 2 == 0 ? Color.gray : Color.white);
        }

        // Setting up the Initial Chess board.
        for (int i = 8; i < 16; i++) {
            pieces = new JLabel(new ImageIcon("WhitePawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(0);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(1);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(6);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
        panels = (JPanel) chessBoard.getComponent(2);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
        panels = (JPanel) chessBoard.getComponent(5);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKing.png"));
        panels = (JPanel) chessBoard.getComponent(3);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
        panels = (JPanel) chessBoard.getComponent(4);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(7);
        panels.add(pieces);
        for (int i = 48; i < 56; i++) {
            pieces = new JLabel(new ImageIcon("BlackPawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(56);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(57);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(62);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishup.png"));
        panels = (JPanel) chessBoard.getComponent(58);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishup.png"));
        panels = (JPanel) chessBoard.getComponent(61);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKing.png"));
        panels = (JPanel) chessBoard.getComponent(59);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackQueen.png"));
        panels = (JPanel) chessBoard.getComponent(60);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(63);
        panels.add(pieces);
    }

    /*
        This method checks if there is a piece present on a particular square.
    */
    private Boolean piecePresent(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        if (c instanceof JPanel) {
            return false;
        } else {
            return true;
        }
    }

    /*
        This is a method to check if a piece is a Black piece.
    */
    private Boolean checkWhiteOponent(int newX, int newY) {
        Boolean oponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("Black")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }

    /*
        This method is called when we press the Mouse. So we need to find out what piece we have
        selected. We may also not have selected a piece!
    */
    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel)
            return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel) c;
        initialX = e.getX();
        initialY = e.getY();
        startX = (e.getX() / 75);
        startY = (e.getY() / 75);
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
        chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
    }
     
 	/*
		This method is used when the Mouse is released...we need to make sure the move was valid before 
		putting the piece back on the board.
	*/

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;

        chessPiece.setVisible(false);
        success = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        String tmp = chessPiece.getIcon().toString();
        pieceName = tmp.substring(0, (tmp.length() - 4));
        validMove = false;

        if (pieceName.contains("white")) {
            switch (pieceName) {
                case "WhitePawn" -> whitePawn(e);
                case "WhiteKnight" -> whiteKnight(e);
                case "WhiteKing" -> whiteKing(e);
                case "WhiteBishup" -> whiteBishup(e);
                case "WhiteRook" -> whiteRook(e);
                case "WhiteQueen" -> {
                    whiteRook(e);
                    if (!validMove) {
                        whiteBishup(e);
                    }
                }
            }
        } else {
            switch (pieceName) {
                case "BlackPawn" -> blackPawn(e);
                case "BlackKnight" -> whiteKnight(e);
                case "BlackKing" -> whiteKing(e);
                case "BlackBishup" -> whiteBishup(e);
                case "BlackRook" -> whiteRook(e);
                case "BlackQueen" -> {
                    whiteRook(e);
                    if (!validMove) {
                        whiteBishup(e);
                    }
                }
            }
        }

        if (!validMove) {
            int location = 0;
            if (startY == 0) {
                location = startX;
            } else {
                location = (startY * 8) + startX;
            }
            String pieceLocation = pieceName + ".png";
            pieces = new JLabel(new ImageIcon(pieceLocation));
            panels = (JPanel) chessBoard.getComponent(location);
            panels.add(pieces);
        } else {
            if ((success) && (pieceName.contains("White"))) {
                int location = 56 + (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
            } else if ((success) && (pieceName.contains("Black"))) {
                int location = 56 + (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
            } else {
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(chessPiece);
                } else {
                    Container parent = (Container) c;
                    parent.add(chessPiece);
                }
                chessPiece.setVisible(true);
            }
            makeAIMove();
        }
    }

    public void whitePawn(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (startY == 1) {
            System.out.println("if white pawn is in the first position");
            if ((startX == (e.getX() / 75)) && ((((e.getY() / 75) - startY) == 1) || ((e.getY() / 75) - startY) == 2)) {
                System.out.println("if white pawn moves one or two Y coordinates from the first position");
                if ((((e.getY() / 75) - startY) == 2)) {
                    System.out.println("if white pawn moves two Y coordinates from the first position");
                    if ((!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() - 75)))) {
                        System.out.println("if there is no pieces in the two Y coordinates the white pawn is moving through");
                        validMove = true;
                    } else {
                        System.out.println("if there is a piece in either of the two Y coordinates the white pawn is moving through");
                        validMove = false;
                    }
                } else {
                    System.out.println("If white pawn moves one Y coordinate from the first position");
                    if ((!piecePresent(e.getX(), (e.getY())))) {
                        System.out.println("If there is no piece in the Y coordinate the white pawn is moving to");
                        validMove = true;
                    } else {
                        System.out.println("If there is a piece in the Y coordinate the white pawn is moving to");
                        validMove = false;
                    }
                }
            } else if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7))) && (newY - startY == 1) || ((newX == (startX - 1)) && (startX - 1 >= 0)))) && (newY - startY == 1)) {
                System.out.println("If there is a black piece in taking distance from the white pawn in the first position");
                if (checkWhiteOponent(e.getX(), e.getY())) {
                    System.out.println("If the piece is a black piece the white pawn can take it");
                    validMove = true;
                } else {
                    System.out.println("If the piece is a white piece the white pawn can't take it");
                    validMove = false;
                }
            } else {
                System.out.println("If the white pawn meets none of the conditions listed it's a invalid move");
                validMove = false;
            }
        } else {
            if ((startX - 1 >= 0) || (startX + 1 <= 7)) {
                System.out.println("If the white pawn is in the second position");
                if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7))) && (newY - startY == 1) || ((newX == (startX - 1)) && (startX - 1 >= 0)))) && (newY - startY == 1)) {
                    System.out.println("If the coordinates the white pawn is moving to has a piece");
                    if (checkWhiteOponent(e.getX(), e.getY())) {
                        System.out.println("If the piece is a black piece");
                        validMove = true;
                        if (startY == 6) {
                            success = true;
                        }
                    } else {
                        System.out.println("If the piece is a white piece");
                        validMove = false;
                    }
                } else {
                    System.out.println("If there is no piece in the coordinates the white pawn is moving to");
                    if (!piecePresent(e.getX(), (e.getY()))) {
                        System.out.println("If the change in the Y coordinate for the white pawn is 1 and the X coordinate is the same");
                        if ((startX == (e.getX() / 75)) && ((e.getY() / 75) - startY) == 1) {
                            if (startY == 6) {
                                success = true;
                            }
                            validMove = true;
                        } else {
                            System.out.println("If the change in the Y coordinate for the white pawn isn't 1 or the X coordinate isn't the same");
                            validMove = false;
                        }
                    } else {
                        System.out.println("If there is a piece in the coordinates the white pawn is moving to");
                        validMove = false;
                    }
                }
            } else {
                System.out.println("If the move isn't on the board");
                validMove = false;
            }
        }
    }

    public void blackPawn(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (startY == 6) {
            System.out.println("if the black pawn is in the first position");
            if ((startX == newX) && (e.getY() / 75 - startY == -1) || (e.getY() / 75 - startY == -2)) {
                System.out.println("If the black pawn moves one or two Y coordinates from the first position");
                if (newY - startY == -2) {
                    System.out.println("If the black pawn moves two Y coordinates from the first position");
                    if ((!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() + 75)))) {
                        System.out.println("If there is no piece present in the two Y coordinates the black pawn is moving through");
                        validMove = true;
                    } else {
                        System.out.println("If there is a piece present in the two Y coordinates the black pawn is moving through");
                        validMove = false;
                    }
                } else {
                    System.out.println("If the black pawn moves one Y coordinate from the first position");
                    if ((!piecePresent(e.getX(), (e.getY())))) {
                        System.out.println("If there is no piece in the Y coordinate the black pawn is moving to");
                        validMove = true;
                    } else {
                        System.out.println("If there is a piece in the Y coordinate the black pawn is moving to");
                        validMove = false;
                    }
                }
            } else if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7))) && (newY - startY == -1) || ((newX == (startX - 1)) && (startX - 1 >= 0)))) && (newY - startY == -1)) {
                System.out.println("If there is a piece that is reachable from the first position");
                if (!checkWhiteOponent(e.getX(), e.getY())) {
                    System.out.println("If that piece is a white piece");
                    validMove = true;
                } else {
                    validMove = false;
                }
            } else {
                System.out.println("If that piece is not a white piece");
                validMove = false;
            }
        } else {
            System.out.println("If the black pawn is in the second position");
            if ((startX - 1 >= 0) || (startX + 1 <= 7)) {
                System.out.println("if the black pawn moves to a coordinate on the board");
                if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7))) && (newY - startY == -1) || ((newX == (startX - 1)) && (startX - 1 >= 0)))) && (newY - startY == -1)) {
                    System.out.println("if there is a piece in the coordinates that the black pawn is moving to");
                    if (!checkWhiteOponent(e.getX(), e.getY())) {
                        System.out.println("If the piece is not a black piece");
                        validMove = true;
                        if (startY == 6) {
                            success = true;
                        }
                    } else {
                        System.out.println("If the piece is a black piece");
                        validMove = false;
                    }
                } else {
                    System.out.println("if there is no piece in the coordinates that the black pawn is moving to");
                    if (!piecePresent(e.getX(), (e.getY()))) {
                        if ((startX == (e.getX() / 75)) && ((e.getY() / 75) - startY) == -1) {
                            System.out.println("if the X coordinate stays the same and the Y coordinate changes by 1");
                            if (startY == 6) {
                                success = true;
                            }
                            validMove = true;
                        } else {
                            System.out.println("if the X coordinate doesn't stay the same or the Y coordinate changes by more or less than 1");
                            validMove = false;
                        }
                    } else {
                        System.out.println("If there is a piece where the black pawn is trying to move to");
                        validMove = false;
                    }
                }
            } else {
                System.out.println("If the black pawn doesn't meet any of the other conditions");
                validMove = false;
            }
        }
    }

    public void whiteKnight(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if ((startX + 2 == newX && startY + 1 == newY) || (startX + 2 == newX && startY - 1 == newY) || (startX - 2 == newX && startY + 1 == newY) || (startX - 2 == newX && startY - 1 == newY) || (startX + 1 == newX && startY + 2 == newY) || (startX + 1 == newX && startY - 2 == newY) || (startX - 1 == newX && startY + 2 == newY) || (startX - 1 == newX && startY - 2 == newY)) {
            System.out.println("If the piece moves in one of the valid knight movements");
            if ((!piecePresent(e.getX(), (e.getY())))) {
                System.out.println("If there is no piece in the coordinate the knight is moving to");
                validMove = true;
            } else {
                System.out.println("if there is a piece where the knight is moving to");
                if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    System.out.println("if the piece in the coordinate the knight is moving to is black and the piece name contains white that we're moving the result is true");
                    validMove = true;
                } else if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    System.out.println("If the piece in the coordinate the knight is moving to is not black and the piece name contains black that we're moving the result is true");
                    validMove = true;
                } else {
                    System.out.println("If there is a piece where the knight is moving and it meets neither condition");
                    validMove = false;
                }
            }
        } else {
            System.out.println("if the knight movement meets no condition");
            validMove = false;
        }
    }

    public void whiteKing(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if ((startX == newX && startY + 1 == newY) || (startX == newX && startY - 1 == newY) || (startX + 1 == newX && startY == newY) || (startX - 1 == newX && startY == newY) || (startX + 1 == newX && startY + 1 == newY) || (startX - 1 == newX && startY - 1 == newY) || (startX - 1 == newX && startY + 1 == newY) || (startX + 1 == newX && startY - 1 == newY)) {
            System.out.println("If the king meets one of the valid move conditions");
            if ((!piecePresent(e.getX(), (e.getY())))) {
                System.out.println("Ìf there is no piece present for the landing coordinates that move is valid");
                validMove = true;
            } else {
                System.out.println("If there is a piece present for the landing coordinates");
                if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    System.out.println("if that piece is black and the piece we're moving is white it's a valid move ");
                    validMove = true;
                } else if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    System.out.println("if that piece is white and the piece we're moving is black it's a valid move");
                    validMove = true;
                } else {
                    System.out.println("if there is a piece where the king is moving and it meets neither condition");
                    validMove = false;
                }
            }
        } else {
            System.out.println("if the king movement meets no condition");
            validMove = false;
        }
    }

    public void whiteBishup(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        int value = startX - newX;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        if (value < 0) {
            value = value * -1;
        }
        //convert start X and Y to strings, add them together = start position x = 1, y = 1
        //convert end X and Y to strings, add them together = end position
        //take away end from start
        int position = Integer.parseInt(Integer.toString(newX) + Integer.toString(newY))
                - Integer.parseInt(Integer.toString(startX) + Integer.toString(startY));

        System.out.println(value);
        for (int i = 1; i <= value; i++) {
            System.out.println(i);
            //down and to the left
            if ((position % 9 == 0) && (newX < startX)) {
                System.out.println("If the newX coordinate is less than the starX coordinate and the coordinate change combination is divisible by 9");
                if (piecePresent((startX - i) * 75, (startY + i) * 75)) {
                    System.out.println("if there is a piece present between the starting coordinate and the finish coordinate");
                    if ((i == value) && (checkWhiteOponent((startX - i) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is black and the piece we're moving is white valid move");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX - i) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is white and the piece we're moving is black valid move");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("if the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("If there is no piece present");
                    validMove = true;
                }
            }

            //up and to the right
            else if ((position % 9 == 0) && (newX > startX)) {
                System.out.println("If the newX coordinate is greater than the starX coordinate and the coordinate change combination is divisible by 9");
                if (piecePresent((startX + i) * 75, (startY - i) * 75)) {
                    System.out.println("if there is a piece present between the starting coordinate and the finish coordinate");
                    if ((i == value) && (checkWhiteOponent((startX + i) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is black and the piece we're moving is white valid move");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX + i) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is white and the piece we're moving is black valid move");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("if the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("If there is no piece present");
                    validMove = true;
                }
            }

            //down and to the right
            else if ((position % 11 == 0) && (newX > startX)) {
                System.out.println("If the newX coordinate is lesser than the starX coordinate and the coordinate change combination is divisible by 11");
                if (piecePresent((startX + i) * 75, (startY + i) * 75)) {
                    System.out.println("if there is a piece present between the starting coordinate and the finish coordinate");
                    if ((i == value) && (checkWhiteOponent((startX + i) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is black and the piece we're moving is white valid move");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX + i) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is white and the piece we're moving is black valid move");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("if the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }

                } else {
                    System.out.println("If there is no piece present");
                    validMove = true;
                }
            }

            //up and to the left
            else if ((position % 11 == 0) && (newX < startX)) {
                System.out.println("If the newX coordinate is lesser than the starX coordinate and the coordinate change combination is divisible by 11");
                if (piecePresent((startX - i) * 75, (startY - i) * 75)) {
                    System.out.println("if there is a piece present between the starting coordinate and the finish coordinate");
                    if ((i == value) && (checkWhiteOponent((startX - i) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is black and the piece we're moving is white valid move");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX - i) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("if the movement is equal to the coordinate of the piece and the piece is white and the piece we're moving is black valid move");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("if the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("if there isn't a piece where the bishop is moving");
                    validMove = true;
                }
            } else {
                System.out.println("if none of the above conditions are met it's a false move");
                validMove = false;
                break;
            }
        }
        //statement to catch if a move is outside the loop (was occurring when moving up and down the Y axis)
        if (value == 0) {
            validMove = false;
        }

    }

    public void whiteRook(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        int value = (newX - startX) + (newY - startY);

        if (value < 0) {
            value = value * -1;
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        for (int i = 1; i <= value; i++) {
            //going left on the x axis
            if ((newX < startX) && (newY == startY)) {
                System.out.println("If the rook moves to a new X coordinate that's less than the start X and the new Y is equal to the start Y");
                if (piecePresent((startX - i) * 75, (startY) * 75)) {
                    System.out.println("If there is a piece present in any of the squares between the starting coordinate and the new coordinate");
                    if ((i == value) && (checkWhiteOponent((startX - i) * 75, (startY) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is black and the piece we're moving is white");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX - i) * 75, (startY) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is white and the piece we're moving is black");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("If the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("if there is no piece present");
                    validMove = true;
                }
            }

            //going right on the x axis
            else if ((newX > startX) && (newY == startY)) {
                System.out.println("If the rook moves to a new X coordinate that's greater than the start X and the new Y is equal to the start Y");
                if (piecePresent((startX + i) * 75, (startY) * 75)) {
                    System.out.println("If there is a piece present in any of the squares between the starting coordinate and the new coordinate");
                    if ((i == value) && (checkWhiteOponent((startX + i) * 75, (startY) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is black and the piece we're moving is white");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX + i) * 75, (startY) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is white and the piece we're moving is black");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("If the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("if there is no piece present");
                    validMove = true;
                }
            }

            //moving back on the y axis
            else if ((newX == startX) && (newY < startY)) {
                System.out.println("If the rook moves to a new Y coordinate that's less than the start Y coordinate and the X coordinate is the same as the start coordinate");
                if (piecePresent((startX) * 75, (startY - i) * 75)) {
                    System.out.println("If there is a piece present in any of the squares between the starting coordinate and the new coordinate");
                    if ((i == value) && (checkWhiteOponent((startX) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is black and the piece we're moving is white");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is white and the piece we're moving is black");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("If the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("if there is no piece present");
                    validMove = true;
                }
            }

            //moving forward on the y axis
            else if ((newX == startX) && (newY > startY)) {
                System.out.println("If the rook moves to a new Y coordinate that's greater than the start Y coordinate and the X coordinate is the same as the start coordinate");
                if (piecePresent((startX) * 75, (startY + i) * 75)) {
                    System.out.println("If there is a piece present in any of the squares between the starting coordinate and the new coordinate");
                    if ((i == value) && (checkWhiteOponent((startX) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is black and the piece we're moving is white");
                        validMove = true;
                        break;
                    } else if ((i == value) && (!checkWhiteOponent((startX) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        System.out.println("If the movement is equal to the coordinate of the piece and the piece in that coordinate is white and the piece we're moving is black");
                        validMove = true;
                        break;
                    } else {
                        System.out.println("If the movement isn't equal to the coordinate of the piece or it doesn't meet either condition above");
                        validMove = false;
                        break;
                    }
                } else {
                    System.out.println("if there is no piece present");
                    validMove = true;
                }
            } else {
                validMove = false;
                break;
            }
        }
        if (value == 0) {
            validMove = false;
        }
    }

    public String getPieceName(int x, int y) {
        return pieceName;
    }

    //checking if the pieces around me aren't a black king piece
    //wtf is the point of that?
    private Boolean checkSurroundingSquares(Square s) {
        Boolean possible = false;
        int x = s.getXC() * 75;
        int y = s.getYC() * 75;
        if (!((getPieceName((x + 75), y).contains("BlackKing")) || (getPieceName((x - 75), y).contains("BlackKing")) || (getPieceName(x, (y + 75)).contains("BlackKing")) || (getPieceName((x), (y - 75)).contains("BlackKing")) || (getPieceName((x + 75), (y + 75)).contains("BlackKing")) || (getPieceName((x - 75), (y + 75)).contains("BlackKing")) || (getPieceName((x + 75), (y - 75)).contains("BlackKing")) || (getPieceName((x - 75), (y - 75)).contains("BlackKing")))) {
            possible = true;
        }
        return possible;
    }

    private Stack getWhitePawnSquares(int x, int y, String piece) {
        Stack moves = new Stack();
        Square startingSquare = new Square(x, y, piece);
        //one in front, one to the front and to the left, one to the front and to the right, two to the front
        Move validM, validM2, validM3, validM4;
        //possible direction movements
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y + 2;
        //if the new x cord isn't great than 7
        if (!((tmpx1 > 7))) {
            //x position stays the same, y position plus 1
            Square tmp = new Square(x, tmpy1, piece);
            //x position plus 1, y position plus 1
            Square tmp1 = new Square(tmpx1, tmpy1, piece);
            //x position minus 1, y position plus 1
            Square tmp2 = new Square(tmpx2, tmpy1, piece);
            //x position stays the same, y position plus 2
            Square tmp3 = new Square(x, tmpy2, piece);

            if (startY == 6) {
                //move forward one piece
                if (checkSurroundingSquares(tmp)) {
                    validM = new Move(startingSquare, tmp);
                    if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                        moves.push(validM);
                    }
                }
                //move forward two pieces
                if (checkSurroundingSquares(tmp3)) {
                    validM3 = new Move(startingSquare, tmp3);
                    if ((!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) && (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 150) + 20))))) {
                        moves.push(validM3);
                    }
                }
            } else{
                //moving forward one piece
                if (checkSurroundingSquares(tmp)) {
                    validM = new Move(startingSquare, tmp);
                    if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                        moves.push(validM);
                    }
                }
                //moving forward one piece and to the right if there is a piece there
                if (checkSurroundingSquares(tmp1)) {
                    validM2 = new Move(startingSquare, tmp1);
                    if (!piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                        //would consider it a valid move if there is no piece there, can't take this if out at the moment, breaks code
                    } else {
                        if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                            moves.push(validM2);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private Stack getKingSquares(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y - 1;

        if (!((tmpx1 > 7))) {
            Square tmp = new Square(tmpx1, y, piece);
            Square tmp1 = new Square(tmpx1, tmpy1, piece);
            Square tmp2 = new Square(tmpx1, tmpy2, piece);
            if (checkSurroundingSquares(tmp)) {
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                        moves.push(validM);
                    }
                }
            }
            if (!(tmpy1 > 7)) {
                if (checkSurroundingSquares(tmp1)) {
                    validM2 = new Move(startingSquare, tmp1);
                    if (!piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                            moves.push(validM2);
                        }
                    }
                }
            }
            if (!(tmpy2 < 0)) {
                if (checkSurroundingSquares(tmp2)) {
                    validM3 = new Move(startingSquare, tmp2);
                    if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        System.out.println("The values that we are going to be looking at are : " + ((tmp2.getXC() * 75) + 20) + " and the y value is : " + ((tmp2.getYC() * 75) + 20));
                        if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                            moves.push(validM3);
                        }
                    }
                }
            }
        }
        if (!((tmpx2 < 0))) {
            Square tmp3 = new Square(tmpx2, y, piece);
            Square tmp4 = new Square(tmpx2, tmpy1, piece);
            Square tmp5 = new Square(tmpx2, tmpy2, piece);
            if (checkSurroundingSquares(tmp3)) {
                validM = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                        moves.push(validM);
                    }
                }
            }
            if (!(tmpy1 > 7)) {
                if (checkSurroundingSquares(tmp4)) {
                    validM2 = new Move(startingSquare, tmp4);
                    if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (checkWhiteOponent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                            moves.push(validM2);
                        }
                    }
                }
            }
            if (!(tmpy2 < 0)) {
                if (checkSurroundingSquares(tmp5)) {
                    validM3 = new Move(startingSquare, tmp5);
                    if (!piecePresent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        if (checkWhiteOponent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20)))) {
                            moves.push(validM3);
                        }
                    }
                }
            }
        }
        Square tmp7 = new Square(x, tmpy1, piece);
        Square tmp8 = new Square(x, tmpy2, piece);
        if (!(tmpy1 > 7)) {
            if (checkSurroundingSquares(tmp7)) {
                validM2 = new Move(startingSquare, tmp7);
                if (!piecePresent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (checkWhiteOponent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    }
                }
            }
        }
        if (!(tmpy2 < 0)) {
            if (checkSurroundingSquares(tmp8)) {
                validM3 = new Move(startingSquare, tmp8);
                if (!piecePresent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (checkWhiteOponent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    }
                }
            }
        }
        return moves;
    } // end of the method getKingSquares()

    private Stack getRookMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
  /*
    There are four possible directions that the Rook can move to:
      - the x value is increasing
      - the x value is decreasing
      - the y value is increasing
      - the y value is decreasing

    Each of these movements should be catered for. The loop guard is set to incriment up to the maximun number of squares.
    On each iteration of the first loop we are adding the value of i to the current x coordinate.
    We make sure that the new potential square is going to be on the board and if it is we create a new square and a new potential
    move (originating square, new square).If there are no pieces present on the potential square we simply add it to the Stack
    of potential moves.
    If there is a piece on the square we need to check if its an opponent piece. If it is an opponent piece its a valid move, but we
    must break out of the loop using the Java break keyword as we can't jump over the piece and search for squares. If its not
    an opponent piece we simply break out of the loop.

    This cycle needs to happen four times for each of the possible directions of the Rook.
  */
        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y;
            if (!(tmpx > 7 || tmpx < 0)) {
                Square tmp = new Square(tmpx, tmpy, piece);
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int j = 1; j < 8; j++) {
            int tmpx1 = x - j;
            int tmpy1 = y;
            if (!(tmpx1 > 7 || tmpx1 < 0)) {
                Square tmp2 = new Square(tmpx1, tmpy1, piece);
                validM2 = new Move(startingSquare, tmp2);
                if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), ((tmp2.getYC() * 75) + 20))) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int k = 1; k < 8; k++) {
            int tmpx3 = x;
            int tmpy3 = y + k;
            if (!(tmpy3 > 7 || tmpy3 < 0)) {
                Square tmp3 = new Square(tmpx3, tmpy3, piece);
                validM3 = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (checkWhiteOponent(((tmp3.getXC() * 75) + 20), ((tmp3.getYC() * 75) + 20))) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int l = 1; l < 8; l++) {
            int tmpx4 = x;
            int tmpy4 = y - l;
            if (!(tmpy4 > 7 || tmpy4 < 0)) {
                Square tmp4 = new Square(tmpx4, tmpy4, piece);
                validM4 = new Move(startingSquare, tmp4);
                if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if (checkWhiteOponent(((tmp4.getXC() * 75) + 20), ((tmp4.getYC() * 75) + 20))) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        return moves;
    }// end of get Rook Moves

    private Stack getBishopMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
  /*
    The Bishop can move along any diagonal until it hits an enemy piece or its own piece
    it cannot jump over its own piece. We need to use four different loops to go through the possible movements
    to identify possible squares to move to. The temporary squares, i.e. the values of x and y must change by the
    same amount on each iteration of each of the loops.

    If the new values of x and y are on the board, we create a new square and a new move (from the original square to the new
    square). We then check if there is a piece present on the new square:
    - if not we add the move as a possible new move
    - if there is a piece we make sure that we can capture our opponents piece and we cannot take our own piece
      and then we break out of the loop

    This process is repeated for each of the other three possible diagonals that the Bishop can travel along.

  */
        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y + i;
            if (!(tmpx > 7 || tmpx < 0 || tmpy > 7 || tmpy < 0)) {
                Square tmp = new Square(tmpx, tmpy, piece);
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } // end of the first for Loop
        for (int k = 1; k < 8; k++) {
            int tmpk = x + k;
            int tmpy2 = y - k;
            if (!(tmpk > 7 || tmpk < 0 || tmpy2 > 7 || tmpy2 < 0)) {
                Square tmpK1 = new Square(tmpk, tmpy2, piece);
                validM2 = new Move(startingSquare, tmpK1);
                if (!piecePresent(((tmpK1.getXC() * 75) + 20), (((tmpK1.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (checkWhiteOponent(((tmpK1.getXC() * 75) + 20), ((tmpK1.getYC() * 75) + 20))) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } //end of second loop.
        for (int l = 1; l < 8; l++) {
            int tmpL2 = x - l;
            int tmpy3 = y + l;
            if (!(tmpL2 > 7 || tmpL2 < 0 || tmpy3 > 7 || tmpy3 < 0)) {
                Square tmpLMov2 = new Square(tmpL2, tmpy3, piece);
                validM3 = new Move(startingSquare, tmpLMov2);
                if (!piecePresent(((tmpLMov2.getXC() * 75) + 20), (((tmpLMov2.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (checkWhiteOponent(((tmpLMov2.getXC() * 75) + 20), ((tmpLMov2.getYC() * 75) + 20))) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the third loop
        for (int n = 1; n < 8; n++) {
            int tmpN2 = x - n;
            int tmpy4 = y - n;
            if (!(tmpN2 > 7 || tmpN2 < 0 || tmpy4 > 7 || tmpy4 < 0)) {
                Square tmpNmov2 = new Square(tmpN2, tmpy4, piece);
                validM4 = new Move(startingSquare, tmpNmov2);
                if (!piecePresent(((tmpNmov2.getXC() * 75) + 20), (((tmpNmov2.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if (checkWhiteOponent(((tmpNmov2.getXC() * 75) + 20), ((tmpNmov2.getYC() * 75) + 20))) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the last loop
        return moves;
    }

    private Stack getQueenMoves(int x, int y, String piece) {
        Stack completeMoves = new Stack();
        Stack tmpMoves = new Stack();
        Move tmp;
  /*
      The Queen is a pretty easy piece to figure out if you have completed the
      Bishop and the Rook movements. Either the Queen is going to move like a
      Bishop or its going to move like a Rook, so all we have to do is make a call to both of these
      methods.
  */
        tmpMoves = getRookMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        tmpMoves = getBishopMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        return completeMoves;
    }

    private Stack getKnightMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Stack attackingMove = new Stack();
        Square s = new Square(x + 1, y + 2, piece);
        moves.push(s);
        Square s1 = new Square(x + 1, y - 2, piece);
        moves.push(s1);
        Square s2 = new Square(x - 1, y + 2, piece);
        moves.push(s2);
        Square s3 = new Square(x - 1, y - 2, piece);
        moves.push(s3);
        Square s4 = new Square(x + 2, y + 1, piece);
        moves.push(s4);
        Square s5 = new Square(x + 2, y - 1, piece);
        moves.push(s5);
        Square s6 = new Square(x - 2, y + 1, piece);
        moves.push(s6);
        Square s7 = new Square(x - 2, y - 1, piece);
        moves.push(s7);

        for (int i = 0; i < 8; i++) {
            Square tmp = (Square) moves.pop();
            Move tmpmove = new Move(startingSquare, tmp);
            if ((tmp.getXC() < 0) || (tmp.getXC() > 7) || (tmp.getYC() < 0) || (tmp.getYC() > 7)) {

            } else if (piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                if (piece.contains("White")) {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) {
                        attackingMove.push(tmpmove);
                    }
                }
            } else {
                attackingMove.push(tmpmove);
            }
        }
        return attackingMove;
    }

    private void colorSquares(Stack squares) {
        Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
        while (!squares.empty()) {
            Square s = (Square) squares.pop();
            int location = s.getXC() + ((s.getYC()) * 8);
            JPanel panel = (JPanel) chessBoard.getComponent(location);
            panel.setBorder(greenBorder);
        }
    }

    private void getLandingSquares(Stack found) {
        Move tmp;
        Square landing;
        Stack squares = new Stack();
        while (!found.empty()) {
            tmp = (Move) found.pop();
            landing = (Square) tmp.getLanding();
            squares.push(landing);
        }
        colorSquares(squares);
    }

    private Stack findWhitePieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        for (int i = 0; i < 600; i += 75) {
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = chessBoard.findComponentAt(j, i);
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("White")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        return squares;
    }

    private void resetBorders() {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++) {
            JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
            tmppanel.setBorder(empty);
        }
    }

    private void printStack(Stack input) {
        Move m;
        Square s, l;
        while (!input.empty()) {
            m = (Move) input.pop();
            s = (Square) m.getStart();
            l = (Square) m.getLanding();
            System.out.println("The possible move that was found is : (" + s.getXC() + " , " + s.getYC() + "), landing at (" + l.getXC() + " , " + l.getYC() + ")");
        }
    }

    private void makeAIMove() {
        resetBorders();
        layeredPane.validate();
        layeredPane.repaint();
        Stack white = findWhitePieces();
        Stack completeMoves = new Stack();
        Move tmp;
        Move selectedMove = null;
        Stack temporary = new Stack();
        while (!white.empty()) {
            Square s = (Square) white.pop();
            String tmpString = s.getName();
            Stack tmpMoves = new Stack();

            //identifying the moves possible and assigning them to a stack
            if (tmpString.contains("Knight")) {
                tmpMoves = getKnightMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Bishup")) {
                tmpMoves = getBishopMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Pawn")) {
                tmpMoves = getWhitePawnSquares(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Rook")) {
                tmpMoves = getRookMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Queen")) {
                tmpMoves = getQueenMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("King")) {
                tmpMoves = getKingSquares(s.getXC(), s.getYC(), s.getName());
            }

            while (!tmpMoves.empty()) {
                tmp = (Move) tmpMoves.pop();
                completeMoves.push(tmp);
            }
        }
        temporary = (Stack) completeMoves.clone();
        getLandingSquares(temporary);
        printStack(temporary);

        if (completeMoves.size() == 0) {
            JOptionPane.showMessageDialog(null, "Cogratulations, you have placed the AI component in a Stale Mate Position");
            System.exit(0);
        } else {
            System.out.println("=============================================================");
            Stack testing = new Stack();
            while (!completeMoves.empty()) {
                Move tmpMove = (Move) completeMoves.pop();
                Square s1 = (Square) tmpMove.getStart();
                Square s2 = (Square) tmpMove.getLanding();
                System.out.println("The " + s1.getName() + " can move from (" + s1.getXC() + ", " + s1.getYC() + ") to the following square: (" + s2.getXC() + ", " + s2.getYC() + ")");
                testing.push(tmpMove);
            }
            System.out.println("=============================================================");
            Border redBorder = BorderFactory.createLineBorder(Color.RED, 3);
            if (rand.contains("rand")) {
                selectedMove = agent.randomMove(testing);
            }

            Square startingPoint = (Square) selectedMove.getStart();
            Square landingPoint = (Square) selectedMove.getLanding();
            int startX1 = (startingPoint.getXC() * 75) + 20;
            int startY1 = (startingPoint.getYC() * 75) + 20;
            int landingX1 = (landingPoint.getXC() * 75) + 20;
            int landingY1 = (landingPoint.getYC() * 75) + 20;
            System.out.println("-------- Move " + startingPoint.getName() + " (" + startingPoint.getXC() + ", " + startingPoint.getYC() + ") to (" + landingPoint.getXC() + ", " + landingPoint.getYC() + ")");

            Component c = (JLabel) chessBoard.findComponentAt(startX1, startY1);
            Container parent = c.getParent();
            parent.remove(c);
            int panelID = (startingPoint.getYC() * 8) + startingPoint.getXC();
            panels = (JPanel) chessBoard.getComponent(panelID);
            panels.setBorder(redBorder);
            parent.validate();

            Component l = chessBoard.findComponentAt(landingX1, landingY1);
            if (l instanceof JLabel) {
                Container parentlanding = l.getParent();
                JLabel awaitingName = (JLabel) l;
                String agentCaptured = awaitingName.getIcon().toString();
                boolean agentwins = false;
                if (agentCaptured.contains("King")) {
                    agentwins = true;
                }
                parentlanding.remove(l);
                parentlanding.validate();
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();

                if (agentwins) {
                    JOptionPane.showMessageDialog(null, "The AI Agent has won!");
                    System.exit(0);
                }
            } else {
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
            }
            boolean white2Move = true;
        }
    }

    public static void choice() {
        //creating elements
        JFrame options = new JFrame();
        JButton randomMove = new JButton("Random");
        JButton bestMove = new JButton("Best move");
        JButton advancedMove = new JButton("Advanced moves");

        //adding buttons to options Jframe
        options.add(randomMove);
        options.add(bestMove);
        options.add(advancedMove);
        //setting dimensions of Jframe
        options.setLayout(new FlowLayout());
        Dimension optionsSize = new Dimension(600, 80);
        options.setSize(optionsSize);
        options.setLocation(600, 300);
        options.setVisible(true);

        randomMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setVisible(false);
                random();
            }
        });

        bestMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setVisible(false);
                bestChoice();
            }
        });

        advancedMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setVisible(false);
                advancedMove();
            }
        });
    }

    public static void openBoard() {
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void random() {
        JFrame randomMoveOptions = new JFrame();
        randomMoveOptions.setLayout(new FlowLayout());
        Dimension Size = new Dimension(600, 80);
        randomMoveOptions.setSize(Size);
        randomMoveOptions.setVisible(true);
        randomMoveOptions.setLocation(600, 300);

        JButton whiteChoice = new JButton("White pieces");
        JButton blackChoice = new JButton("Black pieces");
        JButton back = new JButton("Back");

        randomMoveOptions.add(whiteChoice);
        randomMoveOptions.add(blackChoice);
        randomMoveOptions.add(back);

        whiteChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomMoveOptions.setVisible(false);
                openBoard();
            }
        });

        blackChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomMoveOptions.setVisible(false);
                openBoard();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomMoveOptions.setVisible(false);
                choice();
            }
        });
    }

    public static void bestChoice() {
        JFrame bestMoveOptions = new JFrame();
        Dimension Size = new Dimension(600, 80);
        bestMoveOptions.setLayout(new FlowLayout());
        bestMoveOptions.setSize(Size);
        bestMoveOptions.setVisible(true);
        bestMoveOptions.setLocation(600, 300);

        JButton whiteChoice = new JButton("White pieces");
        JButton blackChoice = new JButton("Black pieces");
        JButton back = new JButton("Back");

        bestMoveOptions.add(whiteChoice);
        bestMoveOptions.add(blackChoice);
        bestMoveOptions.add(back);

        whiteChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bestMoveOptions.setVisible(false);
                openBoard();
            }
        });

        blackChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bestMoveOptions.setVisible(false);
                openBoard();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bestMoveOptions.setVisible(false);
                choice();
            }
        });

    }

    public static void advancedMove() {
        JFrame advancedMoveOptions = new JFrame();
        Dimension Size = new Dimension(600, 80);
        advancedMoveOptions.setLayout(new FlowLayout());
        advancedMoveOptions.setSize(Size);
        advancedMoveOptions.setLocation(600, 300);
        advancedMoveOptions.setVisible(true);

        JButton whiteChoice = new JButton("White pieces");
        JButton blackChoice = new JButton("Black pieces");
        JButton back = new JButton("Back");

        advancedMoveOptions.add(whiteChoice);
        advancedMoveOptions.add(blackChoice);
        advancedMoveOptions.add(back);

        whiteChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedMoveOptions.setVisible(false);
                openBoard();
            }
        });

        blackChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedMoveOptions.setVisible(false);
                openBoard();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedMoveOptions.setVisible(false);
                choice();
            }
        });
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    /*
        Main method that gets the ball moving.
    */
    public static void main(String[] args) {
        choice();

    }
}