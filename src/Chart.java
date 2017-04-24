import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Chart extends JPanel {

    private XYChart xyChart;

    private static double[] xdata = {0, 100, 200, 300};

    void update(int currChart, Parsedata parsedata) {
        xyChart.setTitle(parsedata.getDataArray()[currChart]);
        xyChart.updateXYSeries("xySeries", xdata, parsedata.getTemperatureArray()[currChart], null);
    }

    Chart(Parsedata parsedata) {
        xyChart = QuickChart.getChart(parsedata.getDataArray()[0], "Слои", "Температура", "xySeries", xdata, parsedata.getTemperatureArray()[0]);
        xyChart.getStyler().setLegendVisible(false);
        xyChart.getStyler().setYAxisMin(parsedata.getMinTemp() - 5);
        xyChart.getStyler().setYAxisMax(parsedata.getMaxTemp() + 5);

        this.setLayout(new BorderLayout());
        XChartPanel xChartPanel = new XChartPanel(xyChart);
        JPanel thickness = new JPanel();
        for (int i = 1; i <= parsedata.getTemperatureArray()[0].length; i++) {
            thickness.add(new JTextField("100"));
        }
        thickness.setBorder(new EtchedBorder());
        this.add(xChartPanel, BorderLayout.NORTH);
        this.add(thickness, BorderLayout.SOUTH);
    }
}