package projectc4.c4.clienthej;

/**
 * @author Jimmy Maksymiw, Erik Sandgren
 */
public class GameGrid {
    private int[][] board = new int[7][6];

    public int getElement(int i, int j) {
        return board[i][j];
    }

    public void setElement(int i, int j, int value) {
        board[i][j] = value;
    }

    public int getLength() {
        return board[1].length;
    }

    public int getHeight() {
        return board.length;
    }

    public void reset() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    public void print() {
        for(int i = 0; i < board.length; i++) {
            System.out.println();
            for(int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "  ");
            }
        }
        System.out.println("\n\n\n\n\n");
    }
}