package GUI.reusables;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import GUI.GUIPanelBase;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;

public class IngredientSearchBar extends GUIPanelBase{

	static private final int MAX_RESULTS = 20;
	private Integer ingredientID;
	
	private JComboBox<String> searchBar;
	
	public Integer getIngredient(){
		return ingredientID;
	}
	
	public IngredientSearchBar() {
		searchBar = new JComboBox<String>();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		searchBar.setEditable(true);
		searchBar.setMinimumSize(new Dimension(200, 32));
		//searchBar.setPreferredSize(new Dimension(99999, 25));
		add(searchBar,gbc);
		attachListenerToSearchBar();
	}
	
	private void attachListenerToSearchBar() {
		searchBar.addActionListener(event->{
				if (event.getActionCommand().equals("comboBoxEdited")) {
					List<String> options = getSearchOptions((String)searchBar.getSelectedItem());
					searchBar.removeAllItems();
					for (String s : options) {
						searchBar.addItem(s);
					};
					if (options.size() > 0) {	
						searchBar.showPopup();
					}
						
				}

			});
		//updating ingredietnID when an ingredient is selected
		searchBar.addItemListener(event ->{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				IIngredientService ingredientSrv = IngredientServiceFactory.getService();
				List<Integer> searchResults = ingredientSrv.searchIngredientByName((String)searchBar.getSelectedItem(),1);
				if (searchResults != null && searchResults.size() > 0) {
					ingredientID = searchResults.get(0);
				}
			}
		});
	}
	
	private List<String> getSearchOptions(String input){
		if (input == null || input.length() == 0)
			return new ArrayList<String>();
		IIngredientService ingredientSrv = IngredientServiceFactory.getService();
		List<Integer> searchResults = ingredientSrv.searchIngredientByName(input, MAX_RESULTS);
		if (searchResults == null) {
			return new ArrayList<String>();
		}
		List<String> options = new ArrayList<String>(searchResults.size());
		for (Integer id : searchResults) {
			options.add(ingredientSrv.getIngredientName(id));
		}
		return options;
	}
}
