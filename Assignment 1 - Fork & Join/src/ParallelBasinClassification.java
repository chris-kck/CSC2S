import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelBasinClassification extends RecursiveAction {

    int lo;
    int hi;
    Float[][] terrain2D; // arguments
    static final int SEQUENTIAL_CUTOFF = 5;


    ParallelBasinClassification(Float[][] terrain2D, int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
        this.terrain2D = terrain2D;
    }


    @Override
    protected void compute() { // modify linked list answers

        if (hi - lo < SEQUENTIAL_CUTOFF) {
            //check for basins and append to arrlist
            //int ans = 0; //create a small arrlist
            //for(int i=lo; i < hi; i++) {
            int ik = 2;
            //call clasify/checkbasins//check if arrlist is being appended
            //}
        } else {
            ParallelBasinClassification left = new ParallelBasinClassification(terrain2D, lo, (hi + lo) / 2);
            ParallelBasinClassification right = new ParallelBasinClassification(terrain2D, (hi + lo) / 2, hi);
            left.fork();
            right.compute();
            left.join();
        }
    }


    static final ForkJoinPool fjPool = new ForkJoinPool();

    //int classify(Float[][] terrain2D) {
//        return ForkJoinPool.commonPool().invoke(new ParallelBasinClassification(terrain2D, 0, terrain2D.length));
//    }
}

    //implementation
    //threshold
    //if below threshold check_basins sequentially - imported
    //either merge 2 arraylists / parallilism acceses same list and appends dynamically.