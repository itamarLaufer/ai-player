# Ai Player

The project enables you to create an AI player for your game easily and quickly.
* The game must be between 2 players who play *against* each other.
* All the information about the game must be always visible for both players.
* A prograss of one player is negative for the other player.


## Getting Started

Copy the three java class to your java workspace.
* don't forget to credit!

### How To Use?

In the example we'll create an Ai for TicTacToe. (Some of the method were taken from the Internet).
first you need to create a class which represents a move in the game and extend Move:


```
public class TicTacToeMove extends Move {
    /**
     *
     * @author Itamar Laufer
     */
    private int row;
    private int col;

    public TicTacToeMove(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "TicTacToeMove{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
```
We created for the Move class an Enum Sign
```
public enum Sign {
    /**
     *
     * @author Itamar Laufer
     */
    X,O, E
}
```
Now we need to create a class representing a State in the game. It extend State and has the class <TicTacToeMove>.
Offcourse there is a need to impleament a few methods.
```
public class TicTacToeState extends State<TicTacToeMove>{
    /**
     *
     * @author Itamar Laufer
     */
    private Sign[][]board;
    private Sign player;
    public Sign ai;
    public TicTacToeState(Sign ai)
    {
        this.ai=ai;
        this.player=Sign.X;
        board=new Sign[3][3];
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                board[i][j] = Sign.E;
            }
        }
    }

    @Override
    public List<TicTacToeMove> nextMoves() {
        List<TicTacToeMove>res = new ArrayList<>();
        if(hasWon(Sign.X)||hasWon(Sign.O))
            return res;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(board[i][j]==Sign.E)
                    res.add(new TicTacToeMove(i,j));
            }
        }
        return res;
    }

    @Override
    public void doTurn(TicTacToeMove move) {
        board[move.getRow()][move.getCol()]=player;
        swapSign();
    }

    @Override
    public void undoTurn(TicTacToeMove move) {
        board[move.getRow()][move.getCol()]= Sign.E;
        swapSign();
    }

    @Override
    public boolean isTerminal() {
        return (nextMoves().isEmpty());
    }


    @Override
    public int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     0 otherwise */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        Sign mySeed =ai;
        Sign oppSeed = Sign.O;
        if(mySeed==oppSeed)
            oppSeed=Sign.X;
        if (board[row1][col1] == mySeed) {
            score = 1;
        } else if (board[row1][col1] == oppSeed) {
            score = -1;
        }

        // Second cell
        if (board[row2][col2] == mySeed) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board[row2][col2] == oppSeed) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board[row3][col3] == mySeed) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board[row3][col3] == oppSeed) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
    public String toString()
    {
        String res="";
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                res+=board[i][j];
            }
            res+=System.lineSeparator();
        }
        return res;
    }
    private int[] winningPatterns = {
            0b111000000, 0b000111000, 0b000000111, // rows
            0b100100100, 0b010010010, 0b001001001, // cols
            0b100010001, 0b001010100               // diagonals
    };

    /** Returns true if thePlayer wins */
    private boolean hasWon(Sign thePlayer) {
        int pattern = 0b000000000;  // 9-bit pattern for the 9 cells
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == thePlayer) {
                    pattern |= (1 << (row * 3 + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }
    private void swapSign()
    {
        if(player==Sign.X)
            player=Sign.O;
        else
            player=Sign.X;
    }
}
```
Now we are ready. Let's create a Main class.

```
public class Main {
    /**
     * @author Itamar Laufer
     */
    public static void main(String[] args) {
        // write your code here
        TicTacToeState state = new TicTacToeState(Sign.X);
        AIPlayer player = new AIPlayer(10);
        boolean flag = true;
        while (!state.isTerminal())
        {
            state.doTurn(player.getBestMove(state,flag));
            flag=!flag;
            System.out.println(state);
        }
    }
}
```


```
Give an example
```

## Built With

* [Intellij](https://www.jetbrains.com/idea/) - The java workspace


## Authors

* **Itamar Laufer** - *high scool student* - (https://github.com/itamarLaufer)
