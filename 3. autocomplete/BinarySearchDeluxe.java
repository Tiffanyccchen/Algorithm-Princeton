/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description:  This program is the library of static methods for finding first/last index of specific item using Binary Search.
 *                In the test client, the client takes the command line argument sequentially.
 *                - Enter array of int of size n, and enter elements in array. ex. 1 2 3 4 5
 *                - Enter one of three options : {fi: first index, li : last index}
 *                - Enter the integer you want to find
 *                - Enter ctrl + Z (on Windows) to exit operations
 *  Response : The system will print out the position index of the first/last element you want to find.
 *
 *  Compilation:  javac-algs4 BinarySearcgDeluxe.java
 *  Execution :  java-algs4 BinarySearcgDeluxe
 *  Dependencies : None
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BinarySearchDeluxe {
    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("All arguments should not be null !");
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int comp = comparator.compare(key, a[mid]);
            if (comp < 0) hi = mid - 1;
            else if (comp > 0) lo = mid + 1;
            // if the matched index(mid) 's previous element is the same, then decreases upper interval by one.
                // to prevent out of bound, set the prerequisite to be that mid must not be the first index.
            else if (mid > 0 && comparator.compare(a[mid], a[mid - 1]) == 0) hi = mid - 1;
            else return mid;
        }
        return -1;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("All arguments should not be null !");
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int comp = comparator.compare(key, a[mid]);
            if (comp < 0) hi = mid - 1;
            else if (comp > 0) lo = mid + 1;
            // if the matched index(mid) 's nexr element is the same, then increases upper interval by one.
                // to prevent out of bound, set the prerequisite to be that mid must not be the last index.
            else if (mid < (a.length - 1) && comparator.compare(a[mid], a[mid + 1]) == 0)
                lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        int n;
        StdOut.println( "Enter the array size : ");
        n = StdIn.readInt();
        Integer[] arrayofint = new Integer[n];
        StdOut.println(
                "Enter n array elements separated by spaces (must be integers!) : (eg. 1 2 3)");
        for (int i = 0; i < n; i++) {
            arrayofint[i] = StdIn.readInt();
        }
        Arrays.sort(arrayofint);
        StdOut.println("options : {fi: first index, li : last index}");
        while (!StdIn.isEmpty()) {
            String option = StdIn.readString();
            StdOut.println(
                    "enter the element you want to find : ");
            int element = StdIn.readInt();
            if (option.equals("fi"))
                StdOut.println(firstIndexOf(arrayofint, element, Comparator.naturalOrder()));
            else if (option.equals("li"))
                StdOut.println(lastIndexOf(arrayofint, element, Comparator.naturalOrder()));
            else throw new IllegalArgumentException(
                        "You enter neither one of 2 available options! Can't give the result for you!");
            StdOut.println(
                    "options : {fi: first index, li : last index}");
        }
    }
}
