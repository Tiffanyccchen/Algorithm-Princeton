/* *****************************************************************************
  *  Name:  陳育婷
  *  NetID:  R07H41005
  *  Couse /Teacher: 演算法/ 黃乾綱
  *  Operating system: Windows10
  *  Text editor / IDE: IntelliJ IDEA
 **************************************************************************** */

Programming Assignment 5: Kd-Trees

/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  kd-tree data structure.
 **************************************************************************** */
 The node contains 5 instance variables.
 1. Point2D p -the point
 2. private Value val - the symbol table maps the point to this value
 3. private RectHV rect - the axis-aligned rectangle corresponding to this node
 4. private Node lb - the left/bottom subtree
 5. private Node rt - the right/top subtree
 The constructor will assign values of p, val, rect. The lb, rt are assigned explicitly.
 I have considered whether the store the split orientation (vertical or horizontal) in the node.
 After consideration, I realized that it's not necessary since every level's orientation is guaranteed
 to alternate. It is only needed when putting the value in the tree since we need to create the rectangle to store in the Node.
 Therefore, not storing it won't affect the performance, and the KdTree takes less space.

/* *****************************************************************************
 *  Describe your method for range search in a kd-tree.
 **************************************************************************** */
1. Created a queue for storing points in the range.
2. Start with the root, if the node's corresponding point is in the range, enqueue the point.
3. Explore left and right subtree's node, if the node's corresponding rectangle does not intersect with the range,
stop exploring that subtree, else keep exploring the subtree and repeat 2. until all traversed node is null.
4. Return the queue.

/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a kd-tree.
 **************************************************************************** */
1. If the root isn't null, initialze the nearest point and nearest distance to root's corresponding point and its squared distance to the query point.
2. Start with the root, if the distance between the query point and the rectangle corresponding to a node is not closer than the closest point discovered so far ,
stop exploring furthur and return the nearest point.
3. Else if the distance between the query point and the rectangle corresponding to a node is closer than the closest point discovered so far ,
change the nearest point and nearest squared distance and keep exploring the node's subtrees.
4. Check whether the node's left and right subtree is null, if two subtree are all not null,
check which node's correspondng rectangle contains the query point(the subtree that is on the same side of the splitting line as the query point),
start exploring that subtree first.The closest point found while exploring the first subtree may enable pruning of the second subtree.
5. If only one subtree is not null, explore that subtree only.
6. If all the subtrees are null, means we have explored all possible areas, so return the nearest point found so far.

I design it in a way that we only need the choosing subtree rule when all two subtrees are not null, this one extra condition
can help the function be called one time less each whenever one of the subtree is null, therefore is more efficient.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, using one digit after the decimal point
 *  for each entry. Use at least 1 second of CPU time.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */
 This experiment is implemented in NearestNeighborTime program.
                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:        11                  / 1.089          =   10.101

KdTreeST:       352559              / 1              =   352559

Note: more calls per second indicates better performance.
/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
No bugs/ limitation as far as I know.

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
 PointST is easy cause it is most of its method is wrapped by RedBlackTree. Therefore I only discussed problems in KdTreeST.
1. Nearest method in KdTreeST :
I think the most difficult part I encountered is the nearest method.
Although I made sure I thoroughly understand the KdTree's mechanism before implementing it,
I still encountered a lot of bugs during implementation, like when I misused the distance between the query point and the rectangle
corresponding to a node as a condition to whether to update nearest point. The condition should be the distance between the query point
and the point corresponding to a node instead. The misused condition is used as a pruning rule but not an updating rule.
2. Put method in KdTreeST :
At first I used the argument variables' xmin, ymin, xmax, ymax values in the recursive put method if those value are unchanged in that split.
As a result, the rectangle formed is not correct and almost won't prune.
I found this problem in the test client cause the range method does not return correct points.
Then I changed it to used the argument's node 's xmin, ymin, xmax, ymax values in the recursive put method if those value are unchanged in the split.
Therefore, the rectangle boundaries information will be carried recursively. That's the correct implementation.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
I wrote it on my own.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
I leared a lot in this assignment too. To implement a data structure which can
solve particular domain of problems efficiently is not easy.
Implementing the data structure discovered by others takes a lot of work and consideration,
not to mention to come up with this new data structure.
This assignment strengthen my understanding that algorithms is complemented with data structures.
To have an efficient algorithm, a good data structure for storing values is essential and crucial.
To design a widely applicable data structure, we need the help of efficient algorithms for constructing and storing values in the data structure.
I spent a lot of time on this assignment, but I still enjoy doing it.
