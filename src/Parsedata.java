import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

//  Created by Lex on 14.03.2017.
//  имяПеременной.get(номер строки).get(номер столбца);

public class Parsedata {
    private ArrayList<String[]> input_array = new ArrayList<>();
    private String[] colnames;
    private ArrayList<Date> outputDates = new ArrayList<>();
    private ArrayList<ArrayList<Float>> outputFloats = new ArrayList<>();
    //private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy  H:mm:ss");
    //private SimpleDateFormat format1 = new SimpleDateFormat("H:mm:ss");

    String[] getColnames() {
        return colnames;
    }

    public ArrayList<ArrayList>[] parseCSV(String inputfile) throws IOException, ParseException, NumberFormatException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(inputfile)), "Windows-1251"));
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            String[] data = string.split(";");
            input_array.add(data);
        }
        bufferedReader.close();

// Разделение на 2 массива: массив с именами столбцов (String[] colnames) и лист ArrayList<String[]> input_array
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

//      Если всё хорошо, то закачиваем данные в коллекции:
//      ArrayList<String> outputDates
//      ArrayList<Float> outputFloats
//      с преобразованием типов данных:

//      Цикл по строкам
        for (int i = 0; i < input_array.size(); i++) {
            Date newDateString = new Date();
            ArrayList<Float> newFloatString = new ArrayList<>();

            //Цикл по элементам конкретной строки
            for (int j = 0; j < input_array.get(0).length; j++) {
                //значения температур во флоат:
                if (colnames[j].startsWith("t") || colnames[j].startsWith("T")) {
                    //заменить запятые на точки если есть
                    if (input_array.get(i)[j].contains(",")) {
                        newFloatString.add(Float.parseFloat(input_array.get(i)[j].replace(",", ".")));
                    } else newFloatString.add(Float.parseFloat(input_array.get(i)[j]));
                }
                //Дату в дату
                else if (colnames[j].startsWith("Дата")) {
                    newDateString = new Date();
                    //newDateString = format.parse(input_array.get(i)[j]);
                }
            }
            //записываем строку в конечную коллекцию строк
            outputDates.add(newDateString);
            outputFloats.add(newFloatString);
        }

        ArrayList[] output = new ArrayList[2];
        output[0] = outputDates;
        output[1] = outputFloats;
        return output;
    }
}