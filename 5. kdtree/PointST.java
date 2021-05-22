/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the PointST data structure.
 *               In the test client, the client can enter the (x,y) coordinate of the points.
 *  Response :  The system will print the
 *              1. whether the points set is empty
 *              2. number of points in the points set
 *              3. each point (x,y)'s coordinate, rank, and whether the points set contains the point (x,y) or not
 *              4. the rectangle's coordinates we generate randomly, and points contained in the rectangle
 *              5. the nearset point from the randomly generate point in the points set
 *
 *  Compilation:  javac-algs4 PointST.java
 *  Execution :  java-algs4 PointST
 *  Dependencies : Point2D, RectHV, Queue, RedBlackBST
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointST<Value> {
    // uses a red black BST to represent a symbol table whose keys are two-dimensional points
    private RedBlackBST<Point2D, Value> points2Drbtree;

    // construct an empty symbol table of points
    public PointST() {
        points2Drbtree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return points2Drbtree.isEmpty();
    }

    // number of points
    public int size() {
        return points2Drbtree.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new java.lang.NullPointerException();
        }
        points2Drbtree.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        return points2Drbtree.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        return points2Drbtree.get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return points2Drbtree.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }
        Queue<Point2D> point2Drect = new Queue<Point2D>();
        for (Point2D p : points2Drbtree.keys()) {
            if (rect.contains(p)) {
                point2Drect.enqueue(p);
            }
        }
        return point2Drect;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (points2Drbtree.isEmpty()) return null;
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        double smallestnum = Double.POSITIVE_INFINITY;
        Point2D smallestpoint = null;
        for (Point2D pcompare : points2Drbtree.keys()) {
            double distance = pcompare.distanceSquaredTo(p);
            if (distance <= smallestnum) {
                smallestnum = distance;
                smallestpoint = pcompare;
            }
        }
        return smallestpoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> integerPointST = new PointST<Integer>();
        StdOut.println(
                "Please enter (x,y) position of your point to insert! x,y's ranges should be within 0 to 1");
        StdOut.println("Press ctrl + z to stop inserting points!");
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            integerPointST.put(new Point2D(x, y), i);
            i += 1;
        }
        if (integerPointST.isEmpty()) {
            StdOut.println("The Point2D Symbol Table is empty");
        }
        else {
            StdOut.println("The Point2D Symbol Table is not empty");
        }
        StdOut.println("The Point2D Symbol Table has " + integerPointST.size() + " points!");
        Iterable<Point2D> points2Dall = integerPointST.points();
        for (Point2D p : points2Dall) {
            StdOut.println(p.toString() + " 's val is " + integerPointST.get(p));
            if (integerPointST.contains(p)) {
                StdOut.println("The Point2D Symbol Table contains " + p.toString());
            }
        }
        RectHV rect = new RectHV(0.5, 0.5, 1, 1);
        if (integerPointST.range(rect) == null) {
            StdOut.println("The rectangle generated randomly doesn't contain any point");
        }
        else {
            StdOut.println(
                    "The rectangle generated randomly " + rect.toString() + " contains "
                            + integerPointST.range(rect));
        }
        Point2D point2Dquery = new Point2D(0.5, 0.5);
        StdOut.println(
                "The nearst point to point " + point2Dquery.toString() + " is " + integerPointST
                        .nearest(point2Dquery).toString());
    }
}
