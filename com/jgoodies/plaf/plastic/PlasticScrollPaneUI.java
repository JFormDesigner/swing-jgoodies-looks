/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollPaneUI;

import com.jgoodies.clearlook.ClearLookManager;


/**
 * The JGoodies Plastic Look&amp;Feel implementation of <code>ScrollPaneUI</code>.<p>
 * 
 * Can replace obsolete <code>Border</code>s.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticScrollPaneUI extends MetalScrollPaneUI {
	

	// Stores the original border, in case we replace it.
	private Border	  storedBorder;

	// Have we already checked the parent container?
	private boolean hasCheckedBorderReplacement = false;
	
	
	public static ComponentUI createUI(JComponent b) {
		return new PlasticScrollPaneUI();
	}
	
	
	/**
	 * Replaces the scrollpane's <code>Border</code> if appropriate,
	 * then paints.
	 */
    public void paint(Graphics g, JComponent c) {
    	if (!hasCheckedBorderReplacement) {
    		storedBorder = ClearLookManager.replaceBorder(c);
    		hasCheckedBorderReplacement = true;
    	}
   		super.paint(g, c);
    }
    

	/**
	 * Restores the original <code>Border</code>, in case we replaced it.
	 */
    protected void uninstallDefaults(JScrollPane scrollPane) {
    	if (storedBorder != null) {
    		scrollPane.setBorder(storedBorder);
    	}
    	super.uninstallDefaults(scrollPane);
    }
    
    
}