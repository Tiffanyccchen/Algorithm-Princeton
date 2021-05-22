/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the KdTreeST data structure.
 *               In the test client, the client can enter the (x,y) coordinate of the points.
 *  Response :  The system will print the
 *              1. whether the kdtree set is empty
 *              2. number of points in the kdtree set
 *              3. each point (x,y)'s coordinate, rank, and whether the kdtree set contains the point (x,y) or not
 *              4. the rectangle's coordinates we generate randomly, and points contained in the rectangle
 *              5. the nearset point from the randomly generate point in the kdtree set
 *
 *  Compilation:  javac-algs4 KdTreeST.java
 *  Execution :  java-algs4 KdTreeST
 *  Dependencies : Point2D, RectHV, Queue
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {
    private Node root; // root of KdTreeST
    private int size; // size of KdTreeST

    private class Node {
        private Point2D p;     // the point
        private Value val;     // the symbol table maps the point to this value
        private RectHV rect;   // the axis-aligned rectangle corresponding to this node
        private Node lb;       // the left/bottom subtree
        private Node rt;       // the right/top subtree

        // constructor for Node class
        public Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }


    // construct an empty symbol table of points
    public KdTreeST() {
        // initialize size to 0
        size = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new java.lang.NullPointerException();
        }
        double xmin = Double.NEGATIVE_INFINITY;
        double ymin = Double.NEGATIVE_INFINITY;
        double xmax = Double.POSITIVE_INFINITY;
        double ymax = Double.POSITIVE_INFINITY;
        root = put(root, p, val, new RectHV(xmin, ymin, xmax, ymax), true);
        // grow size by 1 when invoke a put method
        size += 1;
    }

    // private helper for putting (p,v) in the KdTreeST
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean orientevertical) {
        if (x == null) return new Node(p, val, rect);
        // compare to-be-put-point's x value with the node's corrsponding point's x value
        double cmpx = p.x() - x.p.x();
        // compare to-be-put-point's y value with the node's corrsponding point's y value
        double cmpy = p.y() - x.p.y();
        if (cmpx == 0 && cmpy == 0) {
            // if to-be-put-point already exists, change its value
            x.val = val;
        }
        else if (orientevertical) {
            if (cmpx < 0) {
                // change rectangle's xmax value if the split orientation is vertical and to-be-put-point's x value is smaller than the node's corrsponding point's x value
                rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
                x.lb = put(x.lb, p, val, rect, false);
            }
            else if (cmpx >= 0) {
                // change rectangle's xmin value if the split orientation is vertical and to-be-put-point's x value is larger t
                rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
                x.rt = put(x.rt, p, val, rect, false);
            }
        }
        else {
            if (cmpy < 0) {
                // change rectangle's ymax value if the split orientation is horizontal and to-be-put-point's y value is smaller t
                rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
                x.lb = put(x.lb, p, val, rect, true);
            }
            else if (cmpy >= 0) {
                // change rectangle's ymin value if the split orientation is horizontal and to-be-put-point's y value is larger t
                rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
                x.rt = put(x.rt, p, val, rect, true);
            }
        }
        return x;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        return get(root, p, true);
    }

    // private helper for getting value associated with point p
    private Value get(Node x, Point2D p, boolean orientevertical) {
        if (x == null) return null;
        // compare to-be-put-point's x value with the node's corrsponding point's x value
        double cmpx = p.x() - x.p.x();
        // compare to-be-put-point's y value with the node's corrsponding point's y value
        double cmpy = p.y() - x.p.y();
        // test if the point is found by comparing to-be-get point's (x,y) val with node's corrsponding (x,y) val
        if (cmpx != 0 || cmpy != 0) {
            if (orientevertical) {
                //  if split orientation is vertical &to-be-put-point's x value is smaller than the node's corrsponding point's x value, go to left subtree, change orientation
                if (cmpx < 0) return get(x.lb, p, false);
                else if (cmpx >= 0) return get(x.rt, p, false);
            }
            else {
                //  if split orientation is horizontal &to-be-put-point's y value is smaller than the node's corrsponding point's y value, go to left subtree, change orientation
                if (cmpy < 0) return get(x.lb, p, true);
                else if (cmpy >= 0) return get(x.rt, p, true);
            }
        }
        return x.val;
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        // maintain a Node queue in order to trace lb and rt
        Queue<Node> queue = new Queue<Node>();
        // maintain a Point2D queue to store all points in level order
        Queue<Point2D> pointqueue = points(root, queue);
        return pointqueue;
    }

    // private helper for iterating points
    private Queue<Point2D> points(Node x, Queue<Node> queue) {
        if (x == null) return null;
        Queue<Point2D> pointqueue = new Queue<Point2D>();
        queue.enqueue(x);
        pointqueue.enqueue(x.p);
        while (!queue.isEmpty()) {
            // FIFO, therefore always dequeue(print) higher level first
            x = queue.dequeue();
            if (x.lb != null) {
                // enqueue lb first, therefore always dequeue(print) from left first
                queue.enqueue(x.lb);
                pointqueue.enqueue(x.lb.p);
            }
            if (x.rt != null) {
                queue.enqueue(x.rt);
                pointqueue.enqueue(x.rt.p);
            }
        }
        return pointqueue;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }
        Queue<Point2D> rectcontqueue = new Queue<Point2D>();
        range(rect, root, rectcontqueue);
        return rectcontqueue;
    }

    // private helper for all points that are inside the rectangle (or on the boundary)
    private void range(RectHV rect, Node x, Queue<Point2D> queue) {
        if (x == null) return;
        if (rect.contains(x.p)) queue.enqueue(x.p);
        // if Node's subtree (lb or rt) 's corresponding rectangles don't intersect with the query rectangle, do not explore it furthur
        // since there's no posibility the point (on the splitting line) will be contained in the query rectangle
        if (x.lb != null && x.lb.rect.intersects(rect)) range(rect, x.lb, queue);
        if (x.rt != null && x.rt.rect.intersects(rect)) range(rect, x.rt, queue);
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        if (root != null) {
            return nearest(root, p, root.p.distanceSquaredTo(p), root.p);
        }
        else return null;
    }

    // private helper for a nearest neighbor of point p; null if the symbol table is empty
    private Point2D nearest(Node x, Point2D p, double smallest, Point2D smallestpoint) {
        if (x == null) return smallestpoint;
        double rectdistance = x.rect.distanceSquaredTo(p);
        double itselfdistance = x.p.distanceSquaredTo(p);
        //  if the closest point discovered so far is not closer than the distance between the query point and the rectangle
        //  corresponding to a node, change the smallest point and keep exploring
        if (smallest > rectdistance) {
            if (itselfdistance < smallest) {
                smallest = itselfdistance;
                smallestpoint = x.p;
            }
        }
        // else there is no need to explore that node (or its subtrees)
        else {
            return smallestpoint;
        }
        if (x.lb != null && x.rt != null) {
            // choose first the subtree that is on the same side of the splitting line as the query point
            if (x.lb.rect.contains(p)) {
                smallestpoint = nearest(x.lb, p, smallest, smallestpoint);
                return nearest(x.rt, p, smallestpoint.distanceSquaredTo(p), smallestpoint);
            }
            else {
                // choose first the subtree that is on the same side of the splitting line as the query point
                smallestpoint = nearest(x.rt, p, smallest, smallestpoint);
                return nearest(x.lb, p, smallestpoint.distanceSquaredTo(p), smallestpoint);
            }
        }
        else if (x.lb != null) {
            // only one subtree is not null, explore that subtree
            return nearest(x.lb, p, smallest, smallestpoint);
        }
        else if (x.rt != null) {
            // only one subtree is not null, explore that subtree
            return nearest(x.rt, p, smallest, smallestpoint);
        }
        return smallestpoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> kdtreeST = new KdTreeST<Integer>();
        StdOut.println(
                "Please enter (x,y) position of your point to insert! x,y's ranges should be within 0 to 1");
        StdOut.println("Press ctrl + z to stop inserting points!");
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            kdtreeST.put(new Point2D(x, y), i);
            i += 1;
        }
        if (kdtreeST.isEmpty()) {
            StdOut.println("The KdTree Symbol Table is empty");
        }
        else {
            StdOut.println("The KdTree Symbol Table is not empty");
        }
        StdOut.println("The KdTree Symbol Table has " + kdtreeST.size() + " points!");
        Iterable<Point2D> points2Dall = kdtreeST.points();
        for (Point2D p : points2Dall) {
            StdOut.println(p.toString() + " 's val is " + kdtreeST.get(p));
            if (kdtreeST.contains(p)) {
                StdOut.println("The KdTree Symbol Table contains " + p.toString());
            }
        }
        RectHV rect = new RectHV(0.5, 0.5, 1, 1);
        StdOut.println(
                "The rectangle generated " + rect.toString() + " contains "
                        + kdtreeST.range(rect));
        Point2D point2Dquery = new Point2D(0.5, 0.5);
        StdOut.println(
                "The nearst point to point " + point2Dquery.toString() + " is " + kdtreeST
                        .nearest(point2Dquery).toString());
    }
}
