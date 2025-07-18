package graphService;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.AbstractMap;
import java.util.List;

import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;

@SuppressWarnings("serial")
public class MultiPiePlot extends Plot {

	@SuppressWarnings({ "rawtypes", "unused" })
	private final List<AbstractMap.SimpleEntry<PiePlot, String>> piePlotsWithTitles;


    @SuppressWarnings("rawtypes")
	public MultiPiePlot(List<AbstractMap.SimpleEntry<PiePlot, String>> piePlotsWithTitles) {
        if (piePlotsWithTitles == null || piePlotsWithTitles.isEmpty()) {
            throw new IllegalArgumentException("Plot list cannot be null or empty");
        }
        this.piePlotsWithTitles = piePlotsWithTitles;
    }


    @Override
	public String getPlotType() {
        return "Multi Pie Chart with Titles";
    }



    @Override
	@SuppressWarnings("rawtypes")
	public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState, PlotRenderingInfo info) {

        int count = piePlotsWithTitles.size();
        double widthPerPlot = area.getWidth() / count;
        double titleHeight = 20;

        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        g2.setFont(titleFont);
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics(titleFont);
        float titleY = (float)(area.getY() + fm.getAscent());

        for (int i = 0; i < count; i++) {
            PiePlot plot = piePlotsWithTitles.get(i).getKey();
            String title = piePlotsWithTitles.get(i).getValue();

            double x = area.getX() + i * widthPerPlot;
            Rectangle2D plotArea = new Rectangle2D.Double(
                x, area.getY() + titleHeight,
                widthPerPlot, area.getHeight() - titleHeight
            );

            // Center and draw the title
            float titleX = (float)(plotArea.getCenterX() - fm.stringWidth(title) / 2.0);
            g2.drawString(title, titleX, titleY);

            // Draw the pie
            plot.draw(g2, plotArea, anchor, null, info);
        }
    }

}
