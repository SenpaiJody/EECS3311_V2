package GUI.reusables;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import GUI.GBCUtility;

//A PanelListItem that represents an ingredient and its quantity
public class IngredientQuantityListItem extends IngredientListItem {

	private double quantity;
	private boolean quantityEditable;
	private JSpinner quantityField;
	
	public double getQuantity() {
		if (!quantityEditable) {
			return quantity;
		}
		return (double)quantityField.getValue();
	}
	
	public IngredientQuantityListItem(PanelList list, int ingredientID, boolean removable, double quantity ,boolean quantityEditable) {
		super(list, ingredientID, removable);
		this.quantityEditable = quantityEditable;
		GridBagConstraints ingAmtConstraints = GBCUtility.createGBC(2, 0, 1, 2);
		ingAmtConstraints.anchor = GridBagConstraints.LINE_END;
		ingAmtConstraints.weightx = 0;
		
		
		if (!quantityEditable) {
			this.quantity =quantity;
			getInformationContainer().add(new JLabel(String.format("%.2f g", quantity)), ingAmtConstraints);
		}
		else {
			SpinnerNumberModel model = new SpinnerNumberModel(0.1d, 0.d, null, 0.1f);
			quantityField = new JSpinner(model);
			quantityField.setPreferredSize(new Dimension(60, 30));
			getInformationContainer().add(quantityField, ingAmtConstraints);
			
			GridBagConstraints labelConstraints = GBCUtility.createGBC(3, 0, 1, 2);
			labelConstraints.anchor = GridBagConstraints.LINE_END;
			labelConstraints.weightx = 0;
			getInformationContainer().add(new JLabel("g"), labelConstraints);
		}
			
		
	}
	

}
