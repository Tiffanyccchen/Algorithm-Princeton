/* *****************************************************************************
  *  Name:  陳育婷
  *  NetID:  R07H41005
  *  Couse /Teacher: 演算法/ 黃乾綱
  *  Operating system: Windows10
  *  Text editor / IDE: IntelliJ IDEA
 **************************************************************************** */

Programming Assignment 3: Autocomplete

/* *****************************************************************************
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that is equal to the search key.
 **************************************************************************** */
Use usual binary search algorithm. Plus one condition : if the term is equal to the key
and its previous term is equal to the key too, set high bar equal to middle element ( the found match term) - 1.
In this way, under the fact that the system knows there are still matched keys in the front, it will keep searching
the range between low bar and found matched query's position - 1. If the found query's previous term
is not the match, it will return the matched'query position - mid value.
/* *****************************************************************************
 *  Identify which sorting algorithm (if any) that your program uses in the
 *  Autocomplete constructor and instance methods. Choose from the following
 *  options:
 *
 *    none, selection sort, insertion sort, mergesort, quicksort, heapsort
 *
 *  If you are using an optimized implementation, such as Arrays.sort(),
 *  select the principal algorithm.
 **************************************************************************** */

Autocomplete() : I use Arrays.sort(). Since Term's query is a reference type.
In Arrays.sort(), they use tim sort (a hybrid of merge sort and insertion sort, performance is better or equal to merge sort).
reference types use merge sort because
1. Copying the pointers take less memory usage
2. Guaranteed performance and stability
3. Worst number of compares take less than quick sort average number, while compare using references requires at least
4 memory accesses for reference types, less compare is crucial in performance.

allMatches() :
1. For finding prefix's match indexes: since term's queries are reference types. Use tim sort too. Reasons are stated above.
2. For sorting using descending weight : Since Term's weight is a reference type (type Long), Arrays.sort() use tim sort.Reasons are stated above.

numberOfMatches() : Since term's queries are reference types. Use tim sort too. Reasons are stated above.

/* *****************************************************************************
 *  How many compares (in the worst case) does each of the operations in the
 *  Autocomplete data type make, as a function of both the number of terms n
 *  and the number of matching terms m? Use Big Theta notation to simplify
 *  your answers.
 *
 *  Recall that with Big Theta notation, you should discard both the
 *  leading coefficients and lower-order terms, e.g., Theta(m^2 + m log n).
 **************************************************************************** */

Autocomplete():     Theta(n*logn(base 2)
Sorting n elements using tim sort makes at most n * logn compares.
Refer to Algorithm textbook p275 and p279.
(Tim sort's performance is better or equal than merge sort)

allMatches():       Theta(logn(base 2) + mlogm(base 2))
Binary Search takes no more than logn(base 2) + 1 compares for a search.
After finding m matched terms, sorting m terms using tim sort uses at most m * logm compares.

numberOfMatches():  Theta(logn(base 2))
Binary Search takes no more than logn(base 2) + 1 compares for a search.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
No. However, I know this algorithm can be imporoved based on the Enrichment section
in the assignment checklist. Still, this algorithm at least does not take quadratic time.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *
 *  Also include any resources (including the web) that you may
 *  may have used in creating your design.
 **************************************************************************** */
I surveyed Algorithm text book for theoretical help.
BinarySearchDeluxe : 1.1 p47
Comparator implementation and functionality : 2.5 p338 - p340

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
1. Confused by how the comparator should be used, after read textbook p338 - p340 thoroughly, I finally completely understand.
2. The Binary Search Deluxe is actually a variant of Binary Search.
   When I set third condition eg. in lastIndexof() : if(mid < hi) lo = mid, I found that the algorithm will go into an infinite loop on some tasks.
   This happens because since you don't increase lo, then since eg. 3 + (4-3)/2 = 3, the program will stuck at one number permanently.
   Therefore, I change the condition to if(comparator.compare(a[mid], a[mid + 1]) == 0) lo = mid + 1, but still find bugs whe mid = last index of array.
   Finally, I add a condition : mid < array.length - 1, then finally it works perfectly.
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
I think this assignment is practical and fun. The task itself happens every day in our life.
We search for videos, technical resources, or gossips all the time.
To have the change to take a sneak peek at how it can be done using searching and sorting algorithms
is fulfilling and refreshing.
