import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


/**
 * <h1><b>ParallelBasinClassification</b></h1>
 * A parallelized solution to the Sequential program of basin
 * classification. The ParallelBasinClassification program
 * extends the RecursiveAction class from the fork and join
 * framework and implements a divide and conquer multithreaded
 * approach to efficiently classify basins and similarly outputs
 * to a file.
 * <br><hr>
 *
 * @author  Chris Kateera
 * @version 1.0
 * @since   27-08-2020
 */

public class ParallelBasinClassification extends RecursiveAction {

    int lo;
    int hi;
    Float[][] terrain2D;
    static final int SEQUENTIAL_CUTOFF = 2;
    static long t0,t1; //Timing constants

    /**
     *ParallelBasinClassification Constructor
     * @param terrain2D a 2D array with terrain altitudes.
     * @param lo lower index of array to be processed.
     * @param hi higher index of array to be processed.
     */
    ParallelBasinClassification(Float[][] terrain2D, int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
        this.terrain2D = terrain2D;
    }

    /**
     *Compute method which processes multithreaded computations according to sequential cutoff.
     */
    @Override
    protected void compute() {
        //base sequential case: if below threshold check_neighbours sequentially
        if (hi - lo < SEQUENTIAL_CUTOFF) {
            for(int i=lo; i < hi; i++) { //process rows
                for (int j = 0; j < terrain2D[i].length; j++) {
                    boolean coordinate = BasinClassification.check_neighbours(terrain2D, i, j);
                    if (coordinate) {
                       BasinClassification.parallelBasins[i][j] = true; //Append found basins to parallelBasins
                    }
                }
            }
        } else { //divide and conquer
            ParallelBasinClassification left = new ParallelBasinClassification(terrain2D, lo, (hi + lo) / 2);
            ParallelBasinClassification right = new ParallelBasinClassification(terrain2D, (hi + lo) / 2, hi);
            left.fork(); // queue the left task
            right.compute(); // compute the right task
            left.join(); // wait for the first task result //invokeAll does this3-step process.
        }
    }

    /**
     *The main program entry pont which calls other methods.
     * @param args arguments supplied by user.
     */
    public static void main(String[] args) {
        Float[][] ogTerrain2D = BasinClassification.formulateArray(args);
        ForkJoinPool pool = new ForkJoinPool();
        for (int i = 0; i < 1; i++) {
            ParallelBasinClassification task = new ParallelBasinClassification(ogTerrain2D, 0, (ogTerrain2D.length - 1));
            System.gc();
            t0 = System.nanoTime();
            pool.invoke(task);
            t1 = System.nanoTime();
            System.out.println((t1 - t0)/1000000.0); //get time in ms
        }

            //ForkJoinPool.commonPool().invoke(task);
            //Write to output file
        try {
            FileWriter myWriter = new FileWriter(args[1]);
            myWriter.write(BasinClassification.noBasins+"\n");

            for (int i = 0; i < BasinClassification.sequentialBasins.length; i++) {
                for (int j = 0; j < BasinClassification.parallelBasins[i].length; j++) {
                    if(BasinClassification.parallelBasins[i][j]) {
                        myWriter.write(i+" "+j+"\n");
                    }
                }
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}