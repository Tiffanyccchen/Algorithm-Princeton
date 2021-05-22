import edu.princeton.cs.algs4.StdOut;

public class DoublingRatio {

    // This class should not be instantiated.
    private DoublingRatio() {
    }

    /**
     * Returns the amount of time to call {@code ThreeSum.count()} with <em>n</em>
     * random 6-digit integers.
     * @param n the number of integers
     * @return amount of time (in seconds) to call {@code ThreeSum.count()}
     *   with <em>n</em> random 6-digit integers
     */

    /**
     * Prints table of running times to call {@code ThreeSum.count()}
     * for arrays of size 250, 500, 1000, 2000, and so forth, along
     * with ratios of running times between successive array sizes.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int trials = 10;
        double prev = 1;
        for (int n = 100; n <= 1600; n += n) {
            PercolationStats stat = new PercolationStats(n, trials);
            double time = stat.elapsedtime;
            StdOut.printf("%7d %7.3f %5.3f %5.3f\n", trials, time, time / prev, Math.log(time / prev) / Math.log(2));
            prev = time;
        }
    }
}
