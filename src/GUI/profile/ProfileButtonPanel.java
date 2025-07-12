package GUI.profile;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import GUI.GUIPanelBase;

public class ProfileButtonPanel extends GUIPanelBase implements MouseListener{

	ProfileButtonPanel(){
		addMouseListener(this);
	}
	
	
	@Override
	protected void buildSelf(){
		setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setBorder(BorderFactory.createLoweredBevelBorder());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setBackground(getBackground().darker());
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setBackground(getBackground().brighter());
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
	}

}
