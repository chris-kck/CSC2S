import java.awt.Graphics;
import java.util.concurrent.ForkJoinPool;
import javax.swing.*;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	public ForkJoinPool pool;
	public ParallelWaterflow task;
	
	FlowPanel(Terrain terrain) {
		land=terrain;
	} //passed as landdata object with 2D array of heights.
		
	// responsible for painting the terrain and water as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}
		//Draw waterImage to graphic.
		if (land.getWimage() != null){
			g.drawImage(land.getWimage(), 0, 0, null);
		}


	}
	
	public void run() {
		// GUI buttonsallow stopping and starting

		pool = new ForkJoinPool();
		task = new ParallelWaterflow(Flow.fp.land , 0, (Flow.fp.land.dimx - 1));
		while(true){
		if(Flow.playing) {
			//Flow.simulate(land, 0, land.dimx);//repaint(); //done in simulate.
			ParallelWaterflow.parallelmain();

			/*
			for (int i=0; i<10;i++) {
				int[] temp = Flow.fp.land.getPermute(i);
				System.out.println(temp[0]+"and"+temp[1]);
			}
			*/
			//TODO handle pause action
		}

		}
		/*
		•	All grid positions not on the boundary (//x-edges-2 y-edges-2.) are traversed in a permuted order (see the getPermute() method.

		You must use appropriate synchronization and your solution should allow for maximal concurrency:
		operations should not be serialized unless necessary. Simulate with 4 threads,
		each responsible for a portion of the permuted list of grid positions. These should synchronise on each timestep. That is, no thread should be allowed to start the next timestep of simulation before all others are complete.
		Fluid conservation
		 */



		//}
	}
}