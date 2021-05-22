/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the Board data structure.
 *               In the test client, the client execute the Board data structure with the argument - board size n.
 *               After that, they should enter the board number in row major order sequentially.
 *  Response :  The system will print the
 *              1.board size
 *              2.the formatted board
 *              3.whether the board is equal to the board copy we create
 *              4.whether the board is solvable
 *              5.whether the board is the goal board
 *              6.the hamming distance to the goal board
 *              7.the manhattan distance to the goal board
 *              8.the board's neighbors (number of neighbors are between 1 ~ 3)
 *
 *  Compilation:  javac-algs4 Board.java
 *  Execution :  java-algs4 Board n
 *  Dependencies : Queue
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int n; // storing puzzle's size
    private int[][] board; // storing puzzle
    private int hamming = 0; // storing puzzle's hamming distance to the goal board
    private int manhattan = 0; // storing puzzle's manhattan distance to the goal board
    private boolean goal = true; // store whether the puzzle is the goal board
    private int zerorow; // store no tile (0)'s row position
    private int zerocol; // store no tile (0)'s col position
    private int inversions = 0; // store number of inversions

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        if (n < 2 || n > 32678) {
            throw new IllegalArgumentException("The tile's length is not within 2 & and 32678!");
        }
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int boardval = tiles[i][j];
                board[i][j] = tiles[i][j];
                // board (i,j)'s value is not the ideal value and is not no tile
                if (boardval != i * n + j + 1 && boardval > 0) {
                    goal = false;
                    hamming += 1;
                    int boardrow = (boardval - 1) / n;
                    int boardcol = (boardval - 1) % n;
                    manhattan += Math.abs(Math.abs(boardrow - i) + Math.abs(boardcol - j));
                }
                else if (boardval == 0) {
                    zerorow = i;
                    zerocol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%3d", board[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col) {
        if (row < 0 || row > n - 1 || col < 0 || col > n - 1) {
            throw new IllegalArgumentException(
                    "row and column's number should be integers between 0 and n-1");
        }
        return board[row][col];
    }

    // board size n
    public int size() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return goal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.size() != that.size()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tileAt(i, j) != that.tileAt(i, j)) return false;
            }
        }
        return true;
    }

    // make a copy of the board
    private int[][] boardcopy() {
        int[][] boardcopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardcopy[i][j] = board[i][j];
            }
        }
        return boardcopy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        for (int i = zerorow - 1; i <= zerorow + 1; i += 2) {
            if (i >= 0 && i <= n - 1) {
                int[][] boardcopy = boardcopy();
                boardcopy[zerorow][zerocol]
                        = board[i][zerocol]; // exchange not tile's neighbor's value with no tile
                boardcopy[i][zerocol] = 0;
                Board neighborboard = new Board(boardcopy);
                neighbors.enqueue(neighborboard);
            }
        }
        for (int j = zerocol - 1; j <= zerocol + 1; j += 2) {
            if (j >= 0 && j <= n - 1) {
                int[][] boardcopy = boardcopy();
                boardcopy[zerorow][zerocol]
                        = board[zerorow][j]; // exchange not tile's neighbor's value with no tile
                boardcopy[zerorow][j] = 0;
                Board neighborboard = new Board(boardcopy);
                neighbors.enqueue(neighborboard);
            }
        }
        return neighbors;
    }

    // is this board solvable?
    public boolean isSolvable() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = i; k < n; k++) {
                    int m;
                    // if we are still in the same row as first value
                    if (k == i) {
                        m = j;
                    }
                    // if we are in the latter row
                    else {
                        m = 0;
                    }
                    for (; m < n; m++) {
                        if ((board[i][j] > board[k][m]) && (board[k][m] != 0) && (board[i][j]!= 0)) {
                            inversions += 1;
                        }
                    }
                }
            }
        }
        if (n % 2 != 0 && inversions % 2 == 0) {
            return true;
        }
        else if (n % 2 != 0) {
            return false;
        }
        else if ((inversions + zerorow) % 2 != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        StdOut.println(
                "Please enter board number (all 0 ~ n^2 - 1 should appear once)! It will sequentially be stored in row major order\n");
        int[][] usermat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                usermat[i][j] = StdIn.readInt();
            }
        }
        Board userboard = new Board(usermat);
        Board userboardcopy = new Board(usermat);
        StdOut.printf("The size of board is %2d\n", userboard.size());
        StdOut.println("The formatted userboard is\n" + userboard.toString());
        if (userboard.equals(userboardcopy))
            StdOut.println("The board you create is equal to the copy we create !");
        if (userboard.isSolvable()) {
            StdOut.println("The board you create is solvable !");
        }
        else {
            StdOut.println("The board you create is not solvable !");
        }
        if (userboard.isGoal()) {
            StdOut.println("The board you create is the goal board !");
        }
        else {
            StdOut.println("The board you create is not the goal board !");
        }
        StdOut.printf("The hamming distance of the board is %2d\n", userboard.hamming());
        StdOut.printf("The manhattan distance of the board is %2d\n", userboard.manhattan());
        StdOut.printf("The tiles at (0,0), (1,0) are %2d, %2d\n", userboard.tileAt(0, 0),
                      userboard.tileAt(1, 0));
        StdOut.println(userboard.neighbors());
    }

}
