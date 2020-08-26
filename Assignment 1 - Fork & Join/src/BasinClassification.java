import java.io.*;
import java.util.LinkedList;

public class BasinClassification {
    static int j =0;
    static Float[][] terrain2D;
    static LinkedList<int[]> parallelBasins = new LinkedList<>();
    static long t0,t1; //System.currentTimeMillis(); //Timing constants

    public static Float[][] formulateArray (String[] args) {
        String line;
        int lineNo = 0,numrows=0,numcolumns=0;



        try {
            System.out.println(args[0]);
            FileReader terrain = new FileReader("./src/"+args[0]);
            BufferedReader br =new BufferedReader(terrain);

            while ((line = br.readLine()) != null) {
                if (0 == lineNo) {
                    //get grid row x col size
                    numrows = Integer.parseInt(line.split(" ")[0]);
                    numcolumns = Integer.parseInt(line.split(" ")[1]);
                    
                }
                terrain2D = new Float[numrows][numcolumns];
                if (1 == lineNo) {
                    //rows and columns data, generate 2D array O(n^2) :(
                    String[] temp = line.split(" ");
                    int k=0;
                    for (int i = 0; i < numrows; i++) {
                        for (int j = 0; j < numcolumns; j++) {
                            terrain2D[i][j]= Float.parseFloat(temp[k++]);

                        }
                    }

                }
                lineNo++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return terrain2D;
    }


    public static void main(String[] args){
        formulateArray(args); //creates array from data
        t0 = System.currentTimeMillis();
        check_basins(terrain2D); //checks and prints/generates required output
    }

    public static void check_basins(Float[][] terrain2D){

        LinkedList<int[]> basins = new LinkedList<>();
        //skip checking edge top, bottom, left, right coordinates.
        for (int i = 0; i < terrain2D.length; i++) {
            for (int j = 0; j < terrain2D.length; j++) {

                int[] coordinate = check_neighbours(terrain2D, i, j);
                if (coordinate != null) {
                    basins.add(coordinate);
                }
            }
        }
        t1=System.currentTimeMillis();

        System.out.println(basins.size()); //Number of found basins
        for(int[] basin: basins ){
            System.out.print(basin[0]);
            System.out.println(basin[1]);
        }
        System.out.println( t1-t0 );
    }

    public static int[] check_neighbours(Float[][] terrain2D, int i ,int j){
        Float offset = terrain2D[i][j]+ 0.01f;

        try {
            if (terrain2D[i][j + 1] >= offset && terrain2D[i][j - 1] >= offset && terrain2D[i + 1][j] >= offset && terrain2D[i - 1][j] >= offset &&
            terrain2D[i-1][j + 1] >= offset && terrain2D[i-1][j - 1] >= offset && terrain2D[i + 1][j+1] >= offset && terrain2D[i+1][j-1] >= offset) {
             //everything surrounding ij is above offset return.
                return new int[]{i, j};
            }
            else return null;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return null; // i.e edge
            //e.printStackTrace();
        }


    }
}
