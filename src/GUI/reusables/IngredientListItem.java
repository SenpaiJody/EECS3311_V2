package GUI.reusables;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GUI.GBCUtility;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;

public class IngredientListItem extends PanelListItem{

	private int ingredientID;
	public int getIngredientID() { return ingredientID;};
	
	private JPanel informationContainer;
	protected JPanel getInformationContainer() {return informationContainer;}
	
	public IngredientListItem(PanelList list, int ingredientID, boolean removable) {
		this.ingredientID = ingredientID;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		if(removable) {
			JButton removeButton = new JButton("X");
			removeButton.setFont(removeButton.getFont().deriveFont(Font.BOLD).deriveFont(16.f));
			removeButton.setBackground(new Color(0xFF6368));
			removeButton.setActionCommand("remove");
			removeButton.addActionListener(e->{
				if (!e.getActionCommand().equals("remove"))
					return;
				list.removeItem(this);
			});
			add(removeButton);
		}
		addInformationContainer(ingredientID);
	}
	
	private void addInformationContainer(int ingredientID) {
		informationContainer = new JPanel();
		informationContainer.setLayout(new GridBagLayout());

		IIngredientService ingredientService = IngredientServiceFactory.getService();
		String ingName = ingredientService.getIngredientName(ingredientID);
		JLabel ingNameLabel = new JLabel(ingName!= null ? ingName : "[Not Found]");
		ingNameLabel.setFont(ingNameLabel.getFont().deriveFont(Font.BOLD));
		GridBagConstraints ingNameConstraints = GBCUtility.createGBC(0, 0, 1,1);
		ingNameConstraints.anchor = GridBagConstraints.LINE_START;
		ingNameConstraints.weightx = 0;
		informationContainer.add(ingNameLabel,ingNameConstraints);
		
		String ingFoodGroup = ingredientService.getFoodGroupName(ingredientService.getFoodGroup(ingredientID));

		JLabel ingFoodGroupLabel = new JLabel(ingFoodGroup != null ? "["+ingFoodGroup+"]" : "[Not Found]");
		ingFoodGroupLabel.setFont(getFont().deriveFont(Font.PLAIN));
		GridBagConstraints ingFoodGroupConstraints = GBCUtility.createGBC(0,1, 1,1);
		ingFoodGroupConstraints.anchor = GridBagConstraints.LINE_START;
		ingFoodGroupConstraints.weightx = 0;
		informationContainer.add(ingFoodGroupLabel,ingFoodGroupConstraints);
		
		GridBagConstraints filler = GBCUtility.createGBC(1, 0);
		filler.weightx = 1;
		informationContainer.add(Box.createHorizontalGlue(),filler);
		
		

//		
		add(informationContainer);
	}
	
	
	@Override 
	public boolean equals(Object obj) {
		
		if (obj == this)
			return true;
		if (obj.getClass() != this.getClass())
			return false;
		
		IngredientListItem casted = (IngredientListItem) obj;
		if (casted.ingredientID != this.ingredientID)
			return false;
		
		return true;
	}
}
