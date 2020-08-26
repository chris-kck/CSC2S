import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class BasinClassification {
    static int noBasins =0;
    static Float[][] terrain2D;
    static Boolean[][] sequentialBasins, parallelBasins;
    static long t0,t1; //Timing constants

    public static Float[][] formulateArray (String[] args) {
        int numrows,numcolumns;
        try {
            System.out.println(args[0]);//print filename

            Scanner scanner = new Scanner(new File("./src/"+args[0]));
            numrows = scanner.nextInt();
            numcolumns = scanner.nextInt();
            terrain2D = new Float[numrows][numcolumns];
            sequentialBasins = parallelBasins = new Boolean[numrows][numcolumns];

                for (int i = 0; i < numrows; i++) {
                    Arrays.fill(sequentialBasins[i], false); //default to not basin
                    Arrays.fill(parallelBasins[i], false);
                        for (int j = 0; j < numcolumns; j++) {
                            terrain2D[i][j]= scanner.nextFloat();

                        }
                }
            }

         catch (IOException e) {
            e.printStackTrace();
        }
        return terrain2D;
    }

    public static void main(String[] args){
        formulateArray(args); //creates array from data
        for (int i = 0; i < 1; i++) {
            check_basins(terrain2D); //checks and prints/generates required output
            System.out.println((t1-t0)/1000000.0 );
        }
        //TODO output results to file in arguments of main

        //System.out.println(t1-t0 );
        //Write to output file
        try {
            FileWriter myWriter = new FileWriter(args[1]);//use args[1]

            //System.out.println(noBasins);
            myWriter.write(noBasins+"\n");

            for (int i = 0; i < sequentialBasins.length; i++) {
                for (int j = 0; j < sequentialBasins[i].length; j++) {
                    if(sequentialBasins[i][j]) {
                        //System.out.println(i+" "+j);
                        myWriter.write(i+" "+j+"\n");
                    }
                }
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void check_basins(Float[][] terrain2D){
        //skip checking edge top, bottom, left, right coordinates.
        System.gc();
        t0 = System.nanoTime(); //currentTimeMillis();
        for (int i = 0; i < terrain2D.length; i++) {
            for (int j = 0; j < terrain2D.length; j++) {

                boolean coordinate = check_neighbours(terrain2D, i, j);
                if (coordinate) {
                    //basins.add(coordinate);
                    //set returned indices true - default false/null
                    sequentialBasins[i][j] = true;
                }
            }
        }
        t1=System.nanoTime(); //currentTimeMillis();
    }

    public static boolean check_neighbours(Float[][] terrain2D, int i ,int j){
        Float offset = terrain2D[i][j]+ 0.01f;

        try {
            if (terrain2D[i][j + 1] >= offset && terrain2D[i][j - 1] >= offset && terrain2D[i + 1][j] >= offset && terrain2D[i - 1][j] >= offset &&
            terrain2D[i-1][j + 1] >= offset && terrain2D[i-1][j - 1] >= offset && terrain2D[i + 1][j+1] >= offset && terrain2D[i+1][j-1] >= offset) {
             //everything surrounding ij is above offset return.
                noBasins++; //count basins
                return true;
            }
            else return false;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return false; // i.e edge
            //e.printStackTrace();
        }


    }
}