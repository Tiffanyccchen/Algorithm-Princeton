/* *****************************************************************************
 *  Name:  陳育婷
 *  NetID:  R07H41005
 *  Couse /Teacher: 演算法/ 黃乾綱
 *  Operating system: Windows10
 *  Text editor / IDE: IntelliJ IDEA
 **************************************************************************** */

Programming Assignment 2: Deques and Randomized Queues


/* *****************************************************************************
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 **************************************************************************** */
Deque :
1. Choice : Use doubled linked list.
2. Requirement : Add first/last, remove first/last must use constant number of operations. Each iterator operation (including construction) must take constant time.
3. Reason for choosing the data structure :
   If we use array, then insert/remove from the front will take n(array's length) operations, which doesn't fulfill the requirement.
   If we use doubled linked list, because we record first and last element , so both insert/remove from the front/end can be accomplished in constant time.

Randomized Queue :
1. Choice : Use array.
2. Requirement : Each randomized queue operation (besides creating an iterator) must take constant amortized time. Constructing an iterator must take Θ(n) time; the next() and hasNext() operations must take constant time.
3. Reason for choosing the data structure :
   If we use linked list, then the deque(randomly remove one element)/sample operations will takes at worst n operations for finding the element to remove without array's indexing function.
   Using array, with the index's help, we can access the sampled element and exchange it with the last element, remove the last index's element and set it to null. This approach only needs constant number of operations.
   Also, although using resizing to resize array when the number of elements is full or less than 1/4 takes O(n), but overall operations still take constant amortized time
   proved in textbook p198 - 199.
/* *****************************************************************************
 *  How much memory (in bytes) do your data types use to store n items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 **************************************************************************** */
Deque:              ~  _48 *n + 4____  bytes
From textbook p200 -203, we know that an object overhead typically takes 16 pytes, for a Node object an extra 8 bytes needed for
a reference to the enclosing instance. Plus the 8 byte each for item, previous link, and next link 's reference. A node
in a double linked list takes 48 bytes memory. Therefore; n nodes take 48n bytes, plus 4 bytes for recording size of current linked list,
the total memory for deque is 48*n + 4 in total in the worst case.

Randomized Queue:   ~  _24 + 16n____  bytes
n : number of elements in the array with values!!!
An array of objects is an array of reference to the objects. An array contains 16 bytes for overhead, 4 bytes for recording
current array's length, and 4 bytes for padding to multiplier of 8.
Plus, a reference to an object's place takes 8 bytes. If we happen to resize array to double its original length when the
array is full (that is, double length from n to 2n), then the total bytes required for references in the worst case is
8*(2n) = 16n.
Therefore; the worst case's memory usage is 24 + 16n.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
No.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
 I completed it on my own.



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
1. To determine which data structure to use to implement Deque and RandomizedQueue. At first, I think since remove involves
only constant number of operations,then the array must be the choice.However; I didn't consider that in the deque, adding
element from the front for array takes n operations. This doesn't conform to the requirement. Nevertheless; the linkedlist I
learned in the textbook need to traverse n times to reach the end, which takes n operations to insert at the end.
Then I think of a data structure I learned called double linked list, which can record first and last node. As a result, both
insert/remove from the back can be in O(1) time. Thus, I chose double linked list for Deque, and array for RandomizedQueue.

2. The extra credit part requires using only a constant amount of memory plus either one Deque or RandomizedQueue object of maximum size at most k
This is hard to accomplish at the first look. So I started google resources(but not exact code) to help realizing it.
This website(https://zhuanlan.zhihu.com/p/29178293)'s resource really help me achieve this requirement and I learn new knowledge from it too.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
This assignment let us build two abstract data types (ADT) from the scratch. Besides, the nature of two ADTs prompts us
to choose two different data structures to achieve most efficient operations. I think the design goalis to let us know the importance of choosing right data structure.
To make two ADTs having most efficient operations, you need to have thoroughunderstanding of both data structures and abstract data types' requirements and characteristics.
Efficient operations of ADTs are crucial for future algorithms/real applications using these ADTs. All in all, this assignment is interesting and we really can learn a lot
from it.
