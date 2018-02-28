package com.company;

import java.util.List;

/**
 *
 * @param <T>
 */
public abstract class  State<T extends Move>
{
    /**
     * what is the value of the state
     * the value should be calculated according to the first player in the game
     * each game writer should decide how the state is evaluated
     * the value should be calculated according to "How good this state is for the first player in the game?"
     * @return the value of the state
     */
    public abstract int evaluate();

    /**
     *
     * @return all the options that the current player can act
     */
    public abstract List<T>nextMoves();

    /**
     * update the state according to the given move
     * @implNote should update the current player
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
     * terminal means there was a victory or a tie
     * @return whether the state is terminal or not
     */
    public abstract boolean isTerminal();

}
