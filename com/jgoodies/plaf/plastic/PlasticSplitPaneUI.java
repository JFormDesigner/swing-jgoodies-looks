package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.jgoodies.clearlook.ClearLookManager;


/**
 * The JGoodies Plastic Look and Feel implementation of
 * <code>SplitPaneUI</code>.<p>
 * 
 * It can replace obsolete <code>Border</code>s and uses a special divider,
 * that paints a single drag handle instead of many bumps.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticSplitPaneUI extends BasicSplitPaneUI {
	
	
	// Stores the original border, in case we replace it.
	private Border   storedBorder;

	// Have we already checked the parent container?
	private boolean hasCheckedBorderReplacement = false;
	
	
	public static ComponentUI createUI(JComponent x) {
		return new PlasticSplitPaneUI();
	}


	public BasicSplitPaneDivider createDefaultDivider() {
		return new PlasticSplitPaneDivider(this);
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