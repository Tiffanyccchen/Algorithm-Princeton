/* *****************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution: java-algs4 Percolation.java n, repeatedly enter (row, col), press ctrl + Z to stop
 *
 *  Description : This program implements the percolation data structure.
 *    This program takes the command line argument sequentially.
 *    - Enter n - to create an n-by-n grid of sites (intially all blocked)
 *    - Enter row col (separated by space) repeatedly - a sequence of sites (row, col) to open
 *    - Enter ctrl + Z (on Windows) to exit opening sites
 *
 *  Resopnse : After each site is opened, the system will print out status of the site - whether it is open and whether it is full
 *  When you finish opening sites,
 *  the system will tell you percolate status and number of open sites !
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    //use WQUN to efficiently connect open sites and determine percolate status
    private WeightedQuickUnionUF grid;
    //keep track of whether the site is open
    private boolean[][] opensites;
    //store n as a instance variable to reuse efficiently
    private int edgelen;
    //keep track of number of open sites
    private int opencount;
    //virtual top site
    private final int top = 0;
    //virtual bottom site
    private int bottom;

    //map 2d dimension of a square grid with length n to 1d dimension
    private int twotooned(int row, int col) {
        return row * edgelen + col + 1;
    }

    //throw an error if the site's coordinate user types to open is out of bound
    private void validindices(int row, int col) {
        if ((row < 0 || row > (edgelen - 1)) & (col < 0 || col > (edgelen - 1))) {
            throw new IllegalArgumentException("Both " + row + " and " + col + " are not within 0 ~ " + (edgelen - 1));
        }
        if (row < 0 || row > (edgelen - 1)) {
            throw new IllegalArgumentException(row + "is not within 0 ~ " + (edgelen - 1));
        }
        if (col < 0 || col > (edgelen - 1)) {
            throw new IllegalArgumentException(col + "is not within 0 ~ " + (edgelen - 1));
        }
    }

    //check whether the neighbor of the site is out of bound
    private boolean isneighbor(int row, int col) {
        return row >= 0 && row <= (edgelen - 1) && col >= 0 && col <= (edgelen - 1);
    }

    //creates (n-by-n + 2) (virtual top and virtual bottom) grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n by n grids's n is" + n + ", n should be larger than 0");
        }
        grid = new WeightedQuickUnionUF(n * n + 2);
        opensites = new boolean[n][n];
        edgelen = n;
        opencount = 0;
        bottom = n * n + 1;
        //virtual top site is connected with first row, virtual bottom site is connected with last row
        for (int i = 1; i < n + 1; i++) grid.union(top, i);
        for (int i = n * n; i > (n * n - n); i--) grid.union(bottom, i);
    }

    //opens the site (row, col) if it is not open already and the coordinate is valid
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        validindices(row, col);
        opensites[row][col] = true;
        //increase the number of open sites
        opencount++;
        int onedind = twotooned(row, col);
        //if the neighbor site's coordinate is valid and open, connect the site with it
        if (isneighbor(row - 1, col) && opensites[row - 1][col]) grid.union(twotooned(row - 1, col), onedind);
        if (isneighbor(row + 1, col) && opensites[row + 1][col]) grid.union(twotooned(row + 1, col), onedind);
        if (isneighbor(row, col - 1) && opensites[row][col - 1]) grid.union(twotooned(row, col - 1), onedind);
        if (isneighbor(row, col + 1) && opensites[row][col + 1]) grid.union(twotooned(row, col + 1), onedind);
    }

    //returns whether the site is open or not if the coordinate is valid
    public boolean isOpen(int row, int col) {
        validindices(row, col);
        return opensites[row][col];
    }

    //returns whether the site is full(connected to the top) or not if the coordinate is valid
    public boolean isFull(int row, int col) {
        validindices(row, col);
        return grid.find(twotooned(row, col)) == grid.find(top);
    }

    //returnss the number of open sites
    public int numberOfOpenSites() {
        return opencount;
    }

    //return whether the system percolates i.e. virtual top site is connected to virtual bottom site
    public boolean percolates() {
        return grid.find(top) == grid.find(bottom);
    }

    //unit testing for the validity of the class
    public static void main(String[] args) {
        StdOut.println("Please enter n*n grids's n");
        int n = StdIn.readInt();
        Percolation percolate = new Percolation(n);

        StdOut.println("Press ctrl + z to exit the open-site actions\n");
        StdOut.println("Please enter x y coordinate of the site you want to open!\n");
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            percolate.open(row, col);
            StdOut.println(percolate.numberOfOpenSites() + " opensites");
            if (percolate.isOpen(row, col)) {
                StdOut.println("(" + row + " , " + col + ") is open");
            }
            if (percolate.isFull(row, col)) {
                StdOut.println("(" + row + " , " + col + ") is full");
            }
        }
        if (percolate.percolates()) {
            StdOut.println("Percolates with " + percolate.numberOfOpenSites() + " sites open !");
        } else {
            StdOut.println("Does not percolate with " + percolate.numberOfOpenSites() + " sites open !");
        }
    }
}
