/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Description: This program implements the Randomized data structure.
 *    In the test client, the client takes the command line argument sequentially.
 *    argument options : {eq : enqueue; dq : dequeue; sp :sample; ite : iterate all items available randomly;}}
 *    - Enter one of four options
 *    - Enter ctrl + Z (on Windows) to exit operations
 *  Response : After each enqueue/dequeue/sample option, the system will print the item you enqueue/dequeue/sample, and show how many items left in the randomizedqueue;
 *  After the iteration option, the system will print out all items in the randomizedque randomly without repetition
 *
 *  Compilation:  javac-algs4 RandomizedQueue.java
 *  Execution :  java-algs4 RandomizedQueue
 *  Dependencies : None
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on stack

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the array's length
    private void resize(int max) {
        // initialize to max length
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null !");
        }
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) {
            throw new NoSuchElementException("The Randomizedqueue is empty !");
        }
        int sel = StdRandom.uniform(n);
        Item select = a[sel];
        // exchange randomly select item with last item for removal
        a[sel] = a[n - 1];
        a[n - 1] = select;
        // enable loitering
        a[--n] = null;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return select;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int sel = StdRandom.uniform(n);
        return a[sel];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int i = 0; // first iteration point to the first element in the array
        private int[] randnumarray = new int[n];  // a randomized array to decide iteration order

        // construct a randomized array with integers 0 ~ n-1
        public ListIterator() {
            for (int j = 0; j < n; j++) {
                randnumarray[j] = j;
            }
            StdRandom.shuffle(randnumarray);
        }

        public boolean hasNext() {
            return i <= (n - 1);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate !");
            }
            // retur array's (random array's ith index's number) element
            return a[randnumarray[i++]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedque = new RandomizedQueue<Integer>();
        for (int i = 0; i < 5; i++)
            randomizedque.enqueue(i);
        for (int a : randomizedque) {
            for (int b : randomizedque)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        StdOut.println(
                "options : {eq : enqueue; dq : dequeue; sp :sample; ite : iterate all items available randomly;}");
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("eq")) {
                StdOut.println("Enter a string : ");
                String data = StdIn.readString();
                queue.enqueue(data);
                StdOut.println(queue.size() + " items left on randomizedque");
            }
            else if (item.equals("dq")) {
                StdOut.println("Remove a string randomly : remove " + queue.dequeue());
                StdOut.println(queue.size() + " items left on randomizedque");
            }
            else if (item.equals("sp")) {
                StdOut.println("Sample a string randomly : " + queue.sample());
                StdOut.println(queue.size() + " items left on randomizedque");
            }
            else if (item.equals("ite")) {
                for (String a : queue) {
                    StdOut.print(a + ' ');
                }
                StdOut.print('\n');
            }
            else {
                throw new IllegalArgumentException("Must be one of the four options");
            }
            StdOut.println(
                    "options : {eq : enqueue; dq : dequeue; sp :sample; ite : iterate all items available randomly;}");
        }
    }
}
