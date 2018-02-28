package com.company;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer {
    /**
     * the depth of the turns that the ai runs
     * should be function of the average state.nextMoves().size so the ai would be able to complete computation
     */
    int depth;

    public AIPlayer(int depth) {
        this.depth = depth;
    }

    /**
     * the best move computation
     * @param state the current state, the player supposes that it's its turn.
     * @param isFirstPlayer whether the ai is also the first playe of the game or the second.
     * it's important for knowing whether the player is maximizer or minimizer
     * @param <T> the type representing a move of this game
     * @return the move that would give the player the biggest earn (max value)
     */
    public <T extends Move> T getBestMove(State<T>state,boolean isFirstPlayer)
    {
        List<T>moves = state.nextMoves();
        T bestMove=null;
        int maxScore=Integer.MIN_VALUE;
        int score;
        for(T move:moves)
        {
            state.doTurn(move);
            score = miniMax(state,isFirstPlayer);
            state.undoTurn(move);
            if(bestMove==null||(isFirstPlayer&&score>maxScore)||(!isFirstPlayer&&score<maxScore))
            {
                maxScore = score;
                bestMove=move;
            }
        }
        return bestMove;
    }

    /**
     * the method calculate the value of the state
     * using mini max with alpha beta pruning
     * @param depth
     * @param state
     * @param isMaximizer
     * @param alpha
     * @param beta
     * @param <T>
     * @return the value of this state (the best leaf (with given depth) with the max value if maximizer and min value if minimizer)
     */
    private<T extends Move> int miniMax (int depth,State<T>state,boolean isMaximizer,int alpha,int beta)
    {
        int score;
        if(depth==0||state.isTerminal())
            return state.evaluate();
        List<T>moves = state.nextMoves();
        if(isMaximizer)
        {
            for(T move:moves)
            {
                state.doTurn(move);
                score = miniMax(depth-1,state,!isMaximizer,alpha,beta);
                alpha = Math.max(alpha,score);
                state.undoTurn(move);
                if(alpha>=beta)
                    break;
            }
            return alpha;
        }
        else
        {
            for(T move:moves)
            {
                state.doTurn(move);
                score = miniMax(depth-1,state,isMaximizer,alpha,beta);
                beta = Math.min(beta,score);
                state.undoTurn(move);
                if(alpha>=beta)
                    break;
            }
            return beta;
        }
    }
    private<T extends Move> int miniMax (State<T>state,boolean isMaximizer)
    {
        return miniMax(depth,state,!isMaximizer,Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

}


