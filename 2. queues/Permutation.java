/* *****************************************************************************
 *  Name:    Yu Ting Chen
 *  NetID:   R07H41005
 *  Precept: P00
 *
 *  Description: This program reads a sequence of strings, permutate them, and print out k strings
 *   This program takes the command line argument sequentially.
 *   - Enter k - the number of strings you want to see after permutation
 *   - Enter a string repeatedly, or you can read in a sequence of strings using command line argument ex. java-algs4 Permutation < xxx.txt,
 *     which contains strings separated by spaces ex. I like to eat apple
 *  Response: The system print out k strings permutated separated by lines
 *  Compilation:  javac-algs4 Permutation.java
 *  Execution :  java-algs4 Permutation
 *  Dependencies : RandomizedQueue
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        // number of strings to returrn
        int k = Integer.parseInt(args[0]);
        int i = 1;
        RandomizedQueue<String> randomizedque = new RandomizedQueue<String>();
        // read first k strings
        while (!StdIn.isEmpty() && i <= k) {
            String data = StdIn.readString();
            randomizedque.enqueue(data);
            i++;
        }
        int j = 1;
        // read k+1,...,n strings, replace with one randomly select existing string in the randomized queue with k/(k+j) probability
        // this method can maintain that each string still has k/n probability being sampled
        // reference website about the theory : https://zhuanlan.zhihu.com/p/29178293
        while (!StdIn.isEmpty()) {
            String data = StdIn.readString();
            boolean replace = StdRandom.bernoulli(k / (double) (k + j));
            if (replace) {
                randomizedque.dequeue();
                randomizedque.enqueue(data);
            }
            j++;
        }
        while (!randomizedque.isEmpty()) {
            StdOut.println(randomizedque.dequeue());
        }
    }
}
