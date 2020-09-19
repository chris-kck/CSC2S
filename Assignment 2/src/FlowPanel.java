import java.awt.Graphics;
import java.awt.Component;
import javax.swing.*;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	
	FlowPanel(Terrain terrain) {
		land=terrain;
	} //passed as landdata object with 2D array of heights.
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);

			//Either draw a new overlay image here, or add new FP2
			//create a new getWaterImage method that paints water.
		}
		//Experiment doing seperately, then try join the deriveimage method to paint terrain nd water. water if theres water>0 else terrain.
		if (land.getWimage() != null){
			g.drawImage(land.getWimage(), 0, 0, null);

			//Either draw a new overlay image here, or add new FP2
			//create a new getWaterImage method that paints water.
		}


	}
	
	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting



		/*
		I’d suggest creating and starting the threads separately, when your program is launched, and simply using the Boolean variable to control play and pause
		loop inside
		use a volatile boolean as a flag
		the changing; of the status is done by your buttons*/
		int i = 0;
		while (i++<2000000) {
			Flow.updateTimeSteps(); //increments Flow.iterations each time its called
			repaint();
		/*
		•	Water is cleared from the boundary (x=0, y=0, x=dimx-1, and y=dimy-1) by setting values there to zero.
		•	For each grid position wSurface = wDepth + tHeight
		•   no water is transferred out of the current grid position if higher or same level


		•	All grid positions not on the boundary are traversed in a permuted order (see the getPermute() method.
		//edges-2 edges-2.
		•	subtract from old, add to new.
		•	The current water surface at (x,y) is compared to the water surface of the neighbouring grid positions.
		•   A single unit of water is transferred to the lowest of these neighbours (strictly lower)

		You must use appropriate synchronization and your solution should allow for maximal concurrency: operations should not be serialized unless necessary.
		Your simulation should be carried out by 4 threads, each responsible for a portion of the permuted list of grid positions. These should synchronise on each timestep. That is, no thread should be allowed to start the next timestep of simulation before all others are complete.
		Fluid conservation
		 */



		}
	}
}