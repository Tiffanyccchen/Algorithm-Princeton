/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Partner Name:    No
 *  Partner NetID:   No
 *  Partner Precept: P00
 *
 *  Description: This program implements the WordNet data structure.
 *               In the test client, the client needs to read in two files - one formatted hypernym file & one formatted synset file.
 *  Response :  The system will print
 *              1. first 10 wordnet nouns
 *              2. whether the single noun apple and arearfcarxaxg (random typing) are in the wordnet.
 *              3. the shortest common ancestor of individual and edible_fruit.
 *              (Ans. according to the assignment's checklist is physical_entity.)
 *              4. the shortest ancestral path (SCP) 's length of white_marlin and mileage.
 *              (Ans. according to the assignment's checklist is 23.)
 *
 *  Compilation:  javac-algs4 WordNet.java
 *  Execution :  java-algs4 WordNet synsets.txt hypernyms.txt
 *  Dependencies : ShortestCommonAncestor, Digraph, Bag, LinearProbingHashST
 **************************************************************************** */


import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private LinearProbingHashST<String, Bag<Integer>> wordtoidsynset// store word to id symbol table
            = new LinearProbingHashST<String, Bag<Integer>>();
    private LinearProbingHashST<Integer, String> idtowordsynset // store id to word symbol table
            = new LinearProbingHashST<Integer, String>();
    private ShortestCommonAncestor scafinder; // store sca object;
    private Digraph hypernym; // store directed graph of hypernyms

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException();
        In insynsets = new In(synsets);
        while (insynsets.hasNextLine()) {
            String[] line = insynsets.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String synset = line[1];
            String[] nouns = synset.split(" ");
            for (int i = 0; i < nouns.length; i++) {
                String noun = nouns[i];
                if (wordtoidsynset.get(noun) != null) {
                    Bag<Integer> idset = wordtoidsynset.get(noun);
                    // add an id to id set corresponding to the noun
                    idset.add(id);
                }
                else {
                    // create id set corresponding to the noun
                    Bag<Integer> idset = new Bag<Integer>();
                    idset.add(id);
                    // put (noun, idset) in ST
                    wordtoidsynset.put(noun, idset);
                }
            }
            idtowordsynset.put(id, synset);
        }

        int numsynsets = idtowordsynset.size(); // store number of synsets
        In inhypernyms = new In(hypernyms);

        // hypernym DG initialization
        hypernym = new Digraph(numsynsets);
        while (inhypernyms.hasNextLine()) {
            String[] line = inhypernyms.readLine().split(",");
            int hyponymid = Integer.parseInt(line[0]);
            if (line.length >= 2) {
                String[] hypernymids = line[1].split(",");
                for (int i = 0; i < hypernymids.length; i++) {
                    int hypernymid = Integer.parseInt(hypernymids[i]);
                    hypernym.addEdge(hyponymid, hypernymid);
                }
            }
        }
        // initialize sca object
        scafinder = new ShortestCommonAncestor(hypernym);
    }

    // all WordNet nouns
    public Iterable<String> nouns() {
        return wordtoidsynset.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordtoidsynset.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        Bag<Integer> subsetA = wordtoidsynset.get(noun1);
        Bag<Integer> subsetB = wordtoidsynset.get(noun2);
        if (subsetA.size() == 1 && subsetB.size() == 1) {
            int ancestor = scafinder.ancestor(subsetA.iterator().next(), subsetB.iterator().next());
            return idtowordsynset.get(ancestor);
        }
        else {
            int ancestor = scafinder.ancestorSubset(subsetA, subsetB);
            return idtowordsynset.get(ancestor);
        }
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        Bag<Integer> subsetA = wordtoidsynset.get(noun1);
        Bag<Integer> subsetB = wordtoidsynset.get(noun2);
        if (subsetA.size() == 1 && subsetB.size() == 1) {
            return scafinder.length(subsetA.iterator().next(), subsetB.iterator().next());
        }
        else {
            return scafinder.lengthSubset(subsetA, subsetB);
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        int count = 0;
        for (String noun : wordnet.nouns()) {
            StdOut.println(noun);
            count += 1;
            if (count >= 10) break;
        }
        StdOut.println(wordnet.wordtoidsynset.size());
        StdOut.println(wordnet.idtowordsynset.size());
        StdOut.println(wordnet.hypernym.V());
        StdOut.printf("the fact that apple is in the wordnet is %5s\n", wordnet.isNoun("apple"));
        StdOut.printf("the fact that arearfcarxaxg is in the wordnet is %5s\n",
                      wordnet.isNoun("arearfcarxaxg")); // not a word just random typing
        StdOut.printf(
                "The shortest common ancestor between nouns individual & edible_fruit is %20s\n",
                wordnet.sca("individual", "edible_fruit"));
        StdOut.printf(
                "The shortest ancestral path between nouns white_marlin & mileage is %20s\n",
                wordnet.distance("white_marlin", "mileage"));
    }
}
