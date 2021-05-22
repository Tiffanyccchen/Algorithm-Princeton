/* *****************************************************************************
 *  Compilation:  javac-algs4 Percolation_NoBackwash.java
 *  Execution: java-algs4 Percolation_NoBackwash.java n, (row, col) repeating until press ctrl + Z to stop
 *
 *  This program takes the command line argument sequentially.
 *  Its functionality is the same as Percolation class except it solves backwash problem by not using virtual sites.
 *    - Enter n - to create an n-by-n grid of sites (intially all blocked)
 *    - Enter row col (separated by space) repeatedly - a sequence of sites (row, col) to open
 *    - Enter ctrl + Z (on Windows) to exit opening sites
 *
 *  After each site is opened, the system will print out status of the site - whether it is open and whether it is full
 *  When you finish opening sites,
 *  the system will tell you percolate status and number of open sites !
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation_NoBackwash {
    //use WQUN to efficiently connect open sites and determine percolate status
    private WeightedQuickUnionUF grids;
    /*keep track of the status of sites.represent statuses by integers.
    { 1 :connected to top(is full), 2 :connected to bottom, 3:open, 4:blocked, 5 :percolates}*/
    private int[] statusofsites;
    //store n as a instance variable to reuse efficiently
    private int edgelen;
    //keep track of number of open sites
    private int opencount;
    //use a flag to reflect whether the system percolates
    private boolean percolationFlag;

    //map 2d dimension of a square grid with length n to 1d dimension
    private int twotooned(int row, int col) {
        return row * edgelen + col;
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

    //check whether the neighbors' statuses of an open site contain the target status
    private boolean arraycontain(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) return true;
        }
        return false;
    }

    //creates n-by-n grid, with all sites initially blocked
    public Percolation_NoBackwash(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n by n grids's n is" + n + ", n should be larger than 0");
        }
        grids = new WeightedQuickUnionUF(n * n);
        statusofsites = new int[n * n];
        edgelen = n;
        opencount = 0;
        for (int i = 0; i < n * n; i++) statusofsites[i] = 4;
    }

    //opens the site (row, col) if it is not open already and if the coordinate is valid
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        validindices(row, col);
        //increase the number of open sites
        opencount++;
        int onedind = twotooned(row, col);
        int[] sn = new int[4];

        if (isneighbor(row - 1, col) && isOpen(row - 1, col)) {
            //record the status of its opening neighbors
            sn[0] = statusofsites[grids.find(twotooned(row - 1, col))];
            //connect the site with its opening neighbors
            grids.union(twotooned(row - 1, col), onedind);
        }
        if (isneighbor(row + 1, col) && isOpen(row + 1, col)) {
            sn[1] = statusofsites[grids.find(twotooned(row + 1, col))];
            grids.union(twotooned(row + 1, col), onedind);
        }
        if (isneighbor(row, col - 1) && isOpen(row, col - 1)) {
            sn[2] = statusofsites[grids.find(twotooned(row, col - 1))];
            grids.union(twotooned(row, col - 1), onedind);
        }
        if (isneighbor(row, col + 1) && isOpen(row, col + 1)) {
            sn[3] = statusofsites[grids.find(twotooned(row, col + 1))];
            grids.union(twotooned(row, col + 1), onedind);
        }
        //update the status of the new connected component
        //if any of its neighbors is full
        boolean contain1 = arraycontain(sn, 1);
        //if any of its neighbors is connected to bottom
        boolean contain2 = arraycontain(sn, 2);
        //find new status's position
        int snew = grids.find(onedind);
        //check whether the new status percolates, connected to top/ bottom, or is just open
        if ((contain1 && contain2) || (contain1 && row == (edgelen - 1)) || (contain2 && row == 0)) {
            statusofsites[snew] = 5;
            percolationFlag = true;
        } else if (contain1 || row == 0) statusofsites[snew] = 1;
        else if (contain2 || row == (edgelen - 1)) statusofsites[snew] = 2;
        else statusofsites[snew] = 3;
    }

    //returns whether the site is open or not if the coordinate is valid
    public boolean isOpen(int row, int col) {
        validindices(row, col);
        return statusofsites[grids.find(twotooned(row, col))] != 4;
    }

    //returns whether the site is full(connected to the top) or not if the coordinate is valid
    public boolean isFull(int row, int col) {
        validindices(row, col);
        return ((statusofsites[grids.find(twotooned(row, col))] == 1) || (statusofsites[grids.find(twotooned(row, col))] == 5));
    }

    //returns the number of open sites
    public int numberOfOpenSites() {
        return opencount;
    }

    //return whether the grid percolates
    public boolean percolates() {
        return percolationFlag;
    }

    //unit testing for the validity of the class
    public static void main(String[] args) {
        StdOut.println("Please enter n*n grids's n");
        int n = StdIn.readInt();
        Percolation_NoBackwash percolate = new Percolation_NoBackwash(n);

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

