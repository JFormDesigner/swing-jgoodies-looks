/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import com.jgoodies.clearlook.ClearLookManager;
import com.sun.java.swing.plaf.windows.WindowsSplitPaneUI;


/**
 * The JGoodies Windows Look&amp;Feel implementation of
 * <code>SplitPaneUI</code>.
 * <p>
 * It can replace obsolete <code>Border</code>s and uses a special divider,
 * that paints modified one-touch buttons
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsSplitPaneUI extends WindowsSplitPaneUI {
	

	// Stores the original border, in case we replace it.
	private Border storedBorder;

	// Have we already checked the parent container?
	private boolean hasCheckedBorderReplacement = false;
	

	/**
	 * Creates and answers a <code>ExtWindowsSplitPaneUI</code> instance.
	 */
	public static ComponentUI createUI(JComponent x) { 
		return new ExtWindowsSplitPaneUI(); 
	}
	
	
	/**
	 * Creates the default divider.
	 */
	public BasicSplitPaneDivider createDefaultDivider() {
		return new ExtWindowsSplitPaneDivider(this);
	}
	
	
	/**
	 * Replaces the scrollpane's <code>Border</code> if appropriate,
	 * then paints.
	 */
    public void paint(Graphics g, JComponent c) {
    	if (!hasCheckedBorderReplacement) {
    		storedBorder = ClearLookManager.replaceBorder((JSplitPane) c);
    		hasCheckedBorderReplacement = true;
    	}
   		super.paint(g, c);
    }
    

	/**
	 * Restores the original <code>Border</code>, in case we replaced it.
	 */
    protected void uninstallDefaults() {
    	if (storedBorder != null) {
    		splitPane.setBorder(storedBorder);
    	}
    	super.uninstallDefaults();
    }
	
}