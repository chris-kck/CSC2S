import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	AtomicInteger count
			= new AtomicInteger();

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
		while(true){
			if(Flow.playing) {
				//Flow.simulate(land, 0, land.dimx);//repaint(); //done in simulate.
				ParallelWaterflow.parallelmain();
			}
		}
	}
}