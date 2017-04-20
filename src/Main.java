import java.util.ArrayList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class Main {
    public static void main(String[] args) {
        /*Display display = new Display(1000, 700);

        try {
            Parsedata parser = new Parsedata();
            ArrayList<ArrayList>[] arrList = parser.parseCSV("resources/file.csv");
            display.setGrafics(arrList);
            display.notifyMessage("Файл успешно загружен!", "", INFORMATION_MESSAGE);
        }
        catch (Exception e) {
            display.notifyMessage(e.getMessage(), "Ошибка!", ERROR_MESSAGE);
        }*/


        double[] data1 = new double[]{2, 5, 3};
        double[] data2 = new double[]{5, 7, 3, 5};
        double[] data3 = new double[]{7, 2, 6, 3, 4, 8, 9};

        double[][] data = new double[3][];
        data[0] = data1;
        data[1] = data2;
        data[2] = data3;

        Display display = new Display(1000, 700);

        display.setGrafics(data);



    }
}