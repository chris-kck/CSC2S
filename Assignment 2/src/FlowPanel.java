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
		while (i++<20000) {
			Flow.updateTimeSteps(); //increments Flow.iterations each time its called
			repaint();

		}
	}
}