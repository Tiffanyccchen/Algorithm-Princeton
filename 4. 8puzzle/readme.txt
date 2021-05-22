/* *****************************************************************************
  *  Name:  陳育婷
  *  NetID:  R07H41005
  *  Couse /Teacher: 演算法/ 黃乾綱
  *  Operating system: Windows10
  *  Text editor / IDE: IntelliJ IDEA
 **************************************************************************** */
Programming Assignment 4: Slider Puzzle
/* *****************************************************************************
 *  Explain briefly how you represented the Board data type.
 **************************************************************************** */
Board is representended by 8 private instance variables, and 8 public instance methods.
8 instance variables are : n - storing puzzle's size
                           board - storing puzzle
                           manhattan - storing puzzle's manhattan distance to the goal board
                           hamming - storing puzzle's hamming distance to the goal board
                           goal - store whether the puzzle is the goal board
                           zerorow - store no tile (0)'s row position
                           zerocol - store no tile (0)'s col position
                           inversions - store number of inversions
8 public instance methods are stated in the API.
When the board is initialized, it will
1. Assign n, board's each element's value.
2. Change hamming and manhattan distance value if the board's (i,j) value is not the goal board's corresponding value.
In this way, when the client calls hamming and manhattan's distance, the Board type object doesn't need to recalculate every time
with O(n^2) complexity.
3. Determine goal value, assign it to false whenever the board's (i,j) value is not the goal board's corresponding value.
4. Record no tile (0)'s corresponding row and column,which is convenient for calculating inversions.
Out of 8 functions, only the isSolvable() includes real computation, others function are just calling corresponding
variables, which save a lot of time if the client calls functions several times.
/* *****************************************************************************
 *  Explain briefly how you represented a search node
  (board + number of moves + previous search node). *
 **************************************************************************** */
I created 3 private instance variables board, number of moves, and previous search node,
and created 3 corresponding instance methods to return the variables' values.
I also created 3 public instance methods : manhattanpriority() - calculate the board's manhattanpriority value,
                                           which is number of moves already made plus the board's manhattan distance
                                           isgoal() - whether the board is the goal board, this can be done
                                           by calculating its manhattan/ahamming distance.
                                           compareTo() - This is required function for implements Comparable interface.
                                           The board's value is determined by its manhattanpriority value.

/* *****************************************************************************
 *  Explain briefly how you detected unsolvable puzzles.
 *
 *  What is the order of growth of the running time of your isSolvable()
 *  method in the worst case as function of the board size n? Use big Theta
 *  notation to simplify your answer, e.g., Theta(n log n) or Theta(n^3).
 **************************************************************************** */
Description:
First, count the number of inversions of the puzzle. Starting from first position,
compare its value to 2 ~ n^2 positions (in row major order)'s values. If the first value is bigger, then increase
inversions variable by one. Repeating this procedure for following 2 ~ n^2 -1 positions' elements.
In each position, its value will only be compared to larger positions' values.

For odd size puzzle, if number of inversions is odd, the puzzle is solvable.
For even size puzzle, if number of inversions plus the row of the blank square is odd, the puzzle is solvable.
(This rules are stated in Assignment 4's web page)

Order of growth of running time: Theta(n^4) Since the function contains 4 nested loop,
1st loop iterates n times
2nd loop iterates n times
3rd loop iterates (n - number (0 ~ n)(depends on 1st loop's value)) times
3rd loop iterates (n - number (0 ~ n)(depends on 1st loop's value)) times
Therefore, the order of growth is Theta(n^4) (ignoring lower order terms).
/* *****************************************************************************
 *  For each of the following instances, give the minimum number of moves to
 *  solve the instance (as reported by your program). Also, give the amount
 *  of time your program takes with both the Hamming and Manhattan priority
 *  functions. If your program can't solve the instance in a reasonable
 *  amount of time (say, 5 minutes) or memory, indicate that instead. Note
 *  that your program may be able to solve puzzle[xx].txt even if it can't
 *  solve puzzle[yy].txt and xx > yy.
 **************************************************************************** */


                 min number          seconds
     instance     of moves     Hamming     Manhattan
   ------------  ----------   ----------   ----------
   puzzle28.txt     28          1.108        0.051
   puzzle30.txt     30          2.210        0.137
   puzzle32.txt     32 OOMError:Java heap space 0.738
   puzzle34.txt     34 OOMError:Java heap space 0.355
   puzzle36.txt     36 OOMError:Java heap space 4.481
   puzzle38.txt     38 OOMError:Java heap space 1.179
   puzzle40.txt     40 OOMError:Java heap space 0.868
   puzzle42.txt     42 OOMError:Java heap space 9.676

/* *****************************************************************************
 *  If you wanted to solve random 4-by-4 or 5-by-5 puzzles, which
 *  would you prefer: a faster computer (say, 2x as fast), more memory
 *  (say 2x as much), a better priority queue (say, 2x as fast),
 *  or a better priority function (say, one on the order of improvement
 *  from Hamming to Manhattan)? Why?
 **************************************************************************** */
A better priority function (say, one on the order of improvement from Hamming to Manhattan).
The reasons can be stated in two ways.
1. Emperically, we can see that from the above experiments, using hamming instead of manhattan
priority function can causes a speed and memory loss way above two times larger, therefore,
as the number of solutions grow, even the computer with 2 times as much memory or 2 times as fast
or a priority with 2 times as fast can't avoid the OOM error. Since for hamming distance,
we still need to go through the same sequence of boards we visited.
2. Theoretically, using hamming distance causes many ambiguity and tied values for many candidate boards,
since hamming distance just consider the number of tiles in the wrong position instead of the
manhattan distance calculating the vertical and horizontal distances from all the tiles to their goal positions.
Therefore; hamming priority function will force the Solver to explore many sequences of possible solutions,
and maintaining all of them takes a lot of space and time. Therefore, as the minimum number of moves gets larger,
the possible solutions explodes, and OOM error inevitably occurs. This issue can only be solved by changing
priority function.
All in all, from this problem, we again witness the importance of a good algorithm design.

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
No.
/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
1. In Board class, When implementing neighbor() and isSolvable() methods, at first I forget to exclude board value = 0 scenario
(corresponding to no tile). Therefore, it provides some weird results. Plus, in isSolvable() method, the fourth
nested loop initial value should depends on whether the second value to be compared is at the same row as the first
value. That's a point I missed at first.
2. In Solver class, the initial mistake I made is when iterating over solution boards. At
first, the number of moves is correct but the solutions sometimes are more than number of moves and
some of the consecutive boards are not neighbors. After that, I found that I first enqueue all
minimum heap's minimum board in the solution queue, that causes the solution may not be directly a path
from a game tree (may go for other children first and go back). Instead, I should create a stack and
push the goal board solution and the previous board until I met the initial board.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
I completed it on my own.
/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
I definitely learned a lot from this assignment.
1. I learned how to make use of plenty of data structures
to efficiently solve a task.
2. I learned to calculate some functions' required values in the constructor
first to prevent extra time waste when repeatedly calling the function.
3. I also learned about the fun slider puzzle game and how a board game can be solved automatically
by algorithms.
I absolutely enjoyed doing it.
