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
        for (int i = 0; i < clone.size(); i++) {
            Move currentMove = (Move) possibilities.pop();

            if (currentMove.getLanding().getName().isEmpty() && (currentMove.getLanding().getYC() == 3 || currentMove.getLanding().getYC() == 4) && (score <= 1))  {
                score = 1;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("Pawn") && score <= 2) {
                if (score != 2) {
                    numbers.clear();
                }
                score = 2;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("Rook") && score <= 5) {
                if (score != 5) {
                    numbers.clear();
                }
                score = 5;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("Knight") && score <= 3) {
                System.out.println("hello there");
                if (score != 3) {
                    numbers.clear();
                }
                score = 3;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("Queen") && score <= 9) {
                if (score != 9) {
                    numbers.clear();
                }
                score = 9;
                numbers.add(currentMove);
            }
            if (currentMove.getLanding().getName().contains("Bishup") && score <= 3) {
                if (score != 3) {
                    numbers.clear();
                }
                score = 3;
                numbers.add(currentMove);

            }
            if (currentMove.getLanding().getName().contains("King") && score <= 10) {
                if (score != 10) {
                    numbers.clear();
                }
                score = 10;
                numbers.add(currentMove);
            }
        }
        return randomMove(numbers);
    }

    public Move twoLevelsDeep(Stack possibilities, Stack possibilities2) {
        Stack white = (Stack) possibilities.clone();
        Stack black = (Stack) possibilities2.clone();
        Stack value = new Stack();
        Move blackMove = (Move) black.pop();
        int scoreWhite = 0;
        int scoreBlack = 0;
        int scoreHighestWhite = 0;
        int scoreHighestBlack = 0;

        for (int i =0;i<possibilities.size();i++){
            Move whiteMove = (Move) white.pop();
            if (whiteMove.getLanding().getName().isEmpty() && (whiteMove.getLanding().getYC() == 3 || whiteMove.getLanding().getYC() == 4))  {
                scoreWhite = 1;
            }
            if (whiteMove.getLanding().getName().contains("Pawn")) {
                scoreWhite = 2;
            }
            if (whiteMove.getLanding().getName().contains("Rook")) {
                scoreWhite = 5;
            }
            if (whiteMove.getLanding().getName().contains("Knight")) {
                scoreWhite = 3;
            }
            if (whiteMove.getLanding().getName().contains("Queen")) {
                scoreWhite = 9;
            }
            if (whiteMove.getLanding().getName().contains("Bishup")) {
                scoreWhite = 3;
            }
            if (whiteMove.getLanding().getName().contains("King")) {
                scoreWhite = 10;
            }
            if (scoreWhite > scoreHighestWhite){
                scoreHighestWhite = scoreWhite;
            }
        }

        return nextBestMove(possibilities2);
    }
}