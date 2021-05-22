/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description:  This program implements the autocomplete task, returning a lists of k strings matching the query prefix sorted by descending weight.
 *                In the test client, the client specifies the document to read/ number of strings to return/ the query.
 *                - argument 1 : document name + file type ex. cities.txt
 *                - argument 2 : integer value k specifies number of results to return
 *                - Enter the query you want to find
 *                - Enter ctrl + Z (on Windows) to exit operations
 *  Response :    The system will return number of matched and top k terms matching the query in descending weight
 *                the format will be like ex. 100 Taiwan
 *  Compilation:  javac-algs4 Autocomplete.java
 *  Execution :  java-algs4 Autocomplete (filename.txt)document.txt (int)k (must encode in utf-8)
 *  Dependencies : Term.class, BinarySearchDeluxe.class
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Autocomplete {
    private Term[] terms; // array of Terms
    private int firstidx; // first index of query-matched Terms
    private int lastidx; // last index of query-matched Terms

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) throw new IllegalArgumentException("Terms must not be null !");
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) throw new IllegalArgumentException("A term must not be null !");
            else this.terms[i] = terms[i];
        }
        // sort array by natural order
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("prefix must not be null !");
        // find first index of matched terms using the prefix ordering comparator implemented in Term class
        firstidx = BinarySearchDeluxe
                .firstIndexOf(terms, new Term(prefix, (long) 0),
                              Term.byPrefixOrder(prefix.length()));
        lastidx = BinarySearchDeluxe
                .lastIndexOf(terms, new Term(prefix, (long) 0),
                             Term.byPrefixOrder(prefix.length()));
        Term[] findterms; // array of query-matched Terms
        if (firstidx == -1) {
            findterms = new Term[0];
            return findterms;
        }
        else {
            int matchlen = lastidx - firstidx + 1;
            findterms = new Term[matchlen];
            for (int i = 0; i < matchlen; i++) {
                // copy all matched terms to a new array
                findterms[i] = terms[firstidx++];
            }
            // sort matched terms by reversing weight
            Arrays.sort(findterms, Term.byReverseWeightOrder());
            return findterms;
        }
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("prefix must not be null !");
        firstidx = BinarySearchDeluxe
                .firstIndexOf(terms, new Term(prefix, (long) 0),
                              Term.byPrefixOrder(prefix.length()));
        lastidx = BinarySearchDeluxe
                .lastIndexOf(terms, new Term(prefix, (long) 0),
                             Term.byPrefixOrder(prefix.length()));
        if (firstidx == -1) return 0;
        else return (lastidx - firstidx + 1);
    }

    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }
}
