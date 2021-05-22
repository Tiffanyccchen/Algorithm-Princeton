/* *****************************************************************************
  *  Name:  陳育婷
  *  NetID:  R07H41005
  *  Couse /Teacher: 演算法/ 黃乾綱
  *  Operating system: Windows10
  *  Text editor / IDE: IntelliJ IDEA
 **************************************************************************** */

Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */
I use two symbol tables to store synsets. I choose using hashing with linear probing symbol table
to stor the information.
1. Reason for using symbol table:
    We need to transfer nouns user types to corresponding synset ids to let shortest common ancestor(sca)
    object find the length and sca, and transfer the ids back to synsets themselves.
    Therefore, we need a mapping relationship where we can find value based on their keys. That is what symbol tables do.
2. Reason for choosing using hashing with linear probing symbol table:
    Since in here, key order is not important (does no help in finding shortest ancestral path's length and shortest
    common ancestor), then using hashing can achieve expected constant time per operation, which is the fastest
    among all symbol table implementations.
3. Details for two symbol tables.
    One is to find a synset according to its id, where a key is id and a synset is value. This relationship is one-to-one, therefore,
using symbol table is appropriate. The other symbol table is used to find synset ids according to a noun contained in those synsets. This is for letting
the function receiving noun can be transferred into synsets id contained in it and thus sca object which reiceives ids as
arguments can return desired values back.
    The first symbol table : key : id (type : integer); value : synset (type:String)
    The second symbol table : key : noun (type : String); value : synset ids containing that noun (type:Bag with int items)

/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */
I use directed graph (DiGraph class offered in algs4 package) to store information in hypernyms.txt.
1. Reason I use graph:
Hypernym's relationship is complex. A word can have multiple hypernyms, and it can be indirectly and directly linked together.
In this context, neither queue/stack/array like one-type sequeunce storing data type or symbol table like key/value pair
data type is appropriate. Graph is the data type which can record this kind of relationship, where words are vertices,
and hyperntm relationship is edge. Here, edge is not weighted, so we do not have to use weithed-edge graph.

2. Reason I use directed graph:
Because there is direction information between two vertices. We need to identify which synset is hypernym and which synset
is hyponym for vertices on both side of edge instead of just knowing that they are connected.
Therefore, we define the vertices an edge pointed to to be the hypernym, then we can identify their relationship easily.

/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithm? Express your answer as a function of the
 *  number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify
 *  your answer.
 **************************************************************************** */

Description:
Since we use DFS to check whether the graph has a directed cycle and has a single root.
Therefore,the order of growth is the dfs's order of growth. DFS marks all the vertices in a digraph reachable from
a given set of sources in time propotional to the sum of the outdegrees of the vertices marked (p 570 in Algorithms textbook)
1. The total vertices available for marked is V.
2. The maximum sum of outdegrees of all vertices is E.
3. Plus, there must be vertices reachable from multiple vertices. Therefore, for vertices marked, it will not be explored twice.

Order of growth of running time:
Therefore, the Order of growth of running time in Big Theta notation is O(V+E).

/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times. Express your answers as functions
 *  of the number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify your
 *  answers.
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description:
The best case for compute the shortest common ancestor is that a pair in two subsets of vertices are equal. In that case,
the algorithm takes constant time.
The worst case for compute the shortest common ancestor is that the ancestor is the root, and the argument vertices together
can reach all V vertices and E edges.
My algorithm will only travel to all vertices and edges reachable from the argument vertices until they reach the root.
I didn't use a BreadthFirstDirectedPaths object. I implemented an algorithm meeting the additional performance requirement on
my own.

                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                O(1)                O(number of vertices and edges reachable from the argument vertices)
                                            which is O(E+V) in worst case (but better than naive solution which is O(E+V) in most cases
                                            using BreadthFirstDirectedPaths object)

ancestor()              O(1)                O(number of vertices and edges reachable from the argument vertices)
                                            which is O(E+V) in worst case (but better than naive solution which is O(E+V) in most cases
                                            using BreadthFirstDirectedPaths object)

lengthSubset()          O(1)                O(number of vertices and edges reachable from the argument vertices)
                                            which is O(E+V) in worst case (but better than naive solution which is O(E+V) in most cases
                                            using BreadthFirstDirectedPaths object)

ancestorSubset()        O(1)                 O(number of vertices and edges reachable from the argument vertices)
                                             which is O(E+V) in worst case (but better than naive solution which is O(E+V) in most cases
                                             using BreadthFirstDirectedPaths object)

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
Not any as far as I know.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
No.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
The most challenging problem I encounted is writing sca and scp's length in ShortestCommonAncestor class, which is
also the essence of this assignment. I will describe two challenges regarding to it.

1. Addition performance requirement : letthe methods length(), lengthSubset(), ancestor(), and ancestorSubset()
take time proportional to the number of vertices and edges reachable from the argument vertices (or better)

This is really hard because I can't use functions in  BreadthFirstDirectedPaths to compute since it can take O(E+V) in worst case.
I came up with a bright solution in mind after doing some thinking and writing on my notebooks.
However, during implementation, I still encounter some problems.
    1. I didn't set the condition to check whether the queue computed now is empty or not since in the first condition I only ensure
one or all of them are not empty.
    2. To expand the solution to a subset of vertices takes me a little time.
However, I conquered them finally and get great results.

2. Using created class Tuple<X,Y> to store already computed sca and sap's length for two integer vertices

At first, I add this because I think it can speed up computing since we do not need to recompute the value we have already computed.
It is especially useful when computing scp for two subsets of vertices.
However, the problem is that when retrieving Tuple keys, it can recognize Tuples with same (x,y) as same key.
Every time a tuple I created is a new object. Therefore, when compare by reference, the key and the tuple I feed are not the same.
Therefore, I learned to override equals() and hashCode() functions to let symbol table recognize equality and hash value
only based on (x,y) values. And it worked perfectly.


/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
I wrote it on my own.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
I have learned a lot. I did it from scratch.
I learned how to create a custom class
to store values and how to override function when comparing keys.
I learned how to put your ideas into reality and solve problems on the way to success.
It's challenging but fulfilling, so I enjoyed doing it.
