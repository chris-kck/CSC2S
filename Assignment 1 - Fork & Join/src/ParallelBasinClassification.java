import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelBasinClassification extends RecursiveAction {

    int lo;
    int hi;
    Float[][] terrain2D;
    static final int SEQUENTIAL_CUTOFF = 512;
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
                    boolean coordinate = BasinClassification.check_neighbours(terrain2D, i, j);
                    if (coordinate) {
                       BasinClassification.parallelBasins[i][j] = true; //Append found basins to parallelBasins
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
        System.out.println(BasinClassification.noBasins);
        System.out.println(t1-t0);
        //Write to output file
        try {
            FileWriter myWriter = new FileWriter(args[1]);//use args[1]

            System.out.println(BasinClassification.noBasins);
            myWriter.write(BasinClassification.noBasins+"\n");

            for (int i = 0; i < BasinClassification.sequentialBasins.length; i++) {
                for (int j = 0; j < BasinClassification.parallelBasins[i].length; j++) {
                    if(BasinClassification.parallelBasins[i][j]) {
                        System.out.println(i+" "+j);
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