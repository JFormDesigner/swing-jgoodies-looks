package com.jgoodies.swing.util;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * A convenience class for easily creating mouse listeners for popup menus. 
 * It receives mouse events and shows its associated <code>JPopupMenu</code>
 * if and only if the event has the <code>popupTrigger</code> property.
 *
 * @author	Karsten Lentzsch
 */

public abstract class PopupAdapter extends MouseAdapter {
	
	/**
	 * Creates the popup menu.
	 */
	abstract protected JPopupMenu createPopupMenu();
	
	
	private Point getPopupMenuOrigin(Component component, Point mousePosition, Dimension menuSize) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point componentLocation = component.getLocationOnScreen();
		int screenRight = screenSize.width;
		int screenBottom = screenSize.height;
		int menuRight = componentLocation.x + mousePosition.x + menuSize.width;
		int menuBottom = componentLocation.y + mousePosition.y + menuSize.height;
		int rightGap = screenRight - menuRight;
		int bottomGap = screenBottom - menuBottom;
		Point result = new Point(mousePosition);
		//result.translate(-2, -2);
		result.translate(rightGap > 0 ? 0 : rightGap, bottomGap > 0 ? 0 : bottomGap);
		return result;
	}
	
	
	public void mousePressed(MouseEvent e) {
		popupMenuIfTriggered(e);
	}
	
	
	public void mouseReleased(MouseEvent e) {
		popupMenuIfTriggered(e);
	}
	
	
	private void popupMenuIfTriggered(MouseEvent e) {
		if (e.isPopupTrigger()) {
			JPopupMenu popupMenu = createPopupMenu();
			Point p = getPopupMenuOrigin(e.getComponent(), e.getPoint(), popupMenu.getPreferredSize());
			e.consume();
			popupMenu.show(e.getComponent(), p.x, p.y);
		}
	}
}