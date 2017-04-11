import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class parsingTest {
    public static void main(String[] args) {
        parsedata parser = new parsedata();
        while (true){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите имя csv-файла без расширения: resources\\");
            try{
            ArrayList<ArrayList> arrList = parser.parceCSV("resources/"+bufferedReader.readLine()+".csv");

            System.out.println("colnames:");
            for (int i = 0; i < parser.getColnames().length; i++) {
                System.out.print(parser.getColnames()[i] + "\t");
            }

            System.out.println("\nArray:");
            for (ArrayList array : arrList) {
                for (int i = 0; i < array.size(); i++) {
                    System.out.print(array.get(i)+ "\t");
                }
                System.out.println("");
            }
                System.out.println("\nФайл успешно загружен!");
                break;
        }
        catch (FileNotFoundException f){
        }
        catch (IOException e){
            //e.printStackTrace();
        }
    }
    }
}