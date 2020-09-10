import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Terrain {

	float [][] height; // regular grid of height values
	Water [][] waterData; //Array containing water objects for each pixel.

	int dimx, dimy; // data dimensions
	BufferedImage img; // greyscale image for displaying the terrain top-down
	BufferedImage Wimg; // greyscale image for displaying the water top-down

	ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)
	
	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)
	int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	public BufferedImage getImage() {
		  return img;
	}

	public BufferedImage getWimage() {
		return Wimg;
	}
	
	// convert linear position into 2D location in grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y	
	}
	
	// convert height values to greyscale colour and populate an image
	void deriveImage()
	{
		img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;
		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = height[x][y];
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (height[x][y] - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
				 img.setRGB(x, y, col.getRGB());
			}
	}

	void deriveWimage() {
		Wimg = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				// find normalized height value in range
				try {

				if (waterData[x][y].wSurface>0) { //access array with water data and do comparisons.
					Wimg.setRGB(x, y, Color.BLUE.getRGB() );
					//Add colour to 3x3 for it to be visible. ** check 4 out of bounds.
				}
				}
				catch (ArrayIndexOutOfBoundsException e){
					continue;
					}
				}
			}


	
	// generate a permuted list of linear index positions to allow a random
	// traversal over the terrain
	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}
	
	// find permuted 2D location from a linear index in the
	// range [0, dimx*dimy)
	void getPermute(int i, int [] loc) {
		locate(permute.get(i), loc);
	}
	
	// read in terrain from file
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName));
			
			// read grid dimensions
			// x and y correspond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			
			// populate height grid & waterData grid
			height = new float[dimx][dimy];
			waterData = new Water[dimx][dimy]; //instantiate WaterData array
			
			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++) {
					height[x][y] = sc.nextFloat();
					waterData[x][y] = new Water(0, 0); //create new water object at this position.
					}
				}
			//waterData[50][50].wSurface=0.05f; //i can see this on the diagram overlaid.s
				
			sc.close(); 
			
			// create randomly permuted list of indices for traversal 
			genPermute(); 
			
			// generate greyscale height field image
			deriveImage();
			deriveWimage(); //Place somewhere else where it is called each time step post synchronizatuion of water array method traversal.
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}