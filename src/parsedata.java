import java.io.*;
import java.util.ArrayList;
import java.util.Date;

//  Created by Lex on 14.03.2017.
//  Interface: имяПеременной.get(номер строки).get(номер столбца);

class parsedata{
    ArrayList<Object[]> input_array = new ArrayList<>();
    static ArrayList<ArrayList> outputCollection = new ArrayList<>();
    static String[] colnames;
    public String[] getColnames() {
        return colnames;
    }

    public ArrayList<ArrayList> parceCSV(String inputfile) throws IOException {
// Загрузка файла во входной лист ArrayList<Object[]> input_array
        try {
            //BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(inputfile)), "Windows-1251"));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] data = string.split(";");
                input_array.add(data);
            }
            bufferedReader.close();

// Разделение на 2 массива: массив с именами столбцов (String[] colnames) и лист ArrayList<Object[]> input_array
            colnames = (String[]) input_array.get(0);
            input_array.remove(0);

// Проверки:
    // Во всех строках одинаковое количество столбцов.
            int numberCols = input_array.get(0).length;
            for (int i = 0; i < input_array.size(); i++) {
                if (numberCols != input_array.get(i).length){
                    if (i == 1){
                        throw new IOException("Ошибка в файле - неверное количество столбцов в строке №: 1;");
                    }
                    throw new IOException("Ошибка в файле - неверное количество столбцов в строке №: " + (i+1) + ";");
                }
            }

    // Нету пустых ячеек:
            for (int i = 0; i < input_array.size(); i++) {
                for (int j = 0; j < input_array.get(i).length; j++) {
                    if (input_array.get(i)[j].toString().isEmpty()){
                        throw new IOException("Ошибка в файле - Пустая клетка: Cтрока: " + (i+1) + " ; Столбец: " + j + ";");
                    }
                }
            }

//Если всё хорошо, то закачиваем данные в новую коллекцию массивов с преобразованием типов данных:


            for (int i = 0; i < input_array.size() ; i++) { //Цикл по строкам

                ArrayList newString = new ArrayList();

                for (int j = 0; j < input_array.get(0).length; j++) { //Цикл по элементам конкретной строки

                    if (colnames[j].startsWith("t")){
                        //заменить запятые на точки если есть
                        if (input_array.get(i)[j].toString().contains(",")){
                            newString.add(input_array.get(i)[j].toString().replace(",",".")+"f");
                        } else newString.add(input_array.get(i)[j]);
                    }

                    else if (colnames[j].startsWith("Дата")){
                        //newString.add((Date)input_array.get(i)[j]);
                    }
                }
                //записываем строку в конечную коллекцию строк
                outputCollection.add(newString);
            }
        }
        catch (IOException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return outputCollection;
    }
}


