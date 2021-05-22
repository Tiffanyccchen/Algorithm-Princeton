/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the ShortestCommonAncestor.
 *               In the test client, the client can read in a formatted directed-graph file.
 *               The client then can read in the two vertices ids together until they want to stop.
 *  Response:    The system will print the the shortest ancestral path (SCP) 's length and the shortest common ancestor
 *               (type : interger) of the two vertices ids the user types.
 *
 *  Compilation:  javac-algs4 ShortestCommonAncestor.java
 *  Execution :  java-algs4 ShortestCommonAncestor
 *  Dependencies : Digraph, DirectedCycle, Bag, Queue, LinearProbingHashST
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {
    private final Digraph G; // store digraph
    private final LinearProbingHashST<Tuple<Integer, Integer>, Tuple<Integer, Integer>> storesingle
            // store two synset ids' sca and sap's length
            = new LinearProbingHashST<Tuple<Integer, Integer>, Tuple<Integer, Integer>>();

    // create a data type to store sca and sap's length for two integer vertices
    public class Tuple<X, Y> {
        private final X x; // first value
        private final Y y; // second value

        // constructor
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        // return x value
        public X getX() {
            return this.x;
        }

        // return y value
        public Y getY() {
            return this.y;
        }

        // Replace equals for comparing values
        @Override
        public boolean equals(Object other) {
            // compare the values of both objets.
            // return true on success;
            Tuple<X, Y> that = (Tuple<X, Y>) other;
            if (this.getX().equals(that.getX()) && this.getY().equals(that.getY())) {
                return true;
            }
            else {
                return false;
            }
        }

        // Depends only on x,y value
        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 1;
            hash = prime * hash + (Integer) x;
            hash = prime * hash + (Integer) y;
            return hash;
        }

    }

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException();
        this.G = new Digraph(G);
        // check DAG
        assert !new DirectedCycle(this.G).hasCycle();
        // check single rooted
        assert checkrooted();
    }


    // check if G is rooted DAG
    private boolean checkrooted() {
        boolean[] marked = new boolean[this.G.V()];
        Bag<Integer> root = new Bag<Integer>();
        for (int v = 0; v < this.G.V(); v++) {
            dfs(v, marked, root);
            // if root size > 2 or = 0 , this is not a single rooted DAG
            if (root.size() != 1) {
                return false;
            }
        }
        return true;
    }

    // depth first search for checking whether G is rooted DAG
    private void dfs(int v, boolean[] marked, Bag<Integer> root) {
        marked[v] = true;
        // identify last vertice of the path (i.e.,outdegree = 0) -> for rooted DAG it should only be the root
        if (this.G.outdegree(v) == 0) root.add(v);
        for (int w : this.G.adj(v)) {
            if (!marked[w]) dfs(w, marked, root);
        }
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();

        // if two vertices are the same, length is 0
        if (v == w) return 0;

        // check if we have computed its value before
        if (storesingle.contains(new Tuple<Integer, Integer>(v, w))) {
            Tuple<Integer, Integer> ancestorlength = storesingle
                    .get(new Tuple<Integer, Integer>(v, w));
            return ancestorlength.getY();
        }

        Queue<Integer> p = new Queue<Integer>();
        Queue<Integer> q = new Queue<Integer>();
        Integer[] distTov = new Integer[this.G.V()];
        Integer[] distTow = new Integer[this.G.V()];
        distTov[v] = 0;
        distTow[w] = 0;
        p.enqueue(v);
        q.enqueue(w);

        commonbfs(v, w, p, q, distTov, distTow);

        Tuple<Integer, Integer> ancestor = storesingle.get(new Tuple<Integer, Integer>(v, w));
        return ancestor.getY();
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();

        // if two vertices are the same, ancestor is v
        if (v == w) return v;

        // check if we have computed its value before
        if (storesingle.contains(new Tuple<Integer, Integer>(v, w))) {
            Tuple<Integer, Integer> ancestorlength = storesingle
                    .get(new Tuple<Integer, Integer>(v, w));
            return ancestorlength.getX();
        }

        Queue<Integer> p = new Queue<Integer>();
        Queue<Integer> q = new Queue<Integer>();
        Integer[] distTov = new Integer[this.G.V()];
        Integer[] distTow = new Integer[this.G.V()];
        distTov[v] = 0;
        distTow[w] = 0;
        p.enqueue(v);
        q.enqueue(w);

        commonbfs(v, w, p, q, distTov, distTow);

        Tuple<Integer, Integer> ancestor = storesingle.get(new Tuple<Integer, Integer>(v, w));
        return ancestor.getX();
    }

    // BFS from single source
    private void commonbfs(int v, int w, Queue<Integer> p, Queue<Integer> q, Integer[] distTov,
                           Integer[] distTow) {
        while (!p.isEmpty() || !q.isEmpty()) {
            // compute shortest path from v using bfs
            if (!p.isEmpty()) {
                int m = p.dequeue();
                for (int n : G.adj(m)) {
                    if (distTov[n] == null && distTow[n] == null) {
                        distTov[n] = distTov[m] + 1;
                        p.enqueue(n);
                    }
                    // if w has already reached this vertice, then n is the sca, and the length from v,w to n's sum is scp's length,
                    // can stop computing
                    else if (distTov[n] == null) {
                        distTov[n] = distTov[m] + 1;
                        // StdOut.printf("%2d %2d %2d %2d ", v, w, n, distTov[n] + distTow[n]);
                        storesingle.put(new Tuple<Integer, Integer>(v, w),
                                        new Tuple<Integer, Integer>(n, distTov[n] + distTow[n]));
                        return;
                    }
                }
            }
            // compute shortest path from w using bfs
            if (!q.isEmpty()) {
                int s = q.dequeue();
                for (int t : G.adj(s)) {
                    if (distTow[t] == null && distTov[t] == null) {
                        distTow[t] = distTow[s] + 1;
                        q.enqueue(t);
                    }
                    // if v has already reached this vertice, then t is the sca, and the length from v,w to t's sum is scp's length,
                    // can stop computing
                    else if (distTow[t] == null) {
                        distTow[t] = distTow[s] + 1;
                        storesingle.put(new Tuple<Integer, Integer>(v, w),
                                        new Tuple<Integer, Integer>(t, distTov[t] + distTow[t]));
                        return;
                    }
                }
            }
        }
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new java.lang.NullPointerException();

        Queue<Integer> subsetremainA = new Queue<Integer>();
        Queue<Integer> subsetremainB = new Queue<Integer>();

        int min = Integer.MAX_VALUE;
        int ancestor = 0;

        // store the pair we haven't computed in two queues respectively
        for (int a : subsetA) {
            for (int b : subsetB) {
                // if one of the pair in two subsets are equal, then the sca must be it ( a vertice is its ancestor according to checklist)
                // and the length is 0
                if (a == b) return 0;
                if (storesingle.contains(new Tuple<Integer, Integer>(a, b))) {
                    Tuple<Integer, Integer> ancestorlength = storesingle
                            .get(new Tuple<Integer, Integer>(a, b));
                    if (ancestorlength.getY() < min) {
                        ancestor = ancestorlength.getX();
                        min = ancestorlength.getY();
                    }
                }
                else {
                    subsetremainA.enqueue(a);
                    subsetremainB.enqueue(b);
                }
            }
        }

        // check if we have computed all pair of vertices' values before
        if (subsetremainA.size() == 0 && subsetremainB.size() == 0) return min;
        Queue<Integer> A = new Queue<Integer>();
        Queue<Integer> B = new Queue<Integer>();
        Integer[] distToA = new Integer[this.G.V()];
        Integer[] distToB = new Integer[this.G.V()];
        for (int s : subsetremainA) {
            distToA[s] = 0;
            A.enqueue(s);
        }
        for (int s : subsetremainB) {
            distToB[s] = 0;
            B.enqueue(s);
        }

        return commonbfs(A, B, distToA, distToB, ancestor, min, false);
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new java.lang.NullPointerException();
        Queue<Integer> subsetremainA = new Queue<Integer>();
        Queue<Integer> subsetremainB = new Queue<Integer>();

        int min = Integer.MAX_VALUE;
        int ancestor = 0;

        // store the pair we haven't computed in two queues respectively
        for (int a : subsetA) {
            for (int b : subsetB) {
                // if one of the pair in two subsets are equal, then the sca must be it ( a vertice is its ancestor according to checklist)
                // and the length is 0
                if (a == b) return a;
                if (storesingle.contains(new Tuple<Integer, Integer>(a, b))) {
                    Tuple<Integer, Integer> ancestorlength = storesingle
                            .get(new Tuple<Integer, Integer>(a, b));
                    if (ancestorlength.getY() < min) {
                        ancestor = ancestorlength.getX();
                        min = ancestorlength.getY();
                    }
                }
                else {
                    subsetremainA.enqueue(a);
                    subsetremainB.enqueue(b);
                }
            }
        }

        // check if we have computed all pair of vertices' values before
        if (subsetremainA.size() == 0 && subsetremainB.size() == 0) return ancestor;
        Queue<Integer> A = new Queue<Integer>();
        Queue<Integer> B = new Queue<Integer>();
        Integer[] distToA = new Integer[this.G.V()];
        Integer[] distToB = new Integer[this.G.V()];
        for (int s : subsetremainA) {
            distToA[s] = 0;
            A.enqueue(s);
        }
        for (int s : subsetremainB) {
            distToB[s] = 0;
            B.enqueue(s);
        }

        return commonbfs(A, B, distToA, distToB, ancestor, min, true);
    }

    // BFS from multiple source
    private int commonbfs(Queue<Integer> A, Queue<Integer> B, Integer[] distToA, Integer[] distToB,
                          int ancestorint, int min, boolean ancestor) {
        while (!A.isEmpty() || !B.isEmpty()) {
            // compute shortest path from subsetremainA using bfs
            if (!A.isEmpty()) {
                int m = A.dequeue();
                for (int n : G.adj(m)) {
                    if (distToA[n] == null && distToB[n] == null) {
                        distToA[n] = distToA[m] + 1;
                        A.enqueue(n);
                    }
                    // if vertices in subsetRemainB has already reached this vertice, then n is the sca, and the length from subsetRemainA & subsetRemainB
                    // to n's sum is scp's length, (if smaller than the min. we have computed before)
                    // can stop computing
                    else if (distToA[n] == null) {
                        distToA[n] = distToA[m] + 1;
                        if (min > distToA[n] + distToB[n]) {
                            min = distToA[n] + distToB[n];
                            ancestorint = n;
                        }
                    }
                }
            }
            // compute shortest path from subsetremainB using bfs
            if (!B.isEmpty()) {
                int s = B.dequeue();
                for (int t : G.adj(s)) {
                    if (distToB[t] == null && distToA[t] == null) {
                        distToB[t] = distToB[s] + 1;
                        B.enqueue(t);
                    }
                    // if vertices in subsetRemainA has already reached this vertice, then t is the sca, and the length from subsetRemainA & subsetRemainB
                    // to t's sum is scp's length, (if smaller than the min. we have computed before)
                    // can stop computing
                    else if (distToB[t] == null) {
                        distToB[t] = distToB[s] + 1;
                        if (min > distToA[t] + distToB[t]) {
                            min = distToA[t] + distToB[t];
                            ancestorint = t;
                        }
                    }
                }
            }
        }
        // return sca
        if (ancestor) return ancestorint;
        // return scp's length
        return min;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            Bag<Integer> comparesubsetbase = new Bag<Integer>();
            comparesubsetbase.add(1);
            comparesubsetbase.add(2);
            Bag<Integer> comparesubsettwo = new Bag<Integer>();
            comparesubsettwo.add(v);
            comparesubsettwo.add(w);
            int lengthSubset = sca.lengthSubset(comparesubsetbase, comparesubsettwo);
            int ancestorSubset = sca.ancestorSubset(comparesubsetbase, comparesubsettwo);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.printf("lengthsubset = %d, ancestorsubset = %d\n", lengthSubset, ancestorSubset);
        }
    }
}
