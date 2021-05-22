/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the Outcast program.
 *               In the test client, the client can read in a formatted outcast file consisted of words separated bt spaces.
 *  Response :   The system will print the one outcast noun in the file.
 *
 *  Compilation:  javac-algs4 Outcast.java
 *  Execution :  java-algs4 synsets.txt hypernyms.txt outcast[xxx].txt outcast[xxx].txt (depends on how many outcast doc you want to read in)
 *  Dependencies : WordNet, ShortestCommonAncestor
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet; // store wordnet

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxdistancesum = Integer.MIN_VALUE;
        String maxnoun = null;
        for (int i = 0; i < nouns.length; i++) {
            int distancesum = 0;
            for (int j = 0; j < nouns.length; j++) {
                // do not need to compare two same nouns since dist. is always 0
                if (i == j) continue;
                String firstnoun = nouns[i];
                String secondnoun = nouns[j];
                if (!wordnet.isNoun(firstnoun) || !wordnet.isNoun(secondnoun))
                    throw new IllegalArgumentException("noun should be a valid wordnet noun!");
                int distance = wordnet.distance(firstnoun, secondnoun);
                distancesum += distance;
            }
            // update maximum distance if the sum of the noun's distance to all nouns is larger
            if (distancesum > maxdistancesum) {
                maxdistancesum = distancesum;
                maxnoun = nouns[i];
            }
        }
        return maxnoun;
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
