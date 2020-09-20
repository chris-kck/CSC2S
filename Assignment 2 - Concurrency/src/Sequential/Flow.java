import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	public static int iterations;
	public static JPanel b;
	static Stack<int[]> stackR = new Stack<>();
	static Stack<int[]> stackA = new Stack<>();
	public static volatile boolean playing=false;

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}

	//increments Flow.iterations each time its called

	/**
	 *Update time step when a simulation iteration occurs.
	 */
	public static void updateTimeSteps(){
		Component[] a = Flow.b.getComponents();
		JLabel k = (JLabel)a[4];
		k.setText("\t \t \t Time Steps: " + iterations++);
	}

	/**
	 * Method to compare surrounding points and return lowest point.
	 * @param landdata Terrain object
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 * @return an integer array with x,y of lowest surrounding point.
	 */
	public static int[] getLowest(Terrain landdata, int x ,int y){
		float currentSurface = landdata.waterData[x][y].wSurface;
		float lowest=currentSurface;
		int[] lowestPoint= new int[2];
		for (int s = -1; s <= 1; s++)
			for (int t = -1; t <= 1; t++) {
				try {
					int neighbourx = x + s;
					int neighboury = y + t;
					float neighbourSurface = landdata.waterData[neighbourx][neighboury].wSurface;
						if (neighbourSurface < currentSurface && neighbourSurface < lowest) { //lower
							//is it lower than what we came across?
							lowest = neighbourSurface; //store coordinates after comparing depths.
							lowestPoint[0] = neighbourx;
							lowestPoint[1] = neighboury;
						}
				}
				catch (ArrayIndexOutOfBoundsException error){
					continue;
				}
			}
		return lowestPoint;
	}

	/**
	 * Function to simulate and transfer water
	 * @param landdata Terrain data object
	 */
	public static void simulate(Terrain landdata){


		for (int x = 0; x < landdata.dimx; x++) {
			//set edges to zero in seperate loop (x=0, y=0, x=dimx-1, and y=dimy-1)
			landdata.waterData[0][x].removeDepth();//top edge
			landdata.waterData[x][0].removeDepth();//left edge
			landdata.waterData[landdata.dimy-1][x].removeDepth();//bottom edge
			landdata.waterData[x][landdata.dimx-1].removeDepth();//right row

			for (int y = 0; y < landdata.dimy; y++) {
				if (landdata.waterData[x][y].wDepth > 0) {
					int[] lowest = getLowest(landdata, x, y); //returns the lowest surrounding point[] given an index
					if (lowest[0]==x && lowest[1]==y)continue; //Lowest same as passed point then no water transfer. no decrease depth
					else{
						stackR.add(new int[] {x,y}); //Store points to have water removed from them.
						stackA.add(lowest); //Store points to have water added to them.

					}
				}
			}
		}
		//Remove water from origin and transfer water to lowest neighbour
		while (stackA.size()>0){
			int[] k =stackA.pop();
			landdata.waterData[k[0]][k[1]].changeDepth(+0.01f);
		}
		while (stackR.size()>0){
			int[] k =stackR.pop();
			landdata.waterData[k[0]][k[1]].changeDepth(-0.01f);
		}

		landdata.deriveWimage();
		//get graphic and draw image then repaint
		fp.getGraphics().drawImage(landdata.getWimage(), 0, 0, null);
		fp.repaint();
		updateTimeSteps();
	}

	/**
	 * Setup Graphic user interface and action listeners.
	 * @param frameX X dimension of frame dependent on grid size
	 * @param frameY Y dimension of frame dependent on grid size
	 * @param landdata Terrain data
	 */
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
				playing=false;
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
				playing=false;
			}
		});
		playB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//continue waterflow simulation after pause.
				playing=true;
				//simulate(landdata);

				}

		});
		frame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int Xcood = e.getX()-8;
				int Ycood = e.getY()-31;
				System.out.println("Testing click at X:"+Xcood +" Y:"+Ycood );

					for (int s = -3; s <= 3; s++)
						for (int t = -3; t <= 3; t++) {
							try {
								landdata.waterData[Xcood + s][Ycood + t].changeDepth(+0.03f); // Adding water to point after click 3u. 0.01u transferred.
								//landdata.waterData[Xcood + s][Ycood + t].wSurface += 0.03f;
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

	/**
	 * Main thread of execution
	 * @param args args[0] contains name of terrain file passed on the command line.
	 */
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
