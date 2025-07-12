package GUI;

import java.awt.Font;

import javax.swing.JLabel;

public class LabelFactory {
	
	public static final Font DEFAULT_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
	
	
	public static JLabel createLabel(String content, Font font) {
		JLabel label = new JLabel();
		label.setFont(font);
		return label;
	};
	public static JLabel createLabel(String content, Font font, int horizontalAlignment) {
		JLabel label = createLabel(content, font);
		label.setHorizontalAlignment(horizontalAlignment);
		return label;
	}
}
