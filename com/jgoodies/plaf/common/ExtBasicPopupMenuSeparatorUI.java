package com.jgoodies.plaf.common;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;

/**
 * Renders the separator in popup and pull-down menus.
 * Unlike its superclass we use a setting for the insets.
 *
 * @author Karsten Lentzsch
 */
public final class ExtBasicPopupMenuSeparatorUI extends BasicPopupMenuSeparatorUI {

	private static final int SEPARATOR_HEIGHT = 2;
	
	private Insets insets;
			
	
	public static ComponentUI createUI(JComponent b) { 
		return new ExtBasicPopupMenuSeparatorUI(); 
	}
	
	
	protected void installDefaults(JSeparator s) {
		super.installDefaults(s);
        insets = UIManager.getInsets("PopupMenuSeparator.margin");
	}
	

    public void paint(Graphics g, JComponent c ) {
        Dimension s = c.getSize();
        
        int topInset   = insets.top;
        int leftInset  = insets.left;
        int rightInset = insets.right;

        // Paint background
        g.setColor(UIManager.getColor("MenuItem.background"));
        g.fillRect(0, 0, s.width, s.height);

		// Draw side
		/*
		g.setColor(UIManager.getColor("controlHighlight"));
		g.drawLine(0, 0, 0, s.height -1);
		g.drawLine(s.width-1, 0, s.width-1, s.height-1);
		*/
		
		g.translate(0, topInset);	
		g.setColor(c.getForeground());
		g.drawLine(leftInset, 0, s.width - rightInset, 0);

		g.setColor(c.getBackground());
		g.drawLine(leftInset, 1, s.width -rightInset, 1);
		g.translate(0, -topInset);
    }
    

    public Dimension getPreferredSize(JComponent c) { 
    	return new Dimension(0, insets.top + SEPARATOR_HEIGHT + insets.bottom); 
    }
	
}