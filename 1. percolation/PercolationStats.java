/* *****************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java -algs4 PercolationStats n trials
 *  Dependencies: Percolation.java
 *
 *  Description : This program gives summary statistics for conducting percolation threshold experiments of size n trial times.
 *    This program takes the command line argument int n and int trials.
 *    - Enter n - to create an n-by-n grid of sites (intially all blocked)
 *    - Enter trials - to determine the number of trials to repeat
 *
 *  Response : Each trial is ended when the system percolates and record the percolation threshold (number of open sites / total number of sites)
 * After all trials, it prints out the percolation threshold's mean, standard deviation, 95% CI, and system's elapsed time for reference
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    //record the total spent time for testing
    public double elapsedtime;
    //record percentage of open sites in each trial
    private double[] threshold;
    //record number of trials
    private final int trials;

    //perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        //ensure n and trials are valid
        if (n < 0) {
            throw new IllegalArgumentException("n by n grids's n is" + n + ", n should be larger than 0");
        }
        if (trials < 0) {
            throw new IllegalArgumentException("Number of trials is" + trials + "trials should be larger than 0");
        }
        threshold = new double[trials];
        this.trials = trials;
        double gridsnum = n * n;
        Stopwatch watch = new Stopwatch();

        for (int i = 0; i < trials; i++) {
            //create a percolation object
            Percolation percolation = new Percolation(n);
            //open sites until the system percolates
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                percolation.open(row, col);
            }
            //record the percentage of open sites in ith trial
            threshold[i] = percolation.numberOfOpenSites() / gridsnum;
        }
        //record elapsed time
        elapsedtime = watch.elapsedTime();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationstats = new PercolationStats(n, trials);
        StdOut.printf("%-10s = ", "mean()");
        StdOut.printf("%-10.6f\n", percolationstats.mean());
        StdOut.printf("%-10s = ", "stddev()");
        StdOut.printf("%-10.6f\n", percolationstats.stddev());
        StdOut.printf("%-10s = ", "confidenceLow()");
        StdOut.printf("%-10.6f\n", percolationstats.confidenceLow());
        StdOut.printf("%-10s = ", "confidenceHigh()");
        StdOut.printf("%-10.6f\n", percolationstats.confidenceHigh());
        StdOut.printf("%-10s = ", "elapsed time");
        StdOut.printf("%-10.6f\n", percolationstats.elapsedtime);
    }

}
