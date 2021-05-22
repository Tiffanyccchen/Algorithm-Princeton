/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Description: This program implements the Deque data structure.
 *    In the test client, the client takes the command line argument sequentially.
 *    argument options : {af : addFirst; al : addLast; rf :removeFirst; rl : removeLast; ite :Iterate all items available from front;}
 *    - Enter one of five options
 *    - Enter ctrl + Z (on Windows) to exit operations
 *  Response : 1.After each remove/add option, the system will print out the item you remove/add, and show how many items left in the deque;
 *  2. After the iteration option, the system will print out all items in the deque now from the front sequentially.
 *
 *  Compilation:  javac-algs4 Deque.java
 *  Execution :  java-algs4 Deque
 *  Dependencies : None
 ***************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first; // keep track of first node
    private Node last; // keep track of last node
    private int n; // keep track of number of items in deque now

    // helper linked list class
    private class Node {
        private Item item; // Node's data
        private Node next; // link to next node
        private Node prev; // link to previous node
    }

    // construct an empty deque
    public Deque() {
        // instanstize first/ last node, current number of nodes is zero
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null !");
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;
        // if the deque is not empty, then the second front node needs to be linked to new first node
        if (!isEmpty()) oldfirst.prev = first;
        // if this is the only item in the deque, then last is first, this is how we link items added from front and back together
        if (isEmpty()) last = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null !");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        // if the deque is not empty, then the second last node needs to be linked to new last node
        oldlast.next = last;
        // if this is the only item in the deque, then first is last, this is how we link items added from front and back toge
        if (isEmpty()) first = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Dequeue is empty !");
        }
        Item item = first.item;
        // change deque's first node to its original second front node
        first = first.next;
        // if there are nodes left in the deque after removal, then we need to update new first node's prev link to null
        if (first != null) first.prev = null;
        // if there's no node in the deque after removal, then last node's reference should be updated to null too
        else last = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Dequeue is empty !");
        }
        Item item = last.item;
        // change deque's last node to its original second last node
        last = last.prev;
        // if there are nodes left in the deque after removal, then we need to update new last node's next link to null
        if (last != null) last.next = null;
        // if there's no node in the deque after removal, then first node's reference should be updated to null too
        else first = null;
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first; // set current iterate node to first node

        public boolean hasNext() {
            // test if current iterate node reach the end of the deque
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Item item = current.item;
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate !");
            }
            // update current item to next item in the deque
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        StdOut.println(
                "options : {af : addFirst; al : addLast; rf :removeFirst; rl : removeLast; ite :iterate all items available from front;} ");
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("af")) {
                StdOut.println("Enter a string in the front : ");
                String data = StdIn.readString();
                deque.addFirst(data);
                StdOut.println(deque.size() + " items left on deque");
            }
            else if (item.equals("al")) {
                StdOut.println("Enter a string in the end : ");
                String data = StdIn.readString();
                deque.addLast(data);
                StdOut.println(deque.size() + " items left on deque");
            }
            else if (item.equals("rf")) {
                StdOut.println("Remove first string : " + deque.removeFirst());
                StdOut.println(deque.size() + " items left on deque");
            }
            else if (item.equals("rl")) {
                StdOut.println("Remove last string : " + deque.removeLast());
                StdOut.println(deque.size() + " items left on deque");
            }
            else if (item.equals("ite")) {
                for (String a : deque) {
                    StdOut.print(a + ' ');
                }
                StdOut.print('\n');
            }
            else {
                throw new IllegalArgumentException("Must be one of the five options");
            }
            StdOut.println(
                    "options : {af : addFirst; al : addLast; rf :removeFirst; rl : removeLast; ite :iterate all items available from front;} ");
        }
    }
}
