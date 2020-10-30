import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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

    Boolean validMove = false;
    String pieceName;
    Boolean success;
    Boolean turn = true;

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;

        chessPiece.setVisible(false);
        success = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        String tmp = chessPiece.getIcon().toString();
        pieceName = tmp.substring(0, (tmp.length() - 4));

        switch (pieceName) {
            case "WhitePawn" -> whitePawn(e);
            case "BlackPawn" -> blackPawn(e);
            case "WhiteKnight", "BlackKnight" -> whiteKnight(e);
            case "WhiteKing", "BlackKing" -> whiteKing(e);
            case "WhiteBishup", "BlackBishup" -> whiteBishup(e);
            case "WhiteRook", "BlackRook" -> whiteRook(e);
            case "WhiteQueen" -> {
                whiteRook(e);
                if (!validMove) {
                    whiteBishup(e);
                }
            }
        }
        
        //other switch where if boolean = false
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
        }

		/*
			The only piece that has been enabled to move is a White Pawn...but we should really have this is a separate
			method somewhere...how would this work.
			
			So a Pawn is able to move two squares forward one its first go but only one square after that. 
			The Pawn is the only piece that cannot move backwards in chess...so be careful when committing 
			a pawn forward. A Pawn is able to take any of the opponent’s pieces but they have to be one 
			square forward and one square over, i.e. in a diagonal direction from the Pawns original position. 
			If a Pawn makes it to the top of the other side, the Pawn can turn into any other piece, for 
			demonstration purposes the Pawn here turns into a Queen.
		*/
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
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}