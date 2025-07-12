package GUI;

import java.awt.GridBagConstraints;

public class GBCUtility {
	public static GridBagConstraints createGBC(int gridx, int gridy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		return gbc;
	}
	public static GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, int gridheight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		return gbc;
	}

}
