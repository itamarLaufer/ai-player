package com.laufer.itamar.ai;

import java.util.List;

/**
 *
 * @param <T>
 */
public abstract class  State<T>
{
    /**
     * Evaluates how good is this state.
     * The value should be calculated according to the first player in the game
     * Each game developer should decide how the state is evaluated
     * the value should be calculated according to "How good this state is for the first player in the game?"
     * Positive value means the first player is leading and negative mean the second player is leading.
     * @return the value of the state
     */
    public abstract int evaluate();

    /**
     *
     * @return All the move options that the current player can act
     */
    public abstract List<T>nextMoves();

    /**
     * update the state according to the given move
     * @param move the details about the turn the should be acted
     */
    public abstract void doTurn(T move);

    /**
     * cancel the given move
     * supposes that the move was done
     * @param move that was done and should be canceled
     */
    public abstract void undoTurn(T move);

    /**
     * Whether there is a victory or a tie in the game of not.
     * e
     * @return true if the state is terminal, otherwise false.
     */
    public abstract boolean isTerminal();

}
