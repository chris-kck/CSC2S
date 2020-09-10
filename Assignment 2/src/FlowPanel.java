import java.awt.Graphics;
import javax.swing.JPanel;

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

		/*
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLUE);
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		g2d.fillOval(1,30,30,30);
		g2d.fill(new Rectangle(0, 0, getWidth()-30, getHeight()));
		g2d.dispose();
		*/
	}
	
	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	    repaint();
	}
}