import java.io.*;
import java.util.ArrayList;

//  Created by Lex on 14.03.2017.

public class Parsedata {
    private ArrayList<String[]> input_array = new ArrayList<>();
    private String[] colnames;
    private double maxTemp;
    private double minTemp;

    public Parsedata(String inputfile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(inputfile)), "Windows-1251"));
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            String[] data = string.split(";");
            input_array.add(data);
        }
        bufferedReader.close();

        colnames = input_array.get(0);
        input_array.remove(0);

        // Проверки:
        // Во всех строках одинаковое количество столбцов.
        int numberCols = input_array.get(0).length;
        for (int i = 0; i < input_array.size(); i++) {
            if (numberCols != input_array.get(i).length) {
                if (i == 1) {
                    throw new IOException("Ошибка в файле - неверное количество столбцов в строке №: 1;");
                }
                throw new IOException("Ошибка в файле - неверное количество столбцов в строке №: " + (i + 1) + ";");
            }
        }

        // Нету пустых ячеек:
        for (int i = 0; i < input_array.size(); i++) {
            for (int j = 0; j < input_array.get(i).length; j++) {
                if (input_array.get(i)[j].isEmpty()) {
                    throw new IOException("Ошибка в файле - Пустая клетка: Cтрока: " + (i + 1) + ", cтолбец: " + j + ";");
                }
            }
        }
    }

    public String[] getColnames() {
        return colnames;
    }

    public String[] getDataArray() {
        String[] datesArray = new String[input_array.size()];
        for (int i = 0; i < input_array.size(); i++) {
            for (int j = 0; j < input_array.get(0).length; j++) {

                if (colnames[j].startsWith("Д") || colnames[j].startsWith("д")) {
                    datesArray[i] = input_array.get(i)[j];
                }
            }
        }
        return datesArray;
    }

    public double[][] getTemperatureArray() {
        double[][] temperaturesArray = new double[input_array.size()][];

        for (int i = 0; i < input_array.size(); i++) {
            ArrayList<Double> tempString = new ArrayList<>();
            for (int j = 0; j < input_array.get(0).length; j++) {
                if (colnames[j].startsWith("t") || colnames[j].startsWith("T")) {
                    if (input_array.get(i)[j].contains(",")) {
                        tempString.add(Double.parseDouble(input_array.get(i)[j].replace(",", ".")));
                    } else tempString.add(Double.parseDouble(input_array.get(i)[j]));
                }
            }
            double[] stroka = new double[tempString.size()];
            for (int j = 0; j < tempString.size(); j++) {
                if (maxTemp < tempString.get(j)) maxTemp = tempString.get(j);
                if (minTemp > tempString.get(j)) minTemp = tempString.get(j);
                stroka[j] = tempString.get(j);
            }
            temperaturesArray[i] = stroka;
        }
        return temperaturesArray;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }
}