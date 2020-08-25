import java.io.*;

public class BasinClassification {

    public static void main (String[] args) {
        String line;
        int lineNo = 0,numrows=0;


        try {
            System.out.println(args[0]);
            FileReader terrain = new FileReader("./src/"+args[0]);
            BufferedReader br =new BufferedReader(terrain);

            while ((line = br.readLine()) != null) {
                if (0 == lineNo) {
                    //get grid row x col size
                    numrows = Integer.parseInt(line.split(" ")[0]);
                    
                }
                Double[][] terrain2D = new Double[numrows][numrows];
                if (1 == lineNo) {
                    //rows and columns data, generate 2D array
                    String[] temp = line.split(" ");

                    for (int i = 0; i < numrows; i++) {
                        for (int j = 0; j < numrows; j++) {
                            terrain2D[i][j]= Double.parseDouble(temp[i+j]);

                        }
                    }

                }
                lineNo++;
                //checks and prints/generates required output
                check_basins(terrain2D);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void check_basins(Double[][] terrain2D){

        for (int i = 0; i < terrain2D.length; i++) {
            for (int j = 0; j < terrain2D.length; j++) {

                check_neighbours(terrain2D, i, j);
                //TODO Index out of bounds error handling
            }
        }
    }

    public static void check_neighbours(Double[][] terrain2D, int i ,int j){
        Double offset = terrain2D[i][j]+ 0.01;
        if ( terrain2D[i][j+1] >offset){

        }


    }
}
