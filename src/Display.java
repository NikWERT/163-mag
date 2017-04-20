import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class Display {
    private static JFrame display;
    private JPanel mainPanel;

    private int width;
    private int height;

    private int countPanel;

    private JPanel currentChartPanel;
    private JPanel[] panels;

    //Создание основного окна JFrame и заполнение его
    public Display(int width, int height) {
        this.width = width;
        this.height = height;

        display = new JFrame("Распределение температур в толщине ограждающей конструкции. v.0.1");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setPreferredSize(new Dimension(width, height));
        display.setLayout(new BorderLayout());
        display.setLocationRelativeTo(null);
        display.setLocation(display.getX() - width / 2, display.getY() - height / 2);
        display.setResizable(false);

        addJMenuBar();

        currentChartPanel = new JPanel();
        mainPanel = new JPanel(new FlowLayout());
        mainPanel.add(currentChartPanel);
        display.add(mainPanel, BorderLayout.BEFORE_FIRST_LINE);

        display.pack();
        display.setVisible(true);

//      temp();
    }

    //Это что?
//    private void temp() {
//        Canvas[] canvas = new Canvas[countPanel];
//
//        for (int i = 0; i < countPanel; i++) {
//            canvas[i] = new Canvas();
//            canvas[i].setSize(new Dimension(300, 200));
//            canvas[i].setBackground(new Color(20 * i, 250 - 20 * i, 150 + 5 * i));
//
//            panels[i].add(canvas[i]);
//        }
//
//        canvas[0].setBackground(Color.RED);
//        panels[0].add(canvas[0]);
//    }

    //Выпадающие меню сверху
    private void addJMenuBar() {
        JMenu menuFile = new JMenu("Файл");
        JMenuItem menuFileOpen = new JMenuItem(new OpenFile());

        JMenu menuFileExport = new JMenu("Экспорт");
        JMenuItem menuFileExportSingle = new JMenuItem(new ExportCurrent());
        JMenuItem menuFileExportAll = new JMenuItem(new ExportSequence());
        menuFileExport.add(menuFileExportSingle);
        menuFileExport.add(menuFileExportAll);

        JMenuItem menuFileExit = new JMenuItem(new ExitAction());

        JMenu menuAbout = new JMenu("О программе");
        JMenuItem menuAboutHelp = new JMenuItem(new HelpWindow());
        JMenuItem menuAboutCreators = new JMenuItem(new CreatorsWindow());

        menuFile.add(menuFileOpen);
        menuFile.add(menuFileExport);
        menuFile.add(menuFileExit);
        menuAbout.add(menuAboutHelp);
        menuAbout.add(menuAboutCreators);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuAbout);

        display.setJMenuBar(menuBar);
    }

    private void initJPanels() {
        panels = new JPanel[countPanel];
        for (int i = 0; i < countPanel; i++) {
            panels[i] = new JPanel();
        }
    }

    public void addChartOnPanel(XChartPanel chart, int indexPanel) {
        panels[indexPanel].removeAll();
        panels[indexPanel].add(chart);
    }

    private void changePanel(int nextPanel) {
        currentChartPanel.removeAll();
        currentChartPanel.add(panels[nextPanel]);
    }

    //Вкладки
    private void addJTabbedPane(int countTabbedPanels) {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
        for (int i = 0; i < countPanel; i++) {
            JPanel panel = new JPanel();
            tabbedPane.add("Chart " + i, panel);
        }

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int nextPanel = tabbedPane.getSelectedIndex();
                changePanel(nextPanel);
            }
        });

        initJPanels();
        display.add(tabbedPane, BorderLayout.SOUTH);
    }

    //Отрисовка графиков
    //public void setGrafics(ArrayList<ArrayList>[] data) {
//        data[0] - даты
//        data[1] - температуры
//        addJTabbedPane(data[0].size());
//        double[] xData = {100, 100, 100};
//
//        for (int i = 0; i < data[1].size(); i++) {
//            double[] yData = {20, 25, 30};
//            XYChart chart = QuickChart.getChart("График разницы температур слоев", "Слои", "Температура", "y(x)", xData, yData);
//            //XYChart chart = QuickChart.getChart("График", "Стены","Температуры","Тест",xData, yData);
//            XChartPanel Graf = new XChartPanel(chart);
//            addChartOnPanel(Graf, i);
//        }

    public void setGrafics(double[][] data) {
        countPanel = data.length;
        addJTabbedPane(6);

//        addJTabbedPane(data.length);

        for (int i = 0; i < countPanel; i++) {
            double[] xData = new double[data[i].length];
            for (int j = 0; j < data[i].length; j++) {
                xData[j] = j;
            }

            XYChart chart = QuickChart.getChart("График разницы температур слоев", "Слои", "Температура", "y(x)", xData, data[i]);
            XChartPanel Graf = new XChartPanel(chart);
            addChartOnPanel(Graf, i);
        }

    }

    public static void notifyMessage(String error, String title, int msgType) {
        JOptionPane.showMessageDialog(display, error, title, msgType);
    }
}

/**
 * Чисто утилитарные классы
 */
class ExitAction extends AbstractAction {
    ExitAction() {
        putValue(NAME, "Выход");
    }

    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}

class OpenFile extends  AbstractAction {
    OpenFile() {
        putValue(NAME, "Открыть CSV...");
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV-файл из программы Excel", "csv"));
        jFileChooser.showOpenDialog(null);
    }
}

class ExportCurrent extends  AbstractAction {
    ExportCurrent() {
        putValue(NAME, "Текущего кадра...");
    }

    public void actionPerformed(ActionEvent e) {

    }
}

class ExportSequence extends  AbstractAction {
    ExportSequence() {
        putValue(NAME, "Всех кадров...");
    }

    public void actionPerformed(ActionEvent e) {

    }
}

class HelpWindow extends  AbstractAction {
    HelpWindow() {
        putValue(NAME, "Справка");
    }

    public void actionPerformed(ActionEvent e) {
        Display.notifyMessage("ЭТО СПРАВКАААА!!!!111одын", "Справка:", PLAIN_MESSAGE);
    }
}

class CreatorsWindow extends  AbstractAction {
    CreatorsWindow() {
        putValue(NAME, "Авторы");
    }

    public void actionPerformed(ActionEvent e) {
        Display.notifyMessage(
                "Акимов Алексей\n" +
                        "Прижуков Никита\n" +
                        "Пстыга Екатерина\n" +
                        "Серебренникова Екатерина\n\n" +
                        "Отдельное спасибо Ткачёву Дмитрию\n\n" +
                        "НГАСУ Сибстрин\n" +
                        "Кафедра ИСТ\n" +
                        "2017", "Авторы:", PLAIN_MESSAGE);
    }
}