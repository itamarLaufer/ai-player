package com.laufer.itamar.ai;

import java.util.List;

public class AIPlayer {
    /**
     * the depth of the turns that the ai runs
     * should be function of the average state.nextMoves().size so the ai would be able to complete computation
     */
    int depth;

    /**
     * Constructs the ai
     * @param depth the depth of the min-max tree, should be in a size that will enable the calculation to end in the desired time
     */
    public AIPlayer(int depth) {
        this.depth = depth;
    }

    /**
     * the best move computation
     * @param state the current state, the player supposes that it's its turn.
     * @param isFirstPlayer whether the ai is also the first player of the game or the second.
     * it's important for knowing whether the player is maximizer or minimizer
     * @param <T> the type representing a move of this game
     * @return the best move for the player
     */
    public <T> T getBestMove(State<T>state, boolean isFirstPlayer)
    {
        return miniMax(depth, state, isFirstPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE).move;
    }

    /**
     * the method calculate the value of the state
     * using mini max with alpha beta pruning
     * @param depth the depth of the min max tree
     * @param state the current state of the game
     * @param isMaximizer whether the current player maximizes or minimizes
     * @param alpha the biggest value found yet
     * @param beta the lowest value found yet
     * @param <T> the type representing a move of this game
     * @return MoveAndValue object with the best move to perform and its evaluation
     */
    private <T> MoveAndValue<T> miniMax (int depth, State<T>state, boolean isMaximizer, int alpha, int beta)
    {
        int score;
        if(depth==0 || state.isTerminal()) {
            return new MoveAndValue<>(state.evaluate(), null);
        }
        List<T>moves = state.nextMoves();
        MoveAndValue<T> bestMove = null;
        MoveAndValue<T> option;
        if(isMaximizer)
        {
            int maxVal = Integer.MIN_VALUE;
            for(T move:moves)
            {
                state.doTurn(move);
                option = miniMax(depth-1, state, false, alpha, beta);
                score = option.value;
                alpha = Math.max(alpha, score);
                if(score > maxVal) {
                    maxVal = score;
                    bestMove = new MoveAndValue<>(score, move);
                }
                state.undoTurn(move);
                if(alpha >= beta)
                    break;
            }
        }
        else
        {
            int minVal = Integer.MAX_VALUE;
            for(T move:moves)
            {
                state.doTurn(move);
                option = miniMax(depth-1, state, true, alpha, beta);
                score = option.value;
                beta = Math.min(score, beta);
                if(score < minVal) {
                    minVal = score;
                    bestMove = new MoveAndValue<>(score, move);
                }
                state.undoTurn(move);
                if(alpha >= beta)
                    break;
            }
        }
        return bestMove;
    }

    /**
     * This class represents a move in a abstract game aside with the evaluation of this move
     * @param <T> the type of the object representing a move in this game
     */
    private  class MoveAndValue<T>{
        int value;
        T move;

        /**
         * Constructs new MoveAndValue
         * @param value the evaluation of this move
         * @param move the move
         */
        MoveAndValue(int value, T move) {
            this.value = value;
            this.move = move;
        }
    }
}
