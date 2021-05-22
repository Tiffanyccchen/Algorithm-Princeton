/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description:  This program implements the Term data structure.
 *                In the test client, the client takes the command line argument two compare two terms using different orders.
 *                - Enter one of three options : {lo : lexicographcal natural order; pf : first 3 prefixs' order; rw : reverse weight;}
 *                - Enter 'term weight' twice for two terms
 *                - Enter ctrl + Z (on Windows) to exit operations
 *  Response : The system will response a number. If < 0 : previous term is smaller. If > 0 : previous term is larger.
 *             If = 0 : two terms are equal according to the ordering condition.
 *
 *  Compilation:  javac-algs4 Term.java
 *  Execution :  java-algs4 Term r
 *  Dependencies : None
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Term implements Comparable<Term> {
    private String query; // a term's content
    private long weight; // a term's weight

    // Initializes a term with the given query string and weight.
    public Term(String query, Long weight) {
        if (query == null) throw new IllegalArgumentException("query must not be null !");
        if (weight < 0) throw new IllegalArgumentException("weight must be a positive number !");
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ByReverseWeightOrder();
    }

    private static class ByReverseWeightOrder implements Comparator<Term> {
        public int compare(Term v, Term w) {
            // reverse the order : larger items are put in the front, so add a negative sign to accomplish that
            return -Long.compare(v.weight, w.weight);
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        return new ByPrefixOrder(r);
    }

    private static class ByPrefixOrder implements Comparator<Term> {
        private int r;

        // initialize ByPrefixOrder class and assign r as number of prefixs to compare
        public ByPrefixOrder(int r) {
            if (r < 0) throw new IllegalArgumentException("number of prefixs must be positive !");
            this.r = r;
        }

        public int compare(Term v, Term w) {
            int length = Math.min(Math.min(v.query.length(), w.query.length()),r); // smallest number we must compare
            for (int i = 0; i < length; i++) {
                // compare each character's position, total time must propotional to number of characters needed to resolve the comparison
                if (v.query.charAt(i) < w.query.charAt(i))
                    return -1;
                else if (v.query.charAt(i) > w.query.charAt(i))
                    return 1;
            }
            return 0;
        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return this.weight + "\t" + this.query;
    }

    public static void main(String[] args) {
        StdOut.println(
                "options : {lo : lexicographcal natural order; pf : first 3 prefixs' order; rw : reverse weight order;}");
        StdOut.println(
                "returns value < 0 : first term is smaller than second term under comparison definition");
        StdOut.println(
                "returns value > 0 : first term is larger than second term under comparison definition");
        StdOut.println(
                "returns value = 0 : first term is equal to second term under comparison definition");
        while (!StdIn.isEmpty()) {
            String option = StdIn.readString();
            StdOut.println(
                    "Please enter first term and its weight in follow format :'term weight'");
            Term term1 = new Term(StdIn.readString(), StdIn.readLong());

            StdOut.println(
                    "Please enter second term and its weight in follow format :'term weight'");
            Term term2 = new Term(StdIn.readString(), StdIn.readLong());

            if (option.equals("lo")) StdOut.println(term1.compareTo(term2));
            else if (option.equals("rw"))
                StdOut.println(byReverseWeightOrder().compare(term1, term2));
            else if (option.equals("pf")) StdOut.println(byPrefixOrder(3).compare(term1, term2));
            else throw new IllegalArgumentException(
                        "You enter neither one of 3 available options! Can't compare for you!");
            StdOut.println(
                    "options : {lo : lexicographcal natural order; pf : first 3 prefixs' order; rw : reverse weight order;}");
        }
    }
}
