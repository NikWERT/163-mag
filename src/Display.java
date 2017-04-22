import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class Display {
    public JFrame display;
    public JPanel mainPanel;  //С этой панели рендерится картинка!!!!!
    static JMenuBar menuBar;
    static JButton navBeginBtn;
    static JButton navEndBtn;
    static JButton navPrevBtn;
    static JButton navNextBtn;
    static JTextField currentFrame;
    static JLabel frameCount;
    static JButton playBtn;
    static JButton stopBtn;
    static JLabel frameRate;
    static JTextField enterFPS;
    static JMenu menuFileExport;

    private JPanel currentChartPanel;
    private JPanel[] panels;
    private int countPanel;

    //Создание окна JFrame и заполнение его
    public Display(int width, int height) {
        display = new JFrame("Распределение температур в толщине ограждающей конструкции. v.0.1");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setPreferredSize(new Dimension(width, height));
        display.setLayout(new BorderLayout());
        display.setLocationRelativeTo(null);
        display.setLocation(display.getX() - width / 2, display.getY() - height / 2);
        display.setResizable(false);

        addPanels();
        addJMenuBar();

        display.pack();
        display.setVisible(true);
    }

    // Выпадающие меню сверху
    public void addJMenuBar() {
        JMenu menuFile = new JMenu("Файл");
        JMenuItem menuFileOpen = new JMenuItem(new OpenFile());

        menuFileExport = new JMenu("Экспорт");
        menuFileExport.setEnabled(false);
        JMenuItem menuFileExportSingle = new JMenuItem(new ExportCurrent(mainPanel));
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

        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuAbout);

        display.setJMenuBar(menuBar);
    }

    // Панели
    private void addPanels() {
        // Панель навигации
        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.setPreferredSize(new Dimension(300, 50));
        navigationPanel.setBorder(new EtchedBorder());

        navBeginBtn = new JButton("<<");
        navEndBtn = new JButton(">>");
        navPrevBtn = new JButton("<");
        navNextBtn = new JButton(">");
        currentFrame = new JTextField("");
        frameCount = new JLabel("");

        navBeginBtn.setEnabled(false);
        navEndBtn.setEnabled(false);
        navPrevBtn.setEnabled(false);
        navNextBtn.setEnabled(false);
        currentFrame.setEnabled(false);
        frameCount.setEnabled(false);

        navigationPanel.add(navBeginBtn);
        navigationPanel.add(navPrevBtn);
        navigationPanel.add(currentFrame);
        navigationPanel.add(frameCount);
        navigationPanel.add(navNextBtn);
        navigationPanel.add(navEndBtn);
        // ---------------------------------------------------------------------

        // Панель воспроизведения
        // ---------------------------------------------------------------------
        JPanel playerPanel = new JPanel(new FlowLayout());
        playerPanel.setPreferredSize(new Dimension(300, 50));
        playerPanel.setBorder(new EtchedBorder());

        playBtn = new JButton("PLAY"); //play/pause
        stopBtn = new JButton("STOP");
        frameRate = new JLabel("Частота кадров: ");
        enterFPS = new JTextField("15");

        playBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        frameRate.setEnabled(false);
        enterFPS.setEnabled(false);

        playerPanel.add(playBtn);
        playerPanel.add(stopBtn);
        playerPanel.add(frameRate);
        playerPanel.add(enterFPS);
        // ---------------------------------------------------------------------

        // Нижняя Панель (на которой панели навигации и воспроизведения)
        // ---------------------------------------------------------------------
        JPanel lowPanel = new JPanel(new FlowLayout());
        lowPanel.setPreferredSize(new Dimension(1, 60));
        lowPanel.setBorder(new EtchedBorder());
        lowPanel.add(navigationPanel);
        lowPanel.add(playerPanel);


        // Основная верхняя панель (на ней график. с неё рендерится)
        // ---------------------------------------------------------------------
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EtchedBorder());

        JLabel dateLabel = new JLabel("Тут типа дата будет");
        mainPanel.add(dateLabel, BorderLayout.NORTH);

        //Никитина Панель
        //currentChartPanel = new JPanel();
        //mainPanel.add(currentChartPanel);

        display.add(mainPanel, BorderLayout.CENTER);
        display.add(lowPanel, BorderLayout.SOUTH);
    }

//    private void initJPanels() {
//        panels = new JPanel[countPanel];
//        for (int i = 0; i < countPanel; i++) {
//            panels[i] = new JPanel();
//        }
//    }

    public void addChartOnPanel(XChartPanel chart, int indexPanel) {
        panels[indexPanel].removeAll();
        panels[indexPanel].add(chart);
    }

//    private void changePanel(int nextPanel) {
//        currentChartPanel.removeAll();
//        currentChartPanel.add(panels[nextPanel]);
//    }
//
//    private void addJTabbedPane(int countTabbedPanels) {
//        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
//        for (int i = 0; i < countPanel; i++) {
//            JPanel panel = new JPanel();
//            tabbedPane.add("Chart " + i, panel);
//        }
//
//        tabbedPane.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                int nextPanel = tabbedPane.getSelectedIndex();
//                changePanel(nextPanel);
//            }
//        });
//
//        initJPanels();
//        display.add(tabbedPane, BorderLayout.SOUTH);
//    }

    // Отрисовка графиков
    public void setGrafics(ArrayList<ArrayList>[] data) {
//        data[0] - даты
//        data[1] - температуры
        //addJTabbedPane(data[0].size());
        double[] xData = {100, 100, 100}; //толщины

        for (int i = 0; i < data[1].size(); i++) {
            double[] yData = {20, 25, 30};
            XYChart chart = QuickChart.getChart("График разницы температур слоев", "Слои", "Температура", "y(x)", xData, yData);
            //XYChart chart = QuickChart.getChart("График", "Стены","Температуры","Тест",xData, yData);
            XChartPanel Graf = new XChartPanel(chart);
            addChartOnPanel(Graf, i);
        }
    }

//    public void setGrafics(double[][] data) {
//        countPanel = data.length;
//        //addJTabbedPane(data.length);
//        double[] xData = new double[4];
//        xData[0] = 0;
//        xData[1] = 4;
//        xData[2] = 8;
//        xData[3] = 12;
//
//        for (int i = 0; i < countPanel; i++) {
//            XYChart chart = QuickChart.getChart("График разницы температур слоев", "Слои", "Температура", "y(x)", xData, data[i]);
//            XChartPanel Graf = new XChartPanel(chart);
//            addChartOnPanel(Graf, i);
//        }
//    }

    public static void notifyMessage(String error, String title, int msgType) {
        JOptionPane.showMessageDialog(null, error, title, msgType);
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

class OpenFile extends AbstractAction {
    OpenFile() {
        putValue(NAME, "Открыть CSV...");
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV-файл из программы Excel", "csv"));
        int returnValue = jFileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Parsedata parser = new Parsedata();
                ArrayList<ArrayList>[] arrList = parser.parseCSV(jFileChooser.getSelectedFile().toString());
                //Main.display.setGrafics(arrList);


                Display.menuFileExport.setEnabled(true);
                Display.navBeginBtn.setEnabled(true);
                Display.navEndBtn.setEnabled(true);
                Display.navPrevBtn.setEnabled(true);
                Display.navNextBtn.setEnabled(true);
                Display.currentFrame.setEnabled(true);
                Display.frameCount.setEnabled(true);
                Display.playBtn.setEnabled(true);
                Display.stopBtn.setEnabled(true);
                Display.frameRate.setEnabled(true);
                Display.enterFPS.setEnabled(true);
                Display.currentFrame.setText("1");
                Display.frameCount.setText(" из " + arrList[0].size());


                Display.notifyMessage("Файл успешно загружен!", "", INFORMATION_MESSAGE);
            } catch (Exception exc) {
                Display.notifyMessage(exc.getMessage(), "Ошибка!", ERROR_MESSAGE);
            }
        }
    }
}

class ExportCurrent extends AbstractAction {

    static private JPanel panelToRender;

    ExportCurrent(JPanel mainPanel) {
        putValue(NAME, "Текущего кадра...");
        panelToRender = mainPanel;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Изображение в формате PNG", "png"));
        int returnValue = jFileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            BufferedImage img = new BufferedImage(panelToRender.getWidth(), panelToRender.getHeight(), BufferedImage.TYPE_INT_RGB);
            panelToRender.paint(img.getGraphics());

            try {
                File file = new File(jFileChooser.getSelectedFile().getAbsolutePath() + ".png");
                System.out.println(file);
                ImageIO.write(img, "png", file);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}

class ExportSequence extends AbstractAction {
    ExportSequence() {
        putValue(NAME, "Всех кадров...");
    }

    public void actionPerformed(ActionEvent e) {

    }
}

class HelpWindow extends AbstractAction {
    HelpWindow() {
        putValue(NAME, "Справка");
    }

    public void actionPerformed(ActionEvent e) {
        Display.notifyMessage("ЭТО СПРАВКАААА!!!!111одын", "Справка:", PLAIN_MESSAGE);
    }
}

class CreatorsWindow extends AbstractAction {
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