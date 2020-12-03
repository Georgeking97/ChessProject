import java.util.*;

public class AIAgent {
    Random rand;

    public AIAgent(){
        rand = new Random();
    }

    //pass in the stack

/*
  The method randomMove takes as input a stack of potential moves that the AI agent
  can make. The agent uses a random number generator to randomly select a move from
  the inputted Stack and returns this to the calling agent.
*/

    public Move randomMove(Stack possibilities){

        int moveID = rand.nextInt(possibilities.size());
        System.out.println("Agent randomly selected move : "+moveID);
        for(int i=1;i < (possibilities.size()-(moveID));i++){
            possibilities.pop();
        }
        Move selectedMove = (Move)possibilities.pop();
        return selectedMove;
    }

    public Move nextBestMove(Stack possibilities){
        Move selectedMove = new Move();
        System.out.println("nextBest");
        return selectedMove;
    }

    public Move twoLevelsDeep(Stack possibilities){
        Move selectedMove = new Move();
        System.out.println("advanced");
        return selectedMove;
    }
}