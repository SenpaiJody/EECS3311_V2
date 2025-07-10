package visualizationService;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

// with AI
public class DualPiePlot extends Plot {

    private final PiePlot plot1;
    private final PiePlot plot2;
    private final String title1;
    private final String title2;

    
    
    public DualPiePlot(PiePlot plot1, PiePlot plot2, String title1, String title2) {
        this.plot1 = plot1;
        this.plot2 = plot2;
        this.title1 = title1;
        this.title2 = title2;
    }


    
    @Override
    public String getPlotType() {
        return "Dual Pie Chart with Titles";
    }

    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState, PlotRenderingInfo info) {

        double width = area.getWidth() / 2;
        double titleHeight = 20;

        // Areas for drawing the pies (below the titles)
        Rectangle2D leftArea = new Rectangle2D.Double(
            area.getX(), area.getY() + titleHeight, width, area.getHeight() - titleHeight
        );
        Rectangle2D rightArea = new Rectangle2D.Double(
            area.getX() + width, area.getY() + titleHeight, width, area.getHeight() - titleHeight
        );

        // Set title font
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        g2.setFont(titleFont);
        g2.setColor(Color.BLACK);

        // Centered title for left pie
        FontMetrics fm = g2.getFontMetrics(titleFont);
        float leftTitleX = (float)(leftArea.getCenterX() - fm.stringWidth(title1) / 2.0);
        float leftTitleY = (float)(area.getY() + fm.getAscent());
        g2.drawString(title1, leftTitleX, leftTitleY);

        // Centered title for right pie
        float rightTitleX = (float)(rightArea.getCenterX() - fm.stringWidth(title2) / 2.0);
        float rightTitleY = leftTitleY;  // same Y as left
        g2.drawString(title2, rightTitleX, rightTitleY);

        // Draw pies
        plot1.draw(g2, leftArea, anchor, null, info);
        plot2.draw(g2, rightArea, anchor, null, info);
    }
}