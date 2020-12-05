import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AIAgent {
    Random rand;

    public AIAgent() {
        rand = new Random();
    }

    public Move randomMove(Stack possibilities) {
        int moveID = rand.nextInt(possibilities.size());

        for (int i = 1; i < (possibilities.size() - (moveID)); i++) {
            possibilities.pop();
        }

        Move selectedMove = (Move) possibilities.pop();
        return selectedMove;
    }


    public Move nextBestMove(@NotNull Stack possibilities) {
        Stack numbers = new Stack();
        Stack clone = (Stack) possibilities.clone();
        int score = 0;

        System.out.println("Possibilities size before loop: " + possibilities.size());
        for (int i = 0; i < clone.size(); i++) {
            Move currentMove = (Move) possibilities.pop();

            if (currentMove.getLanding().getName().isEmpty() && score <= 1) {
                if (score != 1) {
                    System.out.println("does it make it in here?");
                    numbers.clear();
                }
                score = 1;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("BlackPawn") && score <= 2) {
                if (score != 2) {
                    numbers.clear();
                }
                score = 2;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("BlackRook") && score <= 5) {
                if (score != 5) {
                    numbers.clear();
                }
                score = 5;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("BlackKnight") && score <= 3) {
                if (score != 3) {
                    numbers.clear();
                }
                score = 3;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("BlackQueen") && score <= 9) {
                if (score != 9) {
                    numbers.clear();
                }
                score = 9;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("BlackBishup") && score <= 3) {
                if (score != 3) {
                    numbers.clear();
                }
                score = 3;
                numbers.add(currentMove);

            }
            if (currentMove.getLanding().getName().contains("BlackKing") && score <= 10) {
                if (score != 10) {
                    numbers.clear();
                }
                score = 10;
                numbers.add(currentMove);
            }
        }
        System.out.println("numbers size after loop: " + numbers.size());
        int moveID = rand.nextInt(numbers.size());
        System.out.println("possibilities size after loop: " + possibilities.size());
        System.out.println("numbers size after loop: " + numbers.size());
        for (int i = 1; i < (numbers.size() - (moveID)); i++) {
            numbers.pop();
        }
        return (Move) numbers.pop();
    }

    public Move twoLevelsDeep(Stack possibilities) {
        //Move selectedMove = new Move();

        Move test = nextBestMove(possibilities);
        System.out.println(test);

        //find best move(s) for white pieces
        //


        return test;
    }
}