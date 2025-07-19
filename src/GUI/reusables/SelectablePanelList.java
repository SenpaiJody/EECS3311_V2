package GUI.reusables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class SelectablePanelList extends PanelList implements MouseListener{

	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	

	
	public SelectablePanelList(int width, int height) {
		super(width, height);
	}
	private PanelListItem selectedItem;
	public PanelListItem getSelectedItem() {return selectedItem;}
	
	
	@Override
	public void addItem(PanelListItem item) {
		item.addMouseListener(this);
		super.addItem(item);
	}
	
	@Override
	public void removeItem(PanelListItem item) {
		item.removeMouseListener(this);
		super.removeItem(item);
	}
	
	@Override
	public void removeAllItems() {
		getItems().forEach(item->item.removeMouseListener(this));
		super.removeAllItems();
	}

	public void select(PanelListItem item) {
		if (selectedItem == item)
			return;
		if (selectedItem != null) {
			selectedItem.setBackground(selectedItem.getBackground().brighter());
		}
		item.setBackground(item.getBackground().darker());
		selectedItem = item;
		
		notifyActionListeners(item);
	}
	
	public void deselect() {
		if (selectedItem != null)
			selectedItem.setBackground(selectedItem.getBackground().brighter());
		selectedItem = null;
	}
	

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	public void removeActionlistener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	private void notifyActionListeners(PanelListItem item) {
		listeners.forEach(listener->listener.actionPerformed(new ActionEvent(item, ActionEvent.ACTION_PERFORMED, "ItemChanged")));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		select((PanelListItem)e.getSource());
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}
}
