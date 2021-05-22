/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description:  This program implements automatic step by step solutions for slider puzzles.
 *                The test client should take the name of an input file as a command-line argument
 *                and print the minimum number of moves to solve the puzzle and a corresponding solution.
 *  Response : The system will response minimum number of moves, and also displays a step by step board solution,
 *  equivalently displaying minimum number of moves plus one initial board. Boards within one step away should be neighbors.
 *
 *  Compilation:  javac-algs4 Solver.java
 *  Execution :  java-algs4 Solver puzzle***.txt
 *  Dependencies : MinPQ, Stack, Board
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver {
    public double elapsedtime; // record the total spent time for solving
    private Stack<Board> solution = new Stack<Board>();// record sequences of moves toward the goal board
    private SearchNode goalsearchnode; // recore goal search node
    private int moves; // record number of moves from the initial board to the goal board

    private class SearchNode implements Comparable<SearchNode> {
        private Board board; // current board
        private int moves; // number of moves from initial board to current board
        private SearchNode prev; // previous search node

        // SearchNode constructor
        public SearchNode(Board board, int moves, SearchNode prev) {
            if (board == null)
                throw new IllegalArgumentException("The board argument should not be null !");
            if (!board.isSolvable()) {
                throw new IllegalArgumentException("Unsolvable puzzle.");
            }
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        // the manhattan distance of a board plus the number of moves made so far to get to the search node
        public int manhattanpriority() {
            return moves + board.manhattan();
        }

        // return whether this search node is the goal search node
        public boolean isgoal() {
            return board.hamming() == 0;
        }

        // compare search nodes based on the manhattan priority function
        public int compareTo(SearchNode that) {
            if (this.manhattanpriority() > that.manhattanpriority()) {
                return +1;
            }
            else if (this.manhattanpriority() < that.manhattanpriority()) {
                return -1;
            }
            return 0;
        }

        // return board of the search node
        public Board board() {
            return board;
        }

        // return previous search node
        public SearchNode prev() {
            return prev;
        }

        // return number of moves of this search node
        public int moves() {
            return moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Stopwatch watch = new Stopwatch();
        MinPQ<SearchNode> minpq = new MinPQ<SearchNode>();
        SearchNode initialsearchnode = new SearchNode(initial, 0, null);
        minpq.insert(initialsearchnode);
        // record whether the solution reaches the goal board
        while (true) {
            SearchNode currentmin = minpq.delMin();
            if (currentmin.isgoal()) {
                moves = currentmin.moves();
                goalsearchnode = currentmin;
                break;
            }
            for (Board b : currentmin.board().neighbors()) {
                if (currentmin.prev() != null && currentmin.prev().board().equals(b)) {
                    continue;
                }
                minpq.insert(new SearchNode(b, currentmin.moves() + 1, currentmin));
            }
        }
        elapsedtime = watch.elapsedTime();
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        SearchNode currentsearchnode = goalsearchnode;
        solution.push(currentsearchnode.board());
        while (currentsearchnode.prev() != null) {
            solution.push(currentsearchnode.prev().board());
            currentsearchnode = currentsearchnode.prev();
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        int[][] initialmat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                initialmat[i][j] = in.readInt();
                in.readChar();
            }
        }
        Board initialboard = new Board(initialmat);
        Solver solver = new Solver(initialboard);
        StdOut.printf("Minimum number of moves = %2d\n", solver.moves());
        StdOut.println(solver.solution());
        StdOut.println(solver.elapsedtime);
    }
}
