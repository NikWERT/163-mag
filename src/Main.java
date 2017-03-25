import com.sun.scenario.effect.impl.sw.java.JSWBoxBlurPeer;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.area.AreaChart01;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a simple Chart using QuickChart
 */
public class Main {

    public static void main(String[] args) throws Exception {

        JFrame frame;
        frame = new JFrame("XChart Swing Demo");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        XChartPanel chartPanel = new XChartPanel((new AreaChart01()).getChart());
        frame.add(chartPanel);
        frame.setVisible(true);

        FlowLayout fl = new FlowLayout();

//        frame.setLayout(new BorderLayout());
        frame.setLayout(fl);


        for (int i = 0; i < 10; i++) {
            frame.add(new JButton("" + (i + 1)));
        }


        frame.pack();
    }
}