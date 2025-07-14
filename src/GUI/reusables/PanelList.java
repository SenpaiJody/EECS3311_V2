package GUI.reusables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//A class that lists other panels
public class PanelList extends JPanel{
	private JPanel container; //list item container;
	
	List<PanelListItem> items = new ArrayList<PanelListItem>();
	public List<PanelListItem> getItems(){return items;}
	
	
	public PanelList(int width, int height){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		
		JPanel panel = new JPanel(); //panel that the ScrollPane will act on
		panel.setLayout(new BorderLayout()); //borderLayout so that the container starts at the top of the panel
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		
		container = new JPanel(); //container that will contain all of the list items, dynamically sizes to the size of the list items
		//container.setBorder(BorderFactory.createLineBorder(Color.RED));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); //GridLayout with N rows and 1 column 
		panel.add(container, BorderLayout.PAGE_START);
		
		panel.setBackground(Color.LIGHT_GRAY);
		add(new JScrollPane(panel));
		
	}
	
	public void addItem(PanelListItem item) {
		if(items.contains(item))
			return;
		
		container.add(item);
		items.add(item);
		
		container.revalidate();
		container.repaint();
		revalidate();
		repaint();
	}
	public void removeItem(PanelListItem item) {
		container.remove(item);
		items.remove(item);
		container.revalidate();
		container.repaint();
		revalidate();
		repaint();
	}
	public void removeAllItems() {
		container.removeAll();
		items.clear();
		container.revalidate();
		container.repaint();
		revalidate();
		repaint();
	}
	
	public boolean contains(PanelListItem item) {
		return (items.contains(item));
	}
}
