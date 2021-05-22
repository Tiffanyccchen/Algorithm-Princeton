/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This is for readme question 4.
 * The system will print the number of  nearest-neighbor calculations PointST/KdTreeST implementation
 *  perform per second for input1M.txt (1 million points), where the query points are random points in the unit square
 *
 *  Compilation:  javac-algs4 NearestNeighborTime.java
 *  Execution :  java-algs4 NearestNeighborTime
 *  Dependencies : KdTreeST, Point2DST, Stopwatch
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class NearestNeighborTime {
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the two data structures with point from standard input
        PointST<Integer> brute = new PointST<Integer>();
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.put(p, i);
            brute.put(p, i);
        }
        int count = 0;
        double elapsedtime = 0;
        Stopwatch watch = new Stopwatch();
        while (elapsedtime < 1) {
            kdtree.nearest(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
            elapsedtime = watch.elapsedTime();
            count += 1;
        }
        StdOut.printf("Call KdTreeST's nearest method %2d times in %2f seconds \n", count,
                      elapsedtime);
        count = 0;
        elapsedtime = 0;
        watch = new Stopwatch();
        while (elapsedtime < 1) {
            brute.nearest(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
            elapsedtime = watch.elapsedTime();
            count += 1;
        }
        StdOut.printf("Call PointST's nearest method %2d times in %2f seconds \n", count,
                      elapsedtime);

    }
}
