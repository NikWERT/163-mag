import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

public class Chart extends JPanel implements KeyListener {
    private List<JTextField> thicks;
    private XYChart xyChart;
    private static double[] xdata = {0, 100, 200, 300};

    void update() {
        xyChart.setTitle(Display.parser.getDataArray()[Display.currChart]);
        xyChart.updateXYSeries("xySeries", xdata, Display.parser.getTemperatureArray()[Display.currChart], null);
        this.repaint();
    }

    Chart(ParseData parsedData) {
        xyChart = QuickChart.getChart(parsedData.getDataArray()[0], "Слои", "Температура", "xySeries", xdata, parsedData.getTemperatureArray()[0]);
        xyChart.getStyler().setLegendVisible(false);
        xyChart.getStyler().setYAxisMin(parsedData.getMinTemp() - 5);
        xyChart.getStyler().setYAxisMax(parsedData.getMaxTemp() + 5);

        this.setLayout(new BorderLayout());
        XChartPanel<XYChart> xChartPanel = new XChartPanel<>(xyChart);

        JPanel thickness = new JPanel();

        thicks = new LinkedList<JTextField>();
        for (int i = 1; i <= parsedData.getTemperatureArray()[0].length - 1; i++) {
            JTextField thickfield = new JTextField("100", 4);
            thickfield.setName("thickfield" + i);
            thickfield.setHorizontalAlignment(JTextField.HORIZONTAL);
            thickfield.addKeyListener(this);
            thicks.add(thickfield);
            thickness.add(thicks.get(i - 1));
        }

        thickness.setBorder(new EtchedBorder());
        this.add(xChartPanel, BorderLayout.NORTH);
        this.add(thickness, BorderLayout.SOUTH);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() instanceof JTextField) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                for (int i = 0; i < xdata.length - 1; i++) {
                    xdata[i + 1] = xdata[i] + Integer.parseInt(thicks.get(i).getText());
                }
                this.update();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}