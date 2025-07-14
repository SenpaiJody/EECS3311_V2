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

	public static GridBagConstraints createFiller(int gridx, int gridy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		return gbc;
	}
	public static GridBagConstraints createGBC(int gridx, int gridy, int anchor) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.anchor = anchor;
		return gbc;
	}
	public static GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.anchor = anchor;
		return gbc;
	}
}
