package visualizationService;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


//with AI
public class TriplePiePlot extends Plot {

    private final PiePlot plot1;
    private final PiePlot plot2;
    private final PiePlot plot3;
    private final String title1;
    private final String title2;
    private final String title3;

    public TriplePiePlot(PiePlot plot1, PiePlot plot2, PiePlot plot3, String title1, String title2, String title3) {
        this.plot1 = plot1;
        this.plot2 = plot2;
        this.plot3 = plot3;
        this.title1 = title1;
        this.title2 = title2;
        this.title3 = title3;
    }

    @Override
    public String getPlotType() {
        return "Triple Pie Chart with Titles";
    }

    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState, PlotRenderingInfo info) {

        double width = area.getWidth() / 3;
        double titleHeight = 20;

        // Areas for drawing the pies (below the titles)
        Rectangle2D area1 = new Rectangle2D.Double(
                area.getX(), area.getY() + titleHeight, width, area.getHeight() - titleHeight
        );
        Rectangle2D area2 = new Rectangle2D.Double(
                area.getX() + width, area.getY() + titleHeight, width, area.getHeight() - titleHeight
        );
        Rectangle2D area3 = new Rectangle2D.Double(
                area.getX() + 2 * width, area.getY() + titleHeight, width, area.getHeight() - titleHeight
        );

        // Set title font
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        g2.setFont(titleFont);
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics(titleFont);
        float titleY = (float)(area.getY() + fm.getAscent());

        // Draw titles centered above each pie
        float title1X = (float)(area1.getCenterX() - fm.stringWidth(title1) / 2.0);
        float title2X = (float)(area2.getCenterX() - fm.stringWidth(title2) / 2.0);
        float title3X = (float)(area3.getCenterX() - fm.stringWidth(title3) / 2.0);
        g2.drawString(title1, title1X, titleY);
        g2.drawString(title2, title2X, titleY);
        g2.drawString(title3, title3X, titleY);

        // Draw the pie plots
        plot1.draw(g2, area1, anchor, null, info);
        plot2.draw(g2, area2, anchor, null, info);
        plot3.draw(g2, area3, anchor, null, info);
    }
}
