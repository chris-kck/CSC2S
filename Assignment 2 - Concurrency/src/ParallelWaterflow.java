import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


/**
 * <h1><b>ParallelWaterflow</b></h1>
 * A parallelized solution to the Sequential program of waterflow
 * The ParallelWaterflow program extends the RecursiveAction class
 * from the fork and join framework and implements a divide and
 * conquer multithreaded approach to efficiently traverse through
 * a terrain array with water and transfer water to the lowest
 * surrounding points.
 * <br><hr>
 *
 * @author  Chris Kateera
 * @version 1.0
 * @since   27-08-2020
 */

public class ParallelWaterflow extends RecursiveAction {

    int lo,hi;
    Terrain land;
    static final int SEQUENTIAL_CUTOFF = 64;
    static long t0,t1; //Timing constants

    /**
     *ParallelWaterflow Constructor
     * @param land a 2D array with terrain data.
     * @param lo lower index of array to be processed.
     * @param hi higher index of array to be processed.
     */
    ParallelWaterflow( Terrain land, int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
        this.land = land;
    }

    /**
     *Compute method which processes multithreaded computations according to sequential cutoff.
     */
    @Override
    protected void compute() {
        //base sequential case: if below threshold check_neighbours sequentially
        if (hi - lo < SEQUENTIAL_CUTOFF) {
            //process data between hi and low.
            synchronized (this) {
                Flow.simulate(land, lo, hi);
            }

        } else { //divide and conquer
            ParallelWaterflow left = new ParallelWaterflow(land, lo, (hi + lo) / 2);
            ParallelWaterflow right = new ParallelWaterflow(land, (hi + lo) / 2, hi);
            left.fork(); // queue the left task
            right.compute(); // compute the right task
            left.join(); // wait for the first task result //invokeAll does this3-step process.
        }
    }

    /**
     *The parallelmain program entry pont which calls other methods.
     */
    public static void parallelmain() {

        ForkJoinPool pool = new ForkJoinPool();
        ParallelWaterflow task = new ParallelWaterflow(Flow.fp.land , 0, (Flow.fp.land.dimx - 1));

        System.gc();
        t0 = System.nanoTime(); //time each iteration execution
        pool.invoke(task);
        //Move this elsewhere so it doesn't happen after each thread call.

        while (!Flow.stackA.empty() && !Flow.stackR.empty()){
            int[] k = Flow.stackA.pop();
            Flow.fp.land.waterData[k[0]][k[1]].changeDepth(+0.01f);
            int[] j = Flow.stackR.pop();
            Flow.fp.land.waterData[j[0]][j[1]].changeDepth(-0.01f);
        }


        Flow.fp.land.deriveWimage();
        //get graphic and draw image then repaint
        Flow.fp.getGraphics().drawImage(Flow.fp.land.getWimage(), 0, 0, null);
        Flow.fp.repaint();
        Flow.updateTimeSteps();
        t1 = System.nanoTime();
        //System.out.println((t1 - t0)/1000000.0 + "ms"); //get time in ms
        //Flow.fp.land.permute;
    }

}