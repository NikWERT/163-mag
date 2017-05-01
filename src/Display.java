import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static javax.swing.JOptionPane.*;

public class Display implements ActionListener, KeyListener {
    private JFrame display;
    private JPanel mainPanel;  //С этой панели рендерится картинка!!!!!

    private JMenuItem menuFileOpen;
    private JMenu menuFileExport;
    private JMenuItem menuFileExportSingle;
    private JMenuItem menuFileExportAll;
    private JMenuItem menuFileExit;
    private JMenuItem menuAboutCreators;
    private JMenuItem menuAboutHelp;
    private JButton navBeginBtn;
    private JButton navEndBtn;
    private JButton navPrevBtn;
    private JButton navNextBtn;
    private JTextField navCurrFrField;
    private JLabel navFrCntLabel;
    private JButton playBtn;
    private JButton stopBtn;
    private JLabel frameRate;
    private JTextField enterFPS;

    private int frameCount;  //общее количество кадров от 1
    private int currChart = 0; //текущий кадр (от 0 до frameCount-1)

    private Chart chartPanel;
    private ParseData parser;

    public Display(int width, int height) {
        display = new JFrame("Распределение температур в толщине ограждающей конструкции. v.0.1");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setPreferredSize(new Dimension(width, height));
        display.setLayout(new BorderLayout());
        display.setLocationRelativeTo(null);
        display.setLocation(display.getX() - width / 2, display.getY() - height / 2);
        display.setResizable(false);

        addJMenuBar();
        addPanels();

        display.pack();
        display.setVisible(true);
    }

    private void addJMenuBar() {
        JMenu menuFile = new JMenu("Файл");
        menuFileOpen = new JMenuItem("Открыть CSV...");
        menuFileOpen.addActionListener(this);

        menuFileExport = new JMenu("Экспорт");
        menuFileExport.setEnabled(false);

        menuFileExportSingle = new JMenuItem("Текущего кадра...");
        menuFileExportSingle.addActionListener(this);

        menuFileExportAll = new JMenuItem("Всех кадров...");
        menuFileExportAll.addActionListener(this);

        menuFileExport.add(menuFileExportSingle);
        menuFileExport.add(menuFileExportAll);

        menuFileExit = new JMenuItem("Выход");
        menuFileExit.addActionListener(this);

        JMenu menuAbout = new JMenu("О программе");
        menuAboutHelp = new JMenuItem("Общие указания");
        menuAboutHelp.addActionListener(this);
        menuAboutCreators = new JMenuItem("Авторы");
        menuAboutCreators.addActionListener(this);

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

    private void addPanels() {
        // Панель навигации
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBorder(new EtchedBorder());
        navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        navBeginBtn = new JButton("<<");
        navBeginBtn.addActionListener(this);
        navEndBtn = new JButton(">>");
        navEndBtn.addActionListener(this);
        navPrevBtn = new JButton("<");
        navPrevBtn.addActionListener(this);
        navNextBtn = new JButton(">");
        navNextBtn.addActionListener(this);

        navCurrFrField = new JTextField(6);
        navCurrFrField.setHorizontalAlignment(JTextField.HORIZONTAL);
        navCurrFrField.addKeyListener(this);

        navFrCntLabel = new JLabel("     ");

        navBeginBtn.setEnabled(false);
        navEndBtn.setEnabled(false);
        navPrevBtn.setEnabled(false);
        navNextBtn.setEnabled(false);
        navCurrFrField.setEnabled(false);
        navFrCntLabel.setEnabled(false);

        navigationPanel.add(navBeginBtn);
        navigationPanel.add(navPrevBtn);
        navigationPanel.add(navCurrFrField);
        navigationPanel.add(navFrCntLabel);
        navigationPanel.add(navNextBtn);
        navigationPanel.add(navEndBtn);

        // Панель воспроизведения
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(new EtchedBorder());
        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        playBtn = new JButton("PLAY");
        playBtn.addActionListener(this);
        stopBtn = new JButton("STOP");
        stopBtn.addActionListener(this);
        frameRate = new JLabel(" Частота кадров: ");
        enterFPS = new JTextField(3);
        enterFPS.setHorizontalAlignment(JTextField.HORIZONTAL);

        playBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        frameRate.setEnabled(false);
        enterFPS.setEnabled(false);

        playerPanel.add(playBtn);
        playerPanel.add(stopBtn);
        playerPanel.add(frameRate);
        playerPanel.add(enterFPS);

        // Основная верхняя панель
        mainPanel = new JPanel();

        // Нижняя Панель
        JPanel lowPanel = new JPanel();
        lowPanel.setBorder(new EtchedBorder());
        lowPanel.setLayout(new GridLayout(1, 0));
        lowPanel.add(navigationPanel);
        lowPanel.add(playerPanel);

        display.add("Center", mainPanel);
        display.add("South", lowPanel);
    }

    private void savePicture(BufferedImage img, File outputFile) throws IOException {
        mainPanel.paint(img.getGraphics());
        ImageIO.write(img, "png", outputFile);
/*
        // Create Chart
        XYChart chart=QuickChart.getChart("Sample Chart","X","Y","y(x)",xData,yData);
        // Save it
        BitmapEncoder.saveBitmap(chart,"./Sample_Chart",BitmapFormat.PNG);
        // or save it in high-res
        BitmapEncoder.saveBitmapWithDPI(chart,"./Sample_Chart_300_DPI",BitmapFormat.PNG,300);
*/
    }

    //Экшены
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuFileOpen)) {
            OpenFileAction();
        }
        if (e.getSource().equals(menuFileExportSingle)) {
            ExportSingleAction();
        }
        if (e.getSource().equals(menuFileExportAll)) {
            ExportAllAction();
        }
        if (e.getSource().equals(menuFileExit)) {
            System.exit(0);
        }
        if (e.getSource().equals(menuAboutHelp)) {
            HelpDialogAction();
        }
        if (e.getSource().equals(menuAboutCreators)) {
            CreatorsDialogAction();
        }
//Кнопки в окне
        if (e.getSource().equals(navBeginBtn)) {
            buttonBeginAction();
        }
        if (e.getSource().equals(navEndBtn)) {
            buttonEndAction();
        }
        if (e.getSource().equals(navPrevBtn)) {
            buttonPreviousAction();
        }
        if (e.getSource().equals(navNextBtn)) {
            buttonNextAction();
        }
        if (e.getSource().equals(playBtn)) {
            buttonPlayAction();
        }
        if (e.getSource().equals(stopBtn)) {
            buttonStopAction();
        }
    }

    private void buttonStopAction() {
        if (frameCount > 1 && playBtn.getText().equals("PAUSE")) {
            playBtn.setText("PLAY");
        }
    }

    private void buttonPlayAction() {
        if (playBtn.getText().equals("PLAY")) {
            playBtn.setText("PAUSE");
            //currChart = 480;
            int playFrom = currChart;
            int playFPS = 100;
            try {
                playFPS = Math.round(1000 / Integer.parseInt(enterFPS.getText()));
                while (currChart < frameCount - 1) {
                    currChart++;
                    Thread.sleep(playFPS);
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            navCurrFrField.setText(currChart + 1 + "");
                            chartPanel.update(currChart, parser);
                            display.repaint();
                        }
                    });
                }
            }
            catch (InterruptedException inter) {
                System.out.println(inter.getMessage());
                inter.printStackTrace();
            }
            catch (InvocationTargetException inter) {
                System.out.println(inter.getMessage());
                inter.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                enterFPS.setText(100 + "");
                playFPS = 100;
                JOptionPane.showMessageDialog(display, "Введите целое положительное число", "Ошибка!", ERROR_MESSAGE);
            }
            playBtn.setText("PLAY");

        } else if (playBtn.getText().equals("PAUSE")) {
            playBtn.setText("PLAY");
        }
    }

    private void buttonNextAction() {
        if (frameCount > 1 && currChart < frameCount - 1) {
            currChart++;
            navCurrFrField.setText(currChart + 1 + "");
            chartPanel.update(currChart, parser);
            chartPanel.repaint();
        }
    }

    private void buttonPreviousAction() {
        if (frameCount > 1 && currChart > 0) {
            currChart--;
            navCurrFrField.setText(currChart + 1 + "");
            chartPanel.update(currChart, parser);
            chartPanel.repaint();
        }
    }

    private void buttonEndAction() {
        if (frameCount > 1 && currChart < frameCount - 1) {
            currChart = frameCount - 1;
            navCurrFrField.setText(currChart + 1 + "");
            chartPanel.update(currChart, parser);
            chartPanel.repaint();
        }
    }

    private void buttonBeginAction() {
        if (frameCount > 1) {
            currChart = 0;
            navCurrFrField.setText(currChart + 1 + "");
            chartPanel.update(currChart, parser);
            chartPanel.repaint();
        }
    }

    private void CreatorsDialogAction() {
        JOptionPane.showMessageDialog(
                display,
                "Акимов Алексей\n" + "Прижуков Никита\n" + "Пстыга Екатерина\n" + "Серебренникова Екатерина\n\n" +
                        "Отдельное спасибо Ткачёву Дмитрию\n\n" + "НГАСУ Сибстрин\n" + "Кафедра ИСТ\n" + "2017", "Авторы:",
                PLAIN_MESSAGE);
    }

    private void HelpDialogAction() {
        JOptionPane.showMessageDialog(display,
                "\n1. Исходный файл должен содержать столбец с датой в любом формате." +
                        "\nСтолбцы с показаниями датчиков температуры должны иметь в заглавии английскую букву \"t\"" +
                        "\n2. Сохраните файл из MS Excel в формате \"CSV разделители запятые.\"" +
                        "\n3. Загрузите файл в программу через меню \"Файл -> Открыть CSV...\"" +
                        "\n4. Используйте кнопки на панели навигации для переключения между показаниями датчиков." +
                        "\n5. При необходимости можно сохранить изображение текущего или всех показаний через меню \"Файл -> Экспорт\"",
                "Общие указания по работе с программой:", PLAIN_MESSAGE);
    }

    private void ExportAllAction() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Экспорт всех кадров");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Изображение в формате PNG", "png"));
        int returnValue = jFileChooser.showSaveDialog(display);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            BufferedImage img = new BufferedImage(mainPanel.getWidth(), mainPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            try {
                for (int i = 0; i < frameCount; i++) {
                    File file = new File(jFileChooser.getSelectedFile().getAbsolutePath() + "_" + String.format("%06d", (i + 1)) + ".png");
                    savePicture(img, file);
                    currChart = i;
                    navCurrFrField.setText(currChart + 1 + "");
                    chartPanel.update(currChart, parser);
                    chartPanel.repaint();
                }
                JOptionPane.showMessageDialog(display, frameCount + " файлов успешно сохранёно.", "", INFORMATION_MESSAGE);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(display, exc.getMessage(), "Ошибка", ERROR_MESSAGE);
            }
        }
    }

    private void ExportSingleAction() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Экспорт текущего кадра");
        jFileChooser.setSelectedFile(new File(parser.getDataArray()[currChart].replace(".", "-").replace(":", "-")));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Изображение в формате PNG", "png"));
        int returnValue = jFileChooser.showSaveDialog(display);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            BufferedImage img = new BufferedImage(mainPanel.getWidth(), mainPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            File file = new File(jFileChooser.getSelectedFile().getAbsolutePath() + ".png");
            try {
                savePicture(img, file);
                JOptionPane.showMessageDialog(display, "Файл " + file.getName() + " сохранён.", "", INFORMATION_MESSAGE);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(display, exc.getMessage(), "Ошибка", ERROR_MESSAGE);
            }
        }
    }

    private void OpenFileAction() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setDialogTitle("Открыть CSV-файл");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV-файл из программы Excel", "csv"));
        int returnValue = jFileChooser.showOpenDialog(display);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                parser = new ParseData(jFileChooser.getSelectedFile().toString());
                enterFPS.setText(100 + "");
                frameCount = parser.getDataArray().length;
                chartPanel = new Chart(parser);
                mainPanel.add(chartPanel);
                menuFileExport.setEnabled(true);
                navBeginBtn.setEnabled(true);
                navEndBtn.setEnabled(true);
                navPrevBtn.setEnabled(true);
                navNextBtn.setEnabled(true);
                navCurrFrField.setEnabled(true);
                navFrCntLabel.setEnabled(true);
                playBtn.setEnabled(true);
                stopBtn.setEnabled(true);
                frameRate.setEnabled(true);
                enterFPS.setEnabled(true);
                navCurrFrField.setText(currChart + 1 + "");
                navFrCntLabel.setText(" из " + frameCount);
                JOptionPane.showMessageDialog(display, "Файл успешно загружен!", "", INFORMATION_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(display, exc.toString(), "Ошибка!", ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource().equals(navCurrFrField)){
            if (e.getKeyChar() == KeyEvent.VK_ENTER) try {
                int newcurrChart = Integer.parseInt(navCurrFrField.getText());
                if (newcurrChart > 0 && newcurrChart <= frameCount) {
                    currChart = newcurrChart - 1;
                } else if (newcurrChart > frameCount) {
                    currChart = frameCount - 1;
                } else if (newcurrChart < 1){
                    currChart = 0;
                }

            } catch (NumberFormatException ignored) {
                System.out.println(ignored.getMessage());

            } finally {
                chartPanel.update(currChart, parser);
                mainPanel.repaint();
                navCurrFrField.setText(currChart + 1 + "");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}