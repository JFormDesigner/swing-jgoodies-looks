package com.jgoodies.plaf.common;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;


/**
 * Renders aligned <code>JRadioButtonMenuItem</code>s.
 *
 * @author Karsten Lentzsch
 */
public class ExtBasicRadioButtonMenuItemUI extends ExtBasicMenuItemUI {
	
	protected String getPropertyPrefix() { return "RadioButtonMenuItem"; }
	
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtBasicRadioButtonMenuItemUI();
	}
	
	
	// RadioButtonMenuItems and CheckBoxMenuItems will override
	protected boolean iconBorderEnabled() { return true; }


	public void processMouseEvent(JMenuItem item, MouseEvent e, 
								   MenuElement path[], MenuSelectionManager manager) {
		Point p = e.getPoint();
		if (p.x >= 0 && p.x < item.getWidth() && 
		    p.y >= 0 && p.y < item.getHeight()) {
			if (e.getID() == MouseEvent.MOUSE_RELEASED) {
				manager.clearSelectedPath();
				item.doClick(0);
				item.setArmed(false);
			} else
				manager.setSelectedPath(path);
		} else if (item.getModel().isArmed()) {
			MenuElement newPath[] = new MenuElement[path.length - 1];
			int i, c;
			for (i = 0, c = path.length - 1; i < c; i++)
				newPath[i] = path[i];
			manager.setSelectedPath(newPath);
		}
	}

}