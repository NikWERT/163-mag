import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Chart extends JPanel {

    private XYChart xyChart;
    private static double[] xdata = {0, 100, 200, 300};

    void update(int currChart, ParseData parsedData) {
        xyChart.setTitle(parsedData.getDataArray()[currChart]);
        xyChart.updateXYSeries("xySeries", xdata, parsedData.getTemperatureArray()[currChart], null);
    }

    Chart(ParseData parsedData) {
        xyChart = QuickChart.getChart(parsedData.getDataArray()[0], "Слои", "Температура", "xySeries", xdata, parsedData.getTemperatureArray()[0]);
        xyChart.getStyler().setLegendVisible(false);
        xyChart.getStyler().setYAxisMin(parsedData.getMinTemp() - 5);
        xyChart.getStyler().setYAxisMax(parsedData.getMaxTemp() + 5);

        this.setLayout(new BorderLayout());
        XChartPanel<XYChart> xChartPanel = new XChartPanel<>(xyChart);
        JPanel thickness = new JPanel();
        for (int i = 1; i <= parsedData.getTemperatureArray()[0].length; i++) {
            thickness.add(new JTextField("100"));
        }
        thickness.setBorder(new EtchedBorder());
        this.add(xChartPanel, BorderLayout.NORTH);
        this.add(thickness, BorderLayout.SOUTH);
    }
}