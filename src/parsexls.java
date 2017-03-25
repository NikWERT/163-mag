import java.io.*;
import java.util.ArrayList;


/**
 * Created by Алексей on 14.03.2017.
 */
public class parsexls {
    public static void main(String[] args) {
        readFile("resources/file.csv");
    }

    private static void readFile(String inputfile) {
        String string;
        ArrayList dataarray = new ArrayList();

        try {
            BufferedReader file = new BufferedReader(new FileReader(inputfile));
            while ((string = file.readLine()) != null) {
                String[] data = string.split(";");
                for (int i = 0; i < data.length; i++) {
                    System.out.println(data[i]);
                    dataarray.add(data[i]);
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


