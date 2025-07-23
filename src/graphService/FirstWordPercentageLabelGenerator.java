package graphService;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;
import java.text.AttributedString;
import java.text.NumberFormat;

public class FirstWordPercentageLabelGenerator implements PieSectionLabelGenerator {

    private final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    @Override
    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        if (dataset == null || key == null) {
            return null;
        }

        String fullLabel = key.toString();
        String displayLabel = fullLabel;
        if (displayLabel.length() > 10) {
            displayLabel = displayLabel.substring(0, 9) + "...";
        }

        Number value = dataset.getValue(key);
        if (value == null) {
            return null;
        }

        double total = 0;
        for (int i = 0; i < dataset.getItemCount(); i++) {
            Number v = dataset.getValue(i);
            if (v != null) total += v.doubleValue();
        }

        double percent = total > 0 ? value.doubleValue() / total : 0;
        String percentString = percentFormat.format(percent);

        return percentString + " " + displayLabel;
    }

    @Override
    public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
        return null;  // Not used here
    }
}