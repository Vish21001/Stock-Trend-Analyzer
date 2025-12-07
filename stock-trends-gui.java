import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class StockTrendsGUI extends JFrame {

    private List<Double> historical = new ArrayList<>();
    private List<Double> predicted = new ArrayList<>();

    public StockTrendsGUI() {
        setTitle("Stock Trends Analyzer - Java");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load JSON data
        loadData();

        // Add panel for chart
        add(new ChartPanel());
    }

    private void loadData() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject data = (JSONObject) parser.parse(new FileReader("frontend/data.json"));
            JSONArray hist = (JSONArray) data.get("historical");
            JSONArray pred = (JSONArray) data.get("predicted");

            for (Object o : hist) historical.add(Double.parseDouble(o.toString()));
            for (Object o : pred) predicted.add(Double.parseDouble(o.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ChartPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();

            int margin = 50;
            int chartWidth = width - 2*margin;
            int chartHeight = height - 2*margin;

            // Combine historical + predicted
            List<Double> all = new ArrayList<>(historical);
            all.addAll(predicted);

            double max = all.stream().max(Double::compare).get();
            double min = all.stream().min(Double::compare).get();

            // Draw axes
            g2.drawLine(margin, margin, margin, height - margin); // Y
            g2.drawLine(margin, height - margin, width - margin, height - margin); // X

            // Draw historical in blue
            g2.setColor(Color.BLUE);
            drawLineGraph(g2, historical, chartWidth, chartHeight, margin, min, max);

            // Draw predicted in red
            g2.setColor(Color.RED);
            drawLineGraph(g2, predicted, chartWidth, chartHeight, margin + historical.size(), min, max);
        }

        private void drawLineGraph(Graphics2D g2, List<Double> data, int chartWidth, int chartHeight, int startX, double min, double max) {
            int n = data.size();
            if (n < 2) return;

            for (int i = 0; i < n - 1; i++) {
                int x1 = startX + (i * chartWidth / (historical.size() + predicted.size()));
                int y1 = (int) ((max - data.get(i)) * chartHeight / (max - min)) + 50;
                int x2 = startX + ((i + 1) * chartWidth / (historical.size() + predicted.size()));
                int y2 = (int) ((max - data.get(i + 1)) * chartHeight / (max - min)) + 50;
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StockTrendsGUI gui = new StockTrendsGUI();
            gui.setVisible(true);
        });
    }
}
