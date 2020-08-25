import java.io.*;
import java.util.LinkedList;

public class BasinClassification {

    public static void main (String[] args) {
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
                Double[][] terrain2D = new Double[numrows][numcolumns];
                if (1 == lineNo) {
                    //rows and columns data, generate 2D array
                    String[] temp = line.split(" ");
                    int k=0;
                    for (int i = 0; i < numrows; i++) {
                        for (int j = 0; j < numcolumns; j++) {
                            terrain2D[i][j]= Double.parseDouble(temp[k++]);

                        }
                    }
                    check_basins(terrain2D);
                }
                lineNo++;
                //checks and prints/generates required output

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void check_basins(Double[][] terrain2D){

        LinkedList<int[]> basins = new LinkedList<>();
        for (int i = 0; i < terrain2D.length; i++) {
            for (int j = 0; j < terrain2D.length; j++) {

                int[] coordinate = check_neighbours(terrain2D, i, j);
                if (coordinate != null) {
                    basins.add(coordinate);
                }
                //TODO Index out of bounds error handling
            }
        }
        System.out.println(basins.size()); //Number of found basins
        for(int[] basin: basins ){
            System.out.print(basin[0]);
            System.out.println(basin[1]);
        }
    }

    public static int[] check_neighbours(Double[][] terrain2D, int i ,int j){
        Double offset = terrain2D[i][j]+ 0.01;

        try {
            if (terrain2D[i][j + 1] > offset && terrain2D[i][j - 1] > offset && terrain2D[i + 1][j] > offset && terrain2D[i - 1][j] > offset &&
            terrain2D[i-1][j + 1] > offset && terrain2D[i-1][j - 1] > offset && terrain2D[i + 1][j+1] > offset && terrain2D[i+1][j-1] > offset) {
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
