import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelBasinClassification extends RecursiveAction {

    int lo;
    int hi;
    Float[][] terrain2D;
    static final int SEQUENTIAL_CUTOFF = 128;
    static long t0,t1; //Timing constants

    ParallelBasinClassification(Float[][] terrain2D, int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
        this.terrain2D = terrain2D;
    }


    @Override
    protected void compute() {
        //base sequential case: if below threshold check_neighbours sequentially
        if (hi - lo < SEQUENTIAL_CUTOFF) {
            for(int i=lo; i < hi; i++) { //process rows
                for (int j = 0; j < terrain2D[i].length; j++) {
                    int[] coordinate = BasinClassification.check_neighbours(terrain2D, i, j);
                    if (coordinate != null) {
                       BasinClassification.parallelBasins.add(coordinate); //Append found basins to parallelBasins
                    }
                }
            }
        } else {
            ParallelBasinClassification left = new ParallelBasinClassification(terrain2D, lo, (hi + lo) / 2);
            ParallelBasinClassification right = new ParallelBasinClassification(terrain2D, (hi + lo) / 2, hi);
            left.fork(); // queue the left task
            right.compute(); // compute the right task
            left.join(); // wait for the first task result //invokeAll does this3-step process.
        }
        //System.out.println(sizes+=basins.size());
    }

    public static void main(String[] args) { //Entry point?
        Float[][] ogTerrain2D = BasinClassification.formulateArray(args);

        ForkJoinPool pool = new ForkJoinPool();
        ParallelBasinClassification task = new ParallelBasinClassification(ogTerrain2D, 0, (ogTerrain2D.length-1));
        System.gc();
        t0=System.currentTimeMillis();
        pool.invoke(task);
        t1=System.currentTimeMillis();
        //ForkJoinPool.commonPool().invoke(task);
        System.out.println(BasinClassification.parallelBasins.size());
        for(int[] basin: BasinClassification.parallelBasins ){
            System.out.print(basin[0]);
            System.out.println(basin[1]);
        }
        System.out.println(t1-t0);
    }

}