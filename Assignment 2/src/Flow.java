import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	public static int iterations;
	public static JPanel b;
	static int [][] surroundingxy = {{-1, -1}, {0, -1}, {-1, 1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}}; //top left down

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}


	public static void updateTimeSteps(){
		JLabel k = (JLabel)Flow.b.getComponents()[4];
		k.setText("\t \t \t Time Steps: " + iterations++);
	}


	public static void setupGUI(int frameX,int frameY,Terrain landdata) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
	   	
		b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton resetB = new JButton("Reset");
		JButton pauseB = new JButton("Pause");
		JButton playB = new JButton("Play");
		JButton endB = new JButton("End");
		JLabel steps = new JLabel("Steps: 0");
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
				System.exit(0);
			}
		});
		resetB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO reset counter on gui.
				//TODO If edge position, set water to zero when looping.
				//TODO comparisons: check for lower and store as lowest, compare lower to lowers and set. ####lowest = lower.wSurface<lowest.wSurface ? lower :lowest;
				//clear all water
				for (Water[] wd: landdata.waterData)
					for (Water WS: wd) {
					WS.wDepth=0;
					}

				//generate updated WaterImage
				landdata.deriveWimage();
				//get graphic and draw image then repaint
				fp.getGraphics().drawImage(landdata.getWimage(), 0, 0, null);
				//repaint
				fp.repaint();

				iterations=0;
				updateTimeSteps();

			}
		});
		pauseB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//snapshot of window. pause regeneration of images & tranfer of water.
			}
		});
		playB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//continue waterflow simulation after pause.

				//surroundingxy
				//landdata.waterData

				//set edges to zero in seperate loop or have a if statemen when transfering water to set edge to 0.
				//(x=0, y=0, x=dimx-1, and y=dimy-1)
				for (Water[] wd: landdata.waterData)
					for (Water WS: wd) {
						WS.wDepth=0;
					}


			}
		});
		frame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("Testing click at X:"+e.getX() +" Y:"+e.getY() );
				int Xcood = e.getX();
				int Ycood = e.getY()-30;

					for (int s = -3; s <= 3; s++)
						for (int t = -3; t <= 3; t++) {
							try {
								landdata.waterData[Xcood + s][Ycood + t].wDepth += 0.03f; //Adding water to point after click 3u. 0.01u transferred.
							}
							catch (ArrayIndexOutOfBoundsException error){
								continue;
							}
						}

				//generate updated WaterImage
				landdata.deriveWimage();

				//get graphic and draw image then repaint
				fp.getGraphics().drawImage(landdata.getWimage(), 0, 0, null);
				//repaint
				fp.repaint();

				//System.exit(0);

			}
		});

		
		b.add(endB);
		b.add(resetB);
		b.add(pauseB);
		b.add(playB);
		b.add(steps);

		//add panel to frame
		g.add(b);
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		// 
		landdata.readData(args[0]);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
		
		// to do: initialise and start simulation
	}
}
