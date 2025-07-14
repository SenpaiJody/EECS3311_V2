package GUI.filter;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import GUI.GUIPanelBase;
import foodService.Filter;

public class FilterBuilderPanel extends GUIPanelBase{

	
	
	public Filter getFilter() {
		return null;
	}
	
	public FilterBuilderPanel() {
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBackground(Color.LIGHT_GRAY);
		
		
		
	}
}
